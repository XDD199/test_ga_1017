package com.example.test_ga_1017;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.ga.MoeaTest;
import com.example.test_ga_1017.service.IRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.analysis.sensitivity.OutputWriter;
import org.moeaframework.analysis.sensitivity.ResultEntry;
import org.moeaframework.analysis.sensitivity.ResultFileWriter;
import org.moeaframework.core.NondominatedPopulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FindBestSolution {
    @Autowired
    private IRecipeService recipeService;

    @Test
    void getReferenceSet() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "西葫芦炒蛋", "蒜泥秋葵"};
        List<Recipe> list = recipeService.getRecipeList(names);
        Recipe max = recipeService.countRecipeList(list, new int[]{200, 200, 200, 200});
        MoeaTest.setMax(max);
        MoeaTest.setList(list);

        List<NondominatedPopulation> result = new Executor()
                .withAlgorithm("NSGAIII")
                .withProblemClass(MoeaTest.class)
                .withMaxEvaluations(10000)
                .runSeeds(1000);
        int i = 0;
        File file = new File("D:\\Code\\1110test_best_moea\\sec_NSGA3_referenceSet_seed_1000.set");
        file.createNewFile();
        OutputWriter output = new ResultFileWriter(new MoeaTest(), file, false);
        for (NondominatedPopulation res : result) {
//            printSet(res, i);
//            i++;
            output.append(new ResultEntry(res, new Properties()));
        }
        output.close();
    }

    void printSet(NondominatedPopulation result, int n) throws IOException {
        File file = new File("D:\\Code\\1110test_best_moea\\NSGA3_referenceSet_seed_" + n + ".set");
        file.createNewFile();
        OutputWriter output = new ResultFileWriter(new MoeaTest(), file, false);
        output.append(new ResultEntry(result, new Properties()));
        output.close();
    }

    @Test
    void example() {
        String problem = "UF1";
        String[] algorithms = { "NSGAII", "GDE3", "eMOEA" };

        //setup the experiment
        Executor executor = new Executor()
                .withProblem(problem)
                .withMaxEvaluations(10000);

        Analyzer analyzer = new Analyzer()
                .withProblem(problem)
                .includeHypervolume()
                .showStatisticalSignificance();

        //run each algorithm for 50 seeds
        for (String algorithm : algorithms) {
            analyzer.addAll(algorithm,
                    executor.withAlgorithm(algorithm).runSeeds(50));
        }

        //print the results
        analyzer.printAnalysis();
    }

    @Test
    void plotFunction() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "西葫芦炒蛋", "蒜泥秋葵"};
        List<Recipe> list = recipeService.getRecipeList(names);
        Recipe max = recipeService.countRecipeList(list, new int[]{200, 200, 200, 200});
        MoeaTest.setMax(max);
        MoeaTest.setList(list);

        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII")
                .withProblem("ZDT1")
                .withMaxEvaluations(10000)
                .run();

        new Plot().add("NSGAIII", result).show();
    }

}
