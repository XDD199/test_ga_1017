package com.example.test_ga_1017.ga;

import com.example.test_ga_1017.dto.Recipe;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.ArrayList;
import java.util.List;

import static com.example.test_ga_1017.ga.JeneTest.dynamicNutrientGetter;
import static java.lang.Math.abs;

public class MoeaDayTest extends AbstractProblem {
    private static List<Recipe> list1 = null;
    private static List<Recipe> list2 = null;
    private static Recipe max = null;

    public MoeaDayTest() {
        super(8, 8, 3);
    }

    public static List<Recipe> getList1() {
        return list1;
    }

    public static void setList1(List<Recipe> list1) {
        MoeaDayTest.list1 = list1;
    }

    public static List<Recipe> getList2() {
        return list2;
    }

    public static void setList2(List<Recipe> list2) {
        MoeaDayTest.list2 = list2;
    }

    public static Recipe getMax() {
        return max;
    }

    public static void setMax(Recipe max) {
        MoeaDayTest.max = max;
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
        double[] fout = new double[8];
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

        fout[0] = (abs(f[0] - 1044) + abs(f[0] - 1392) - (1392 - 1044)) / max.getEnergy();//energy
        fout[1] = (abs(f[1] - 27) + abs(f[1] - 39) - (39 - 27)) / max.getProtein();//protein
        fout[2] = (abs(f[2] - 0.25) + abs(f[2] - 0.3) - (0.3 - 0.25)) / 0.4;//fat
        fout[3] = (abs(f[3] - 0.5) + abs(f[3] - 0.65) - (0.65 - 0.5)) / 0.7;//carbohydrate
        fout[4] = (abs(f[14] - 5.76) + abs(f[14] - 21.6) - (21.6 - 5.76)) / max.getFe();//fe
        fout[5] = (abs(f[11] - 256) + abs(f[11] - 800) - (800 - 256)) / max.getCa();//ca
        fout[6] = (abs(f[5] - 262.4) + abs(f[5] - 820) - (820 - 262.4)) / max.getVita() + (abs(f[13] - 3.68) + abs(f[13] - 10.028) - (10.028 - 3.68)) / max.getZn();//vita+zn

        fout[7] = (abs(f[4] - 7) + abs(f[4] - 30) - (30 - 7)) / max.getDf() + (f[6] > 0.512 ? 0 : 0.512 - f[6]) / max.getVitb1()
                + (f[7] > 0.512 ? 0 : 0.512 - f[7]) / max.getVitb2() + (abs(f[8] - 32) + abs(f[8] - 480) - (480 - 32)) / max.getVitc()
                + (f[9] > 672 ? 0 : 672 - f[9]) / max.getK() + (abs(f[10] - 512) + abs(f[10] - 832) - (832 - 512)) / max.getNa()
                + (f[12] > 102.4 ? 0 : 102.4 - f[12]) / max.getMg();//df vitb1 vitb2 vitc k na mg

        solution.setObjective(0, fout[0]);
        solution.setObjective(1, fout[1]);
        solution.setObjective(2, fout[2]);
        solution.setObjective(3, fout[3]);
        solution.setObjective(4, fout[4]);
        solution.setObjective(5, fout[5]);
        solution.setObjective(6, fout[6]);
        solution.setObjective(7, fout[7]);

        //f0只是两餐的，所以并不是说0.4 0.3，而是4/7 3/7
        lunch = abs(lunch / f[0] - 0.4) + abs(lunch / f[0] - 0.6) - (0.6 - 0.4);
        dinner = abs(dinner / f[0] - 0.3) + abs(dinner / f[0] - 0.5) - (0.5 - 0.3);

        dark = 0.5 - dark / both;
        solution.setConstraint(0, lunch <= 0 ? 0 : lunch);
        solution.setConstraint(1, dinner <= 0 ? 0 :dinner);
        solution.setConstraint(2, dark <= 0 ? 0 : dark);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(8, 8, 3);

        solution.setVariable(0, EncodingUtils.newInt(120, 160));
        solution.setVariable(1, EncodingUtils.newInt(80, 160));
        solution.setVariable(2, EncodingUtils.newInt(80, 160));
        solution.setVariable(3, EncodingUtils.newInt(80, 160));
        solution.setVariable(4, EncodingUtils.newInt(80, 160));
        solution.setVariable(5, EncodingUtils.newInt(80, 160));
        solution.setVariable(6, EncodingUtils.newInt(80, 160));
        solution.setVariable(7, EncodingUtils.newInt(80, 160));

        return solution;
    }
}
