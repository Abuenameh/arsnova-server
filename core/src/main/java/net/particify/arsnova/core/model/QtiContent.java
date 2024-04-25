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
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.springframework.core.style.ToStringCreator;
import java.lang.foreign.Arena;

import net.particify.arsnova.core.model.serialization.View;
import static citolab.qti.scoringengine.libScoringEngine_h.Score;

public class QtiContent extends Content {
  private String qtiItem;

  public QtiContent() {

  }

  public QtiContent(final QtiContent content) {
    super(content);
    this.qtiItem = content.qtiItem;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public String getQtiItem() {
    return qtiItem;
  }

  @JsonView({View.Persistence.class, View.Public.class})
  public void setQtiItem(final String qtiItem) {
    this.qtiItem = qtiItem;
  }

  @Override
  @JsonView(View.Public.class)
  public int getPoints() {
    return isScorable() ? 10 : 0;
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
          this.getPoints(),
          AnswerResult.AnswerResultState.ABSTAINED);
    }

    if (!isScorable()) {
      return new AnswerResult(
          this.id,
          0,
          this.getPoints(),
          AnswerResult.AnswerResultState.NEUTRAL);
    }

    StringBuilder assessmentItem = new StringBuilder();
    assessmentItem.append(STR."""
                                  <qti-assessment-item identifier="item">
                                      \{qtiItem}
                                  </qti-assessment-item>
                              """);

    List<QtiAnswer.QtiResponse> responses = answer.getResponses();
    StringBuilder assessmentResult = new StringBuilder();

    assessmentResult.append("""
                                <assessmentResult>
                                    <itemResult identifier="item">
                            """);
    for (QtiAnswer.QtiResponse response : responses) {
      assessmentResult.append(STR."""
                                      <responseVariable identifier="\{response.getIdentifier()}" cardinality="\{response.getCardinality()}" baseType="\{response.getBaseType()}">
                                          <candidateResponse>
                                  """);
      switch (response.getCardinality()) {
        case "single":
          assessmentResult.append(STR."<value>\{response.getValue()}</value>");
          break;
        case "multiple":
          for (String value : response.getValues()) {
            assessmentResult.append(STR."<value>\{value}</value>");
          }
          break;
      }
      assessmentResult.append("""
                                      </candidateResponse>
                                  </responseVariable>
                              """);
    }
    assessmentResult.append("""
                                    </itemResult>
                                </assessmentResult>
                            """);

    double achievedPoints = 0;
    try (var arena = Arena.ofConfined()) {
      var assessmentItemStr = arena.allocateUtf8String(assessmentItem.toString());
      var assessmentResultStr = arena.allocateUtf8String(assessmentResult.toString());
      achievedPoints = Score(assessmentItemStr, assessmentResultStr) * this.getPoints();
    }
    final AnswerResult.AnswerResultState state = achievedPoints > 0.999
        ? AnswerResult.AnswerResultState.CORRECT : AnswerResult.AnswerResultState.WRONG;

    return new AnswerResult(
        this.id,
        achievedPoints,
        this.getPoints(),
        state);
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

    return qtiItem == that.qtiItem;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), qtiItem);
  }

  @Override
  protected ToStringCreator buildToString() {
    return super.buildToString()
        .append("qtiItem", qtiItem);
  }
}
