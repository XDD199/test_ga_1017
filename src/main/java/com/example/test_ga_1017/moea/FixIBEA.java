package com.example.test_ga_1017.moea;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.dto.RecipeFood;
import com.example.test_ga_1017.ga.MoeaTest;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.service.RecipeService;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;
import org.moeaframework.core.spi.AlgorithmProvider;
import org.moeaframework.core.variable.EncodingUtils;

import java.io.File;
import java.util.List;

public class FixIBEA {
    private static IRecipeService recipeService = new RecipeService();

    public static void main(String[] args) {
        //potObjective();
        compareIBEA();
    }

    public static void potObjective() {
        AlgorithmFactory.getInstance().addProvider(new IBEA2Provider());
        NondominatedPopulation result = new Executor()
                .withAlgorithm("IBEA2")
                .withProperty("indicator", "epsilon")
                .withProblemClass(ChangeProblem1111.class)
                .withMaxEvaluations(10000)
                .run();

        //new Plot().add("IBEA", result).show();
        double[] mins = new double[15];
        double[] maxs = new double[15];

        for (Solution solution : result) {
            for (int i = 0; i < 15; i++) {
                mins[i] = Math.min(mins[i], solution.getObjective(i));
                maxs[i] = Math.max(maxs[i], solution.getObjective(i));
            }
        }
        for (int i = 0; i < 15; i++) {
            System.out.println(maxs[i] - mins[i]);
        }
    }


    public static void compareIBEA() {
        AlgorithmFactory.getInstance().addProvider(new IBEA2Provider());
        String[] algorithms = {"IBEA2", "IBEA"};

        Executor executor = new Executor()
                .withProblemClass(BasicProblem1111.class)
                .withMaxEvaluations(10000);

        //HV IBEA better
//        Analyzer analyzer = new Analyzer()
//                .withReferenceSet(new File("D:\\Code\\MOEA_WORK\\1117\\BasicProblem1111_IBEA2.pf"))
//                .withSameProblemAs(executor)
//                .includeHypervolume()
//                .showStatisticalSignificance();

        //IGD IBEA better
//        Analyzer analyzer = new Analyzer()
//                .withReferenceSet(new File("D:\\Code\\MOEA_WORK\\1117\\BasicProblem1111_IBEA2.pf"))
//                .withSameProblemAs(executor)
//                .includeInvertedGenerationalDistance()
//                .showStatisticalSignificance();

        //GD IBEA2 better
        Analyzer analyzer = new Analyzer()
                .withReferenceSet(new File("D:\\Code\\MOEA_WORK\\1117\\BasicProblem1111_IBEA2.pf"))
                .withSameProblemAs(executor)
                .includeGenerationalDistance()
                .showStatisticalSignificance();

        for (String algorithm : algorithms) {
            analyzer.addAll(algorithm, executor.withAlgorithm(algorithm).withProperty("indicator", "epsilon").runSeeds(50));
        }

        analyzer.printAnalysis();

        new Plot().add(analyzer).show();
    }
}
