package com.example.test_ga_1017.moea;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.ga.MoeaDayTest;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.service.RecipeService;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.test_ga_1017.ga.JeneTest.dynamicNutrientGetter;
import static java.lang.Math.abs;

public class DayProblem1111 extends AbstractProblem {

    private static List<Recipe> list1 = null;
    private static List<Recipe> list2 = null;
    private static Recipe max = null;

    public DayProblem1111() {
        super(8, 11);
    }

    public static List<Recipe> getList1() {
        return list1;
    }

    public static void setList1(List<Recipe> list1) {
        DayProblem1111.list1 = list1;
    }

    public static List<Recipe> getList2() {
        return list2;
    }

    public static void setList2(List<Recipe> list2) {
        DayProblem1111.list2 = list2;
    }

    public static Recipe getMax() {
        return max;
    }

    public static void setMax(Recipe max) {
        DayProblem1111.max = max;
    }

    @Override
    public void evaluate(Solution solution) {
        int x0 = EncodingUtils.getInt(solution.getVariable(0));
        int x1 = EncodingUtils.getInt(solution.getVariable(1));
        int x2 = EncodingUtils.getInt(solution.getVariable(2));
        int x3 = EncodingUtils.getInt(solution.getVariable(3));

        int y0 = EncodingUtils.getInt(solution.getVariable(4));
        int y1 = EncodingUtils.getInt(solution.getVariable(5));
        int y2 = EncodingUtils.getInt(solution.getVariable(6));
        int y3 = EncodingUtils.getInt(solution.getVariable(7));

        int[] x = {x0, x1, x2, x3, y0, y1, y2, y3};
        double[] f = new double[15];
        double[] fout = new double[11];
        List<Recipe> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        Recipe temp;
        String[] nutrients = new String[]{"Energy", "Protein", "Fat", "Carbohydrate", "Df", "Vita", "Vitb1", "Vitb2", "Vitc", "K", "Na", "Ca", "Mg", "Zn", "Fe"};
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < list.size(); j++) {
                temp = list.get(j);
                try {
                    f[i] = f[i] + x[j] * dynamicNutrientGetter(nutrients[i], temp) / temp.getRawWeight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        double lunch = 0;
        double dinner = 0;
        double dark = 0;
        double both = 0;
        for (int i = 0; i < 8; i++) {
            temp = list.get(i);
            if (i < 4) {
                lunch = lunch + x[i] * temp.getEnergy() / temp.getRawWeight();
            } else {
                dinner = dinner + x[i] * temp.getEnergy() / temp.getRawWeight();
            }
            dark = dark + x[i] * temp.getDark() / temp.getRawWeight();
            both = both + x[i] * temp.getBoth() / temp.getRawWeight();
        }

        f[2] = (f[2] * 9 / f[0]);
        f[3] = (f[3] * 4 / f[0]);

        fout[0] = (abs(f[0] - 1890) + abs(f[0] - 2520) - (2520 - 1890)) / max.getEnergy();//energy
        fout[1] = (abs(f[1] - 45) + abs(f[1] - 65) - (65 - 45)) / max.getProtein();//protein
        fout[2] = (abs(f[2] - 0.25) + abs(f[2] - 0.3) - (0.3 - 0.25)) / 0.4;//fat
        fout[3] = (abs(f[3] - 0.5) + abs(f[3] - 0.65) - (0.65 - 0.5)) / 0.7;//carbohydrate
        fout[4] = (abs(f[14] - 11.2) + abs(f[14] - 42) - (42 - 11.2)) / max.getFe();//fe
        fout[5] = (abs(f[11] - 680) + abs(f[11] - 2125) - (2125 - 680)) / max.getCa();//ca
        fout[6] = (abs(f[5] - 440) + abs(f[5] - 1375) - (1375 - 440)) / max.getVita()
                + (abs(f[13] - 6.4) + abs(f[13] - 17.44) - (17.44 - 6.4)) / max.getZn();//vita+zn

        fout[7] = (abs(f[4] - 14) + abs(f[4] - 60) - (60 - 14)) / max.getDf()
                + (abs(f[8] - 60) + abs(f[8] - 900) - (900 - 60)) / max.getVitc()
                + (f[9] > 1360 ? 0 : 1360 - f[9]) / max.getK()
                + (f[12] > 208 ? 0 : 208 - f[12]) / max.getMg();//df  vitc k mg

        fout[8] = (f[6] > 0.88 ? 0 : 0.88 - f[6]) / max.getVitb1()
                + (f[7] > 0.88 ? 0 : 0.88 - f[7]) / max.getVitb2()
                + (abs(f[10] - 1120) + abs(f[10] - 1820) - (1820 - 1120)) / max.getNa(); //vitb1 vitb2 na

        fout[9] = (abs(lunch / f[0] - 0.4) + abs(lunch / f[0] - 0.6) - (0.6 - 0.4)) / 0.6;
        fout[10] = (dark / both >= 0.5 ? 0 : 0.5 - dark / both) / 0.5;

        solution.setObjective(0, fout[0]);
        solution.setObjective(1, fout[1]);
        solution.setObjective(2, fout[2]);
        solution.setObjective(3, fout[3]);
        solution.setObjective(4, fout[4]);
        solution.setObjective(5, fout[5]);
        solution.setObjective(6, fout[6]);
        solution.setObjective(7, fout[7]);
        solution.setObjective(8, fout[8]);
        solution.setObjective(9, fout[9]);
        solution.setObjective(10, fout[10]);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(8, 11);

        solution.setVariable(0, EncodingUtils.newInt(100, 160));
        solution.setVariable(1, EncodingUtils.newInt(50, 110));
        solution.setVariable(2, EncodingUtils.newInt(50, 110));
        solution.setVariable(3, EncodingUtils.newInt(60, 120));

        solution.setVariable(4, EncodingUtils.newInt(100, 160));
        solution.setVariable(5, EncodingUtils.newInt(50, 110));
        solution.setVariable(6, EncodingUtils.newInt(50, 110));
        solution.setVariable(7, EncodingUtils.newInt(60, 120));

        return solution;
    }

}
