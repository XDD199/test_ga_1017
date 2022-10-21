package com.example.test_ga_1017.ga;

import org.moeaframework.Executor;
import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

public class RunSchafferProblem {
    public static void main(String[] args) {
//        Instrumenter instrumenter = new Instrumenter()
//                .withProblemClass(SchafferProblem.class)
//                .withFrequency(100)
//                .attachAll();

        NondominatedPopulation result = new Executor()
        .withAlgorithm("NSGAII")
        .withProblemClass(SchafferProblem.class)
        .withMaxEvaluations(10000)
        .run();

        for (Solution solution : result) {
            System.out.printf("%d, %d, %d, %d => %.5f, %.5f\n",
                    EncodingUtils.getInt(solution.getVariable(0)),
                    EncodingUtils.getInt(solution.getVariable(1)),
                    EncodingUtils.getInt(solution.getVariable(2)),
                    EncodingUtils.getInt(solution.getVariable(3)),
                    solution.getObjective(0),
                    solution.getObjective(1));
            }
        new Plot()
        .add("NSGAII", result)
        .show();

//        Accumulator accumulator = instrumenter.getLastAccumulator();
//        for (int i = 0; i < accumulator.size("NFE"); i++) {
//            System.out.println(accumulator.get("NFE", i) + "\t" + accumulator.get("GenerationalDistance", i));
//        }
    }

}
