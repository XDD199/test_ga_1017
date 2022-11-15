package com.example.test_ga_1017.moea;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.ga.MoeaTest;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.service.RecipeService;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.LinkedList;
import java.util.List;

import static com.example.test_ga_1017.ga.JeneTest.dynamicNutrientGetter;
import static java.lang.Math.abs;

public class BasicProblem1111 extends AbstractProblem {

    private static List<Recipe> list = null;
    private static Recipe max = null;

    public BasicProblem1111() {
        super(4, 8);
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "西葫芦炒蛋", "蒜泥秋葵"};
        List<Recipe> list = new LinkedList<>();
        list.add(Recipe.useRecipe(names[0]));
        list.add(Recipe.useRecipe(names[1]));
        list.add(Recipe.useRecipe(names[2]));
        list.add(Recipe.useRecipe(names[3]));
        IRecipeService recipeService = new RecipeService();
        Recipe max = null;
        try {
            max = recipeService.countRecipeList(list, new int[]{200, 200, 200, 200});
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.list = list;
        this.max = max;
    }

    public static List<Recipe> getList() {
        return list;
    }

    public static void setList(List<Recipe> list) {
        BasicProblem1111.list = list;
    }

    public static Recipe getMax() {
        return max;
    }

    public static void setMax(Recipe max) {
        BasicProblem1111.max = max;
    }

    @Override
    public void evaluate(Solution solution) {
        int x0 = EncodingUtils.getInt(solution.getVariable(0));
        int x1 = EncodingUtils.getInt(solution.getVariable(1));
        int x2 = EncodingUtils.getInt(solution.getVariable(2));
        int x3 = EncodingUtils.getInt(solution.getVariable(3));

        int[] x = {x0, x1, x2, x3};
        final double[] f = new double[15];
        final double[] fout = new double[8];
        Recipe temp;
        String[] nutrients = new String[]{"Energy", "Protein", "Fat", "Carbohydrate", "Df", "Vita", "Vitb1", "Vitb2", "Vitc", "K", "Na", "Ca", "Mg", "Zn", "Fe"};
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 4; j++) {
                temp = list.get(j);
                try {
                    f[i] = f[i] + x[j] * dynamicNutrientGetter(nutrients[i], temp) / temp.getRawWeight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        f[2] = (f[2] * 9 / f[0]);
        f[3] = (f[3] * 4 / f[0]);

        fout[0] = (abs(f[0] - 1044) + abs(f[0] - 1392) - (1392 - 1044));//energy
        fout[1] = (abs(f[1] - 27) + abs(f[1] - 39) - (39 - 27));//protein
        fout[2] = (abs(f[2] - 0.25) + abs(f[2] - 0.3) - (0.3 - 0.25));//fat
        fout[3] = (abs(f[3] - 0.5) + abs(f[3] - 0.65) - (0.65 - 0.5));//carbohydrate
        fout[4] = (abs(f[14] - 5.76) + abs(f[14] - 21.6) - (21.6 - 5.76));//fe
        fout[5] = (abs(f[11] - 256) + abs(f[11] - 800) - (800 - 256));//ca
        fout[6] = (abs(f[5] - 262.4) + abs(f[5] - 820) - (820 - 262.4)) + (abs(f[13] - 3.68) + abs(f[13] - 10.028) - (10.028 - 3.68));//vita+zn

        fout[7] = (abs(f[4] - 7) + abs(f[4] - 30) - (30 - 7)) + (f[6] > 0.512 ? 0 : 0.512 - f[6]) /
                + (f[7] > 0.512 ? 0 : 0.512 - f[7]) + (abs(f[8] - 32) + abs(f[8] - 480) - (480 - 32))
                + (f[9] > 672 ? 0 : 672 - f[9]) + (abs(f[10] - 512) + abs(f[10] - 832) - (832 - 512))
                + (f[12] > 102.4 ? 0 : 102.4 - f[12]);//df vitb1 vitb2 vitc k na mg

        solution.setObjective(0, fout[0]);
        solution.setObjective(1, fout[1]);
        solution.setObjective(2, fout[2]);
        solution.setObjective(3, fout[3]);
        solution.setObjective(4, fout[4]);
        solution.setObjective(5, fout[5]);
        solution.setObjective(6, fout[6]);
        solution.setObjective(7, fout[7]);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(4, 8);
        solution.setVariable(0, EncodingUtils.newInt(120, 180));
        solution.setVariable(1, EncodingUtils.newInt(70, 130));
        solution.setVariable(2, EncodingUtils.newInt(70, 130));
        solution.setVariable(3, EncodingUtils.newInt(70, 160));
        return solution;
    }
}
