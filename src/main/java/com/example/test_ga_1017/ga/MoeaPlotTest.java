package com.example.test_ga_1017.ga;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.service.RecipeService;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

public class MoeaPlotTest {
    public static void main(String[] args) throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "西葫芦炒蛋", "蒜泥秋葵"};
        List<Recipe> list = new LinkedList<>();
        list.add(Recipe.useRecipe(names[0]));
        list.add(Recipe.useRecipe(names[1]));
        list.add(Recipe.useRecipe(names[2]));
        list.add(Recipe.useRecipe(names[3]));
        IRecipeService recipeService = new RecipeService();
        Recipe max = recipeService.countRecipeList(list, new int[]{200, 200, 200, 200});
        MoeaTest.setMax(max);
        MoeaTest.setList(list);

        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII")
                .withProblemClass(MoeaTest.class)
                .withMaxEvaluations(10000)
                .run();

        new Plot().add("NSGAIII", result, 0, 5).show();
    }
}
