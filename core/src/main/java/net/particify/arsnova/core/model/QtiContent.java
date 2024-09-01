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
import java.util.Objects;
import org.springframework.core.style.ToStringCreator;

import net.particify.arsnova.core.model.serialization.View;

public class QtiContent extends Content {
  private String qtiItem;
  private boolean showResponses;

  public QtiContent() {

  }

  public QtiContent(final QtiContent content) {
    super(content);
    this.qtiItem = content.qtiItem;
    this.showResponses = content.showResponses;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public String getQtiItem() {
    return qtiItem;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public void setQtiItem(final String qtiItem) {
    this.qtiItem = qtiItem;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public boolean getShowResponses() {
    return showResponses;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public void setShowResponses(final boolean showResponses) {
    this.showResponses = showResponses;
  }

  @Override
  @JsonView(View.Public.class)
  public boolean isScorable() {
    return true;
  }

  @Override
  public AnswerResult determineAnswerResult(final Answer answer) {
    if (answer instanceof QtiAnswer) {
      return determineAnswerResult((QtiAnswer) answer);
    }

    return super.determineAnswerResult(answer);
  }

  public AnswerResult determineAnswerResult(final QtiAnswer answer) {
    if (answer.isAbstention()) {
      return new AnswerResult(
          this.id,
          0,
          0,
          this.getPoints(),
          0,
          AnswerResult.AnswerResultState.ABSTAINED);
    }

    if (!isScorable()) {
      return new AnswerResult(
          this.id,
          0,
          0,
          this.getPoints(),
          0,
          AnswerResult.AnswerResultState.NEUTRAL);
    }

    final double achievedPoints = calculateAchievedPoints(answer.getScore(), answer.getMaxScore());
    final AnswerResult.AnswerResultState state = achievedPoints > 0.999 * this.getPoints()
        ? AnswerResult.AnswerResultState.CORRECT : (achievedPoints > 0 && achievedPoints < 0.999 * this.getPoints())
        ? AnswerResult.AnswerResultState.PARTIALLY_CORRECT : AnswerResult.AnswerResultState.WRONG;
    final double competitivePoints =
        calculateCompetitivePoints(answer.getCreationTimestamp().toInstant(), achievedPoints);

    return new AnswerResult(
        this.id,
        achievedPoints,
        competitivePoints,
        this.getPoints(),
        answer.getDurationMs(),
        state);
  }

  @Override
  public double calculateAchievedPoints(final Answer answer) {
    if (answer instanceof QtiAnswer qtiAnswer) {
      return calculateAchievedPoints(qtiAnswer.getScore(), qtiAnswer.getMaxScore());
    }
    return super.calculateAchievedPoints(answer);
  }

  private double calculateAchievedPoints(final double score, final double maxScore) {
    return (score / maxScore) * this.getPoints();
  }

  @Override
  public QtiContent copy() {
    return new QtiContent(this);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * All fields of <tt>QtiContent</tt> are included in equality checks.
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
    final QtiContent that = (QtiContent) o;

    return Objects.equals(qtiItem, that.qtiItem) && showResponses == that.showResponses;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), qtiItem, showResponses);
  }

  @Override
  protected ToStringCreator buildToString() {
    return super.buildToString()
        .append("qtiItem", qtiItem)
        .append("showResponses", showResponses);
  }
}
