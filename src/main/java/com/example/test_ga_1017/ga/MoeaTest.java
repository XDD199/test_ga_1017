package com.example.test_ga_1017.ga;

import com.example.test_ga_1017.dto.Recipe;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.problem.AbstractProblem;

import java.util.List;

import static com.example.test_ga_1017.ga.JeneTest.dynamicNutrientGetter;
import static java.lang.Math.abs;

public class MoeaTest extends AbstractProblem {

    private static List<Recipe> list = null;
    private static Recipe max = null;

    public MoeaTest() {
        super(4, 8);
    }

    public static List<Recipe> getList() {
        return list;
    }

    public static void setList(List<Recipe> list) {
        MoeaTest.list = list;
    }

    public static Recipe getMax() {
        return max;
    }

    public static void setMax(Recipe max) {
        MoeaTest.max = max;
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
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(4, 8);
        solution.setVariable(0, EncodingUtils.newInt(140, 200));
        solution.setVariable(1, EncodingUtils.newInt(90, 200));
        solution.setVariable(2, EncodingUtils.newInt(90, 200));
        solution.setVariable(3, EncodingUtils.newInt(90, 200));
        return solution;
    }
}
