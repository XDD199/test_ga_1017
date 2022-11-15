package com.example.test_ga_1017.moea;

import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;

import java.io.File;

public class ComparingAlgorithms {

    public static void main(String[] args) {
        String[] algorithms = {"NSGAIII", "MOEAD", "IBEA"};

        Executor executor = new Executor()
                .withProblemClass(BasicProblem1111.class)
                .withMaxEvaluations(10000);

        //HV
        Analyzer analyzer = new Analyzer()
                .withReferenceSet(new File("D:\\Code\\MOEA_WORK\\1114\\BasicProblem1111.pf"))
                .withSameProblemAs(executor)
                .includeHypervolume()
                .showStatisticalSignificance();
        //IGD
//        Analyzer analyzer = new Analyzer()
//                .withReferenceSet(new File("D:\\Code\\MOEA_WORK\\1114\\BasicProblem1111.pf"))
//                .withSameProblemAs(executor)
//                .includeInvertedGenerationalDistance()
//                .showStatisticalSignificance();

        //GD
//        Analyzer analyzer = new Analyzer()
//                .withReferenceSet(new File("D:\\Code\\MOEA_WORK\\1114\\BasicProblem1111.pf"))
//                .withSameProblemAs(executor)
//                .includeGenerationalDistance()
//                .showStatisticalSignificance();

        for (String algorithm : algorithms) {
            if (algorithm.equals("IBEA")) {
                analyzer.addAll(algorithm, executor.withAlgorithm(algorithm).withProperty("indicator", "epsilon").runSeeds(50));
            } else {
                analyzer.addAll(algorithm, executor.withAlgorithm(algorithm).runSeeds(50));
            }
        }

        analyzer.printAnalysis();

        new Plot().add(analyzer).show();
    }
}
