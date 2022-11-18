package com.example.test_ga_1017.moea;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.dto.RecipeFood;
import com.example.test_ga_1017.ga.MoeaTest;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.service.RecipeService;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.List;

public class FixIBEA {
    private static IRecipeService recipeService = new RecipeService();

    public static void main(String[] args) {
        potObjective();
    }

    public static void potObjective() {
        NondominatedPopulation result = new Executor()
                .withAlgorithm("IBEA")
                //.withProperty("indicator", "epsilon")
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

    public static void printMeal() {
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "西葫芦炒蛋", "蒜泥秋葵"};
        List<Recipe> list = recipeService.getRecipeList(names);
        NondominatedPopulation result = new Executor()
                .withAlgorithm("IBEA")
                .withProperty("indicator", "epsilon")
                .withProblemClass(ChangeProblem1111.class)
                .withMaxEvaluations(10000)
                .run();

        int[] weight = new int[4];
        for (Solution solution : result) {
            weight[0] = EncodingUtils.getInt(solution.getVariable(0));
            weight[1] = EncodingUtils.getInt(solution.getVariable(1));
            weight[2] = EncodingUtils.getInt(solution.getVariable(2));
            weight[3] = EncodingUtils.getInt(solution.getVariable(3));
            Recipe temp = null;
            try {
                temp = recipeService.countRecipeList(list, weight);
            } catch (Exception e) {
                e.printStackTrace();
            }
            double score = 0;
            try {
                score = recipeService.countHuScore(list, weight, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (score > 70) {
                System.out.println(weight[0] + "," + weight[1] + "," + weight[2] + "," + weight[3]);
                System.out.println(temp + "," + score);
            }
        }
    }
}
