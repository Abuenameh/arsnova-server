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

package net.particify.arsnova.core.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.core.style.ToStringCreator;

import net.particify.arsnova.core.model.serialization.View;

public class QtiAnswerBackup extends Answer {
  public static class QtiResponse {
    public QtiResponse() {

    }

    public QtiResponse(final String identifier, final String value) {
      this.identifier = identifier;
      this.value = value;
    }

    // @NotBlank
    private String identifier;

    private String value;

    @JsonView({View.Persistence.class, View.Public.class})
    public String getIdentifier() {
      return identifier;
    }

    @JsonView({View.Persistence.class, View.Public.class})
    public void setIdentifier(final String identifier) {
      this.identifier = identifier;
    }

    @JsonView({View.Persistence.class, View.Public.class})
    public String getValue() {
      return value;
    }

    @JsonView({View.Persistence.class, View.Public.class})
    public void setValue(final String value) {
      this.value = value;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (!super.equals(o)) {
        return false;
      }
      final QtiResponse that = (QtiResponse) o;

      return Objects.equals(identifier, that.identifier) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(identifier, value);
    }

    @Override
    public String toString() {
      return new ToStringCreator(this)
          .append("identifier", identifier)
          .append("value", value)
          .toString();
    }

  }

  private List<@NotNull QtiResponse> responses = new ArrayList<>();

  public QtiAnswerBackup() {

  }

  public QtiAnswerBackup(final QtiContent content, final String creatorId) {
    super(content, creatorId);
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public List<QtiResponse> getResponses() {
    return responses;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public void setResponses(final List<QtiResponse> responses) {
    this.responses = responses;
  }

  @Override
  public boolean isAbstention() {
    return responses.isEmpty();
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * All fields of <tt>ChoiceAnswer</tt> are included in equality checks.
   * </p>
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    final QtiAnswerBackup that = (QtiAnswerBackup) o;

    return Objects.equals(responses, that.responses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), responses);
  }

  @Override
  protected ToStringCreator buildToString() {
    return super.buildToString()
        .append("responses", responses);
  }
}
