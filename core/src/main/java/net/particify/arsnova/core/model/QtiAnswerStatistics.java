package net.particify.arsnova.core.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;

import net.particify.arsnova.core.model.serialization.View;

public class QtiAnswerStatistics extends AnswerStatistics {
  public static class QtiRoundStatistics extends RoundStatistics {
    // private List<Double> scores;
    // private List<Integer> independentCounts;

    // @JsonView(View.Public.class)
    // public List<Double> getScores() {
    //   if (scores == null) {
    //     scores = new ArrayList<>();
    //   }
    //   return scores;
    // }

    // @JsonView(View.Public.class)
    // public void setScores(final List<Double> scores) {
    //   this.scores = scores;
    // }

    // @JsonView(View.Public.class)
    // public List<Integer> getIndependentCounts() {
    //   if (independentCounts == null) {
    //     independentCounts = new ArrayList<>();
    //   }
    //   return independentCounts;
    // }

    // @JsonView(View.Public.class)
    // public void setIndependentCounts(final List<Integer> independentCounts) {
    //   this.independentCounts = independentCounts;
    // }

    // @JsonView(View.Public.class)
    // public Double getCorrectAnswerFraction() {
    //   return correctAnswerFraction;
    // }

    // @JsonView(View.Public.class)
    // public void setCorrectAnswerFraction(final Double correctAnswerFraction) {
    //   this.correctAnswerFraction = correctAnswerFraction;
    // }
  }

  private List<QtiRoundStatistics> roundStatistics;

  @JsonView(View.Public.class)
  public List<QtiRoundStatistics> getRoundStatistics() {
    return roundStatistics;
  }

  public void setRoundStatistics(final List<QtiRoundStatistics> roundStatistics) {
    this.roundStatistics = roundStatistics;
  }
}
