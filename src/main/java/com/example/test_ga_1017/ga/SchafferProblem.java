package com.example.test_ga_1017.ga;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

public class SchafferProblem extends AbstractProblem {

    public SchafferProblem() {
        super(4, 2);
    }

    @Override
    public void evaluate(Solution solution) {
        int[] x = EncodingUtils.getInt(solution);
        double f1 = 0, f2 = 0;
        f1 = Math.abs(x[0]*447.4/130 + x[1]*88.974/116.1 + x[2]*123.261/108.2 + x[3]*180.827/108.1 - 1160);
        f2 = Math.abs(x[0]*10.93/130 + x[1]*2.76838/116.1 + x[2]*11.7727/108.2 + x[3]*9.8596/108.1 - 30);
        solution.setObjective(0, f1);
        solution.setObjective(1, f2);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(4, 2);
        solution.setVariable(0, EncodingUtils.newInt(80, 160));
        solution.setVariable(1, EncodingUtils.newInt(80, 160));
        solution.setVariable(2, EncodingUtils.newInt(80, 160));
        solution.setVariable(3, EncodingUtils.newInt(80, 160));
        return solution;
    }
}
