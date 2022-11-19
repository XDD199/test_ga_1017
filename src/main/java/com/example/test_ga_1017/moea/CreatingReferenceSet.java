package com.example.test_ga_1017.moea;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.spi.AlgorithmFactory;

import java.io.File;

public class CreatingReferenceSet {
    public static void main(String[] args) throws Exception {
        AlgorithmFactory.getInstance().addProvider(new IBEA2Provider());
        int nseeds = 30;
        String[] algorithms = new String[]{"NSGAIII", "MOEAD", "IBEA", "IBEA2"};

        NondominatedPopulation referenceSet = new NondominatedPopulation();

        Executor executor = new Executor().withProblemClass(BasicProblem1111.class).withMaxEvaluations(10000);

        for (String algorithm: algorithms) {
            if (algorithm.equals("IBEA") || algorithm.equals("IBEA2")) {
                executor.withAlgorithm(algorithm)
                        .withProperty("indicator", "epsilon");
            } else {
                executor.withAlgorithm(algorithm);
            }
            for (int i = 0; i < nseeds; i++) {
                referenceSet.addAll(executor.run());
            }
        }

        PopulationIO.writeObjectives(new File("D:\\Code\\MOEA_WORK\\1117\\BasicProblem1111_IBEA2.pf"), referenceSet);
    }
}
