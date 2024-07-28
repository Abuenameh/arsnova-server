package net.particify.arsnova.core.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;

import net.particify.arsnova.core.model.serialization.View;

public class QtiAnswerStatistics extends AnswerStatistics {
  public static class QtiRoundStatistics extends RoundStatistics {
    private List<String> responses;
    private List<Integer> independentCounts;

    @JsonView(View.Public.class)
    public List<String> getResponses() {
      return responses;
    }

    @JsonView(View.Public.class)
    public void setResponses(final List<String> responses) {
      this.responses = responses;
    }

    @JsonView(View.Public.class)
    public List<Integer> getIndependentCounts() {
      if (independentCounts == null) {
        independentCounts = new ArrayList<>();
      }

      return independentCounts;
    }

    public void setIndependentCounts(final List<Integer> independentCounts) {
      this.independentCounts = independentCounts;
    }
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
