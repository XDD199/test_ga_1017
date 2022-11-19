package com.example.test_ga_1017.moea;

import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;

public class AdditiveEpsilonIndicatorFitnessEvaluator2 extends IndicatorFitnessEvaluator2 {
    public AdditiveEpsilonIndicatorFitnessEvaluator2(Problem problem) {
        super(problem);
    }

    protected double calculateIndicator(Solution solution1, Solution solution2) {
        //double eps = -1.0D / 0.0;
        double eps = -1000;

        for(int i = 0; i < this.getProblem().getNumberOfObjectives(); ++i) {
            double temp_eps = solution1.getObjective(i) - solution2.getObjective(i);
            if (temp_eps > eps) {
                eps = temp_eps;
            }
        }

        return eps;
    }

    public boolean areLargerValuesPreferred() {
        return false;
    }
}
