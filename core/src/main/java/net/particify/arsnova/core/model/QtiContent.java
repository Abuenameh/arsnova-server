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

import net.particify.arsnova.core.model.serialization.View;

public class QtiContent extends Content {
  // public static class AnswerOption {
  //   public AnswerOption() {

  //   }

  //   public AnswerOption(final String label) {
  //     this.label = label;
  //   }

  //   @NotBlank
  //   private String label;

  //   private String renderedLabel;

  //   @JsonView({View.Persistence.class, View.Public.class})
  //   public String getLabel() {
  //     return label;
  //   }

  //   @JsonView({View.Persistence.class, View.Public.class})
  //   public void setLabel(final String label) {
  //     this.label = label;
  //   }

  //   @JsonView(View.Public.class)
  //   public String getRenderedLabel() {
  //     return renderedLabel;
  //   }

  //   public void setRenderedLabel(final String renderedLabel) {
  //     this.renderedLabel = renderedLabel;
  //   }

  //   @Override
  //   public boolean equals(final Object o) {
  //     if (this == o) {
  //       return true;
  //     }
  //     if (!super.equals(o)) {
  //       return false;
  //     }
  //     final AnswerOption that = (AnswerOption) o;

  //     return Objects.equals(label, that.label);
  //   }

  //   @Override
  //   public int hashCode() {
  //     return Objects.hash(label);
  //   }

  //   @Override
  //   public String toString() {
  //     return new ToStringCreator(this)
  //         .append("label", label)
  //         .toString();
  //   }

  // }

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
    // return correctOptionIndexes.size() > 0;
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

    final double achievedPoints = 0;
    final AnswerResult.AnswerResultState state = achievedPoints > getPoints() * 0.999
        ? AnswerResult.AnswerResultState.CORRECT : AnswerResult.AnswerResultState.WRONG;
    // final double achievedPoints = calculateAchievedPoints(answer.getSelectedChoiceIndexes());
    // final AnswerResult.AnswerResultState state = achievedPoints > getPoints() * 0.999
    //     ? AnswerResult.AnswerResultState.CORRECT : AnswerResult.AnswerResultState.WRONG;

    return new AnswerResult(
        this.id,
        achievedPoints,
        this.getPoints(),
        state);
  }

  // public double calculateAchievedPoints(final List<Integer> selectedChoiceIndexes) {
  //   if (getFormat() == Format.SORT) {
  //     return selectedChoiceIndexes.equals(correctOptionIndexes) ? getPoints() : 0;
  //   }

  //   final double pointsPerOption = 1.0 * getPoints() / correctOptionIndexes.size();
  //   return Math.max(0, selectedChoiceIndexes.stream()
  //       .mapToDouble(i -> (correctOptionIndexes.contains(i) ? pointsPerOption : -pointsPerOption))
  //       .sum());
  // }

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
