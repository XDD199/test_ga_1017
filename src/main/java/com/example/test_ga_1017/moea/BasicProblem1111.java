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
        super(4, 9);
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "西葫芦炒蛋", "蒜泥秋葵"};
        List<Recipe> list = new LinkedList<>();
        list.add(Recipe.useRecipe(names[0]));
        list.add(Recipe.useRecipe(names[1]));
        list.add(Recipe.useRecipe(names[2]));
        list.add(Recipe.useRecipe(names[3]));
        IRecipeService recipeService = new RecipeService();
        Recipe max = null;
        try {
            max = recipeService.countRecipeList(list, new int[]{160, 110, 110, 120});
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
        final double[] fout = new double[9];
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

        fout[0] = (abs(f[0] - 756) + abs(f[0] - 1008) - (1008 - 756)) / 756;//energy
        fout[1] = (abs(f[1] - 18) + abs(f[1] - 26) - (26 - 18)) / 18;//protein
        fout[2] = (abs(f[2] - 0.25) + abs(f[2] - 0.3) - (0.3 - 0.25)) / 0.25;//fat
        fout[3] = (abs(f[3] - 0.5) + abs(f[3] - 0.65) - (0.65 - 0.5)) / 0.5;//carbohydrate
        fout[4] = (abs(f[14] - 4.48) + abs(f[14] - 16.8) - (16.8 - 4.48)) / 4.48;//fe
        fout[5] = (abs(f[11] - 272) + abs(f[11] - 850) - (850 - 272)) / 272;//ca
        fout[6] = (abs(f[5] - 176) + abs(f[5] - 550) - (550 - 176)) / 176
                + (abs(f[13] - 2.56) + abs(f[13] - 6.976) - (6.976 - 2.56)) / 2.56;//vita+zn

        fout[7] = (abs(f[4] - 5.6) + abs(f[4] - 24) - (24 - 5.6)) / 5.6
                + (abs(f[8] - 24) + abs(f[8] - 360) - (360 - 24)) / 24
                + (f[9] > 544 ? 0 : 544 - f[9]) / 544
                + (f[12] > 83.2 ? 0 : 83.2 - f[12]) / 83.2;//df  vitc k  mg

        fout[8] = (f[6] > 0.352 ? 0 : 0.352 - f[6]) / 0.352//vitb1
                + (f[7] > 0.352 ? 0 : 0.352 - f[7]) / 0.352 //vitb2
                + (abs(f[10] - 448) + abs(f[10] - 728) - (728 - 448)) / 448; //na

        double big = Math.pow(10,20);
        solution.setObjective(0, fout[0]);
        solution.setObjective(1, fout[1]);
        solution.setObjective(2, fout[2]);
        solution.setObjective(3, fout[3]);
        solution.setObjective(4, fout[4]);
        solution.setObjective(5, fout[5]);
        solution.setObjective(6, fout[6]);
        solution.setObjective(7, fout[7]);
        solution.setObjective(8, fout[8]);
    }

    /**
     * 9-11岁的菜肴重量、营养素推荐范围
     * 午餐 *0.4
     * 为了计算指标的数量级不要太小，需要把目标函数值放大
     * @return
     */
    @Override
    public Solution newSolution() {
        Solution solution = new Solution(4, 9);
        solution.setVariable(0, EncodingUtils.newInt(100, 160));
        solution.setVariable(1, EncodingUtils.newInt(50, 110));
        solution.setVariable(2, EncodingUtils.newInt(50, 110));
        solution.setVariable(3, EncodingUtils.newInt(60, 120));
        return solution;
    }
}
