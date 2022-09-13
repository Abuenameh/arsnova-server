/*
 * This file is part of ARSnova Backend.
 * Copyright (C) 2012-2019 The ARSnova Team and Contributors
 *
 * ARSnova Backend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ARSnova Backend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.thm.arsnova.model;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import org.springframework.core.style.ToStringCreator;

import de.thm.arsnova.model.serialization.FormatContentTypeIdResolver;
import de.thm.arsnova.model.serialization.View;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.CUSTOM,
		property = "format",
		visible = true,
		defaultImpl = Content.class
)
@JsonTypeIdResolver(FormatContentTypeIdResolver.class)
public class Content extends Entity implements RoomIdAware {
	public enum Format {
		CHOICE,
		BINARY,
		SCALE,
		NUMBER,
		TEXT,
		GRID,
		SLIDE,
		SORT,
		FLASHCARD,
		WORDCLOUD,
		PRIORIZATION
	}

	public static class State {
		@Positive
		private int round = 1;

		private Date answeringEndTime;
		private boolean answersPublished;

		@JsonView({View.Persistence.class, View.Public.class})
		public int getRound() {
			return round;
		}

		@JsonView({View.Persistence.class, View.Public.class})
		public void setRound(final int round) {
			this.round = round;
		}

		@JsonView({View.Persistence.class, View.Public.class})
		public Date getAnsweringEndTime() {
			return answeringEndTime;
		}

		@JsonView({View.Persistence.class, View.Public.class})
		public void setAnsweringEndTime(final Date answeringEndTime) {
			this.answeringEndTime = answeringEndTime;
		}

		@JsonView({View.Persistence.class, View.Public.class})
		public boolean isAnswersPublished() {
			return answersPublished;
		}

		@JsonView({View.Persistence.class, View.Public.class})
		public void setAnswersPublished(final boolean answersPublished) {
			this.answersPublished = answersPublished;
		}

		public boolean isAnswerable() {
			return answeringEndTime == null || answeringEndTime.compareTo(new Date()) > 0;
		}

		@Override
		public int hashCode() {
			return Objects.hash(round, answeringEndTime);
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (!super.equals(o)) {
				return false;
			}
			final State state = (State) o;

			return round == state.round
					&& Objects.equals(answeringEndTime, state.answeringEndTime);
		}

		@Override
		public String toString() {
			return new ToStringCreator(this)
					.append("round", round)
					.append("answeringEndTime", answeringEndTime)
					.toString();
		}
	}

	private static final String SUBJECT_LEGACY_PLACEHOLDER = "Subject";

	@NotEmpty
	private String roomId;

	@NotNull
	private String subject = "";

	@NotBlank
	private String body;

	private String renderedBody;

	@NotNull
	private Format format;

	private Set<String> groups;
	private boolean abstentionsAllowed;

	@JsonMerge
	private State state;

	private Date timestamp;
	private String additionalText;
	private String renderedAdditionalText;
	private String additionalTextTitle;

	private TextRenderingOptions bodyRenderingOptions;

	{
		this.bodyRenderingOptions = new TextRenderingOptions();
		this.addRenderingMapping(
				this::getBody,
				this::setRenderedBody,
				this.bodyRenderingOptions);
		this.addRenderingMapping(
				this::getAdditionalText,
				this::setRenderedAdditionalText,
				this.bodyRenderingOptions);
	}

	public Content() {

	}

	/**
	 * Copying constructor which adopts most of the original content's
	 * properties which are not used to store relations to other data.
	 */
	public Content(final Content content) {
		this.body = content.body;
		this.format = content.format;
		this.abstentionsAllowed = content.abstentionsAllowed;
		this.state = content.state;
		this.timestamp = content.timestamp;
		this.additionalText = content.additionalText;
		this.additionalTextTitle = content.additionalTextTitle;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public String getRoomId() {
		return roomId;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setRoomId(final String roomId) {
		this.roomId = roomId;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public String getSubject() {
		return "";
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setSubject(final String subject) {
		this.subject = subject != null ? subject : "";
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public String getBody() {
		return (!subject.isBlank() && !subject.equals(SUBJECT_LEGACY_PLACEHOLDER) && !body.startsWith(subject))
				? "**" + subject + "**\n\n" + body
				: body;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setBody(final String body) {
		this.body = body;
	}

	@JsonView(View.Public.class)
	public String getRenderedBody() {
		return renderedBody;
	}

	public void setRenderedBody(final String renderedBody) {
		this.renderedBody = renderedBody;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public Format getFormat() {
		return format;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setFormat(final Format format) {
		this.format = format;
		this.bodyRenderingOptions.setMarkdownFeatureset(
				format == Format.SLIDE || format == Format.FLASHCARD
				? TextRenderingOptions.MarkdownFeatureset.EXTENDED
				: TextRenderingOptions.MarkdownFeatureset.SIMPLE);
	}

	@JsonView(View.Public.class)
	public Set<String> getGroups() {
		if (groups == null) {
			groups = new HashSet<>();
		}

		return groups;
	}

	/* Content groups are persisted in the Room */
	@JsonView(View.Public.class)
	public void setGroups(final Set<String> groups) {
		this.groups = groups;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public State getState() {
		return state != null ? state : (state = new State());
	}

	public void resetState() {
		this.state = new State();
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setState(final State state) {
		this.state = state;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public Date getTimestamp() {
		return timestamp;
	}

	@JsonView(View.Persistence.class)
	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public String getAdditionalText() {
		return additionalText;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setAdditionalText(final String additionalText) {
		this.additionalText = additionalText;
	}

	@JsonView({View.Persistence.class, View.Extended.class})
	public String getAdditionalTextTitle() {
		return additionalTextTitle;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setAdditionalTextTitle(final String additionalTextTitle) {
		this.additionalTextTitle = additionalTextTitle;
	}

	@JsonView(View.Public.class)
	public String getRenderedAdditionalText() {
		return renderedAdditionalText;
	}

	public void setRenderedAdditionalText(final String renderedAdditionalText) {
		this.renderedAdditionalText = renderedAdditionalText;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public boolean isAbstentionsAllowed() {
		return abstentionsAllowed;
	}

	@JsonView({View.Persistence.class, View.Public.class})
	public void setAbstentionsAllowed(final boolean abstentionsAllowed) {
		this.abstentionsAllowed = abstentionsAllowed;
	}

	@JsonView(View.Public.class)
	public int getPoints() {
		return 0;
	}

	@JsonView(View.Public.class)
	public boolean isScorable() {
		return false;
	}

	public AnswerResult determineAnswerResult(final Answer answer) {
		final AnswerResult.AnswerResultState state = answer.isAbstention()
				? AnswerResult.AnswerResultState.ABSTAINED
				: AnswerResult.AnswerResultState.NEUTRAL;
		return new AnswerResult(this.id, 0, this.getPoints(), state);
	}

	@JsonView(View.Persistence.class)
	@Override
	public Class<? extends Entity> getType() {
		return Content.class;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * The following fields of <tt>LogEntry</tt> are excluded from equality checks:
	 * {@link #state}.
	 * </p>
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!super.equals(o)) {
			return false;
		}
		final Content content = (Content) o;

		return Objects.equals(roomId, content.roomId)
				&& Objects.equals(subject, content.subject)
				&& Objects.equals(body, content.body)
				&& format == content.format
				&& Objects.equals(groups, content.groups)
				&& Objects.equals(timestamp, content.timestamp);
	}

	@Override
	protected ToStringCreator buildToString() {
		return super.buildToString()
				.append("roomId", roomId)
				.append("subject", subject)
				.append("body", body)
				.append("format", format)
				.append("groups", groups)
				.append("abstentionsAllowed", abstentionsAllowed)
				.append("state", state)
				.append("additionalText", additionalText)
				.append("additionalTextTitle", additionalTextTitle)
				.append("timestamp", timestamp);
	}
}
