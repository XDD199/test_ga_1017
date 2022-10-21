package com.example.test_ga_1017.ga;

import com.example.test_ga_1017.dto.Recipe;
import io.jenetics.*;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Engine;
import io.jenetics.engine.Limits;
import io.jenetics.engine.Problem;
import io.jenetics.ext.SimulatedBinaryCrossover;
import io.jenetics.ext.moea.MOEA;
import io.jenetics.ext.moea.NSGA2Selector;
import io.jenetics.ext.moea.Vec;
import io.jenetics.util.ISeq;
import io.jenetics.util.IntRange;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.abs;

public class JeneTest {
    public static final int VARIABLES = 4;
    public static final int OBJECTIVES = 15;

    private static List<Recipe> list;
    private static Recipe max;

    public static double dynamicNutrientGetter(String nutritionName, Recipe recipe) throws Exception {
        try {
            Class classZ = Class.forName("com.example.test_ga_1017.dto.Recipe");
            Method method = classZ.getMethod("get" + nutritionName);
            return (double) method.invoke(recipe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 适应度函数
     * @param x
     * @return
     */
    static Vec<double[]> f(final int[] x) {
        final double[] f = new double[OBJECTIVES];
        //目标函数是15个的测试
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
        //15-17岁 午餐
        f[2] = abs(f[2] * 9 / f[0] - 0.275) / 0.275;
        f[3] = abs(f[3] * 4 / f[0] - 0.575) / 0.575;

        f[0] = abs(f[0] - 1160) / 1160.0;
        f[1] = abs(f[1] - 30) / 30.0;
        f[4] = abs(f[4] - 10) / 10.0;
        f[5] = abs(f[5] - 328) / 328.0;
        f[6] = abs(f[6] - 0.64) / 0.64;
        f[7] = abs(f[7] - 0.64) / 0.64;
        f[8] = abs(f[8] - 40) / 40;
        f[9] = abs(f[9] - 840) / 840;//k
        f[10] = abs(f[10] - 640) / 640; //na
        f[11] = abs(f[11] - 320) / 320;//ca
        f[12] = abs(f[12] - 128) / 128;//mg
        f[13] = abs(f[13] - 4.6) / 4.6;//zn
        f[14] = abs(f[14] - 7.2) / 7.2;//fe

        return Vec.of(f);
    }

    static Vec<double[]> f2(final int[] x) {
        final double[] f = new double[OBJECTIVES];
        //目标函数是15个的测试
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
        f[0] = (abs(f[0] - 1044) + abs(f[0] - 1392) - (1392 - 1044)) / max.getEnergy();//energy
        f[1] = (abs(f[1] - 27) + abs(f[1] - 39) - (39 - 27)) / max.getProtein();//protein
        f[2] = (abs(f[2] - 0.25) + abs(f[2] - 0.3) - (0.3 - 0.25)) / 0.4;//fat
        f[3] = (abs(f[3] - 0.5) + abs(f[3] - 0.65) - (0.65 - 0.5)) / 0.7;//carbohydrate
        f[4] = (abs(f[4] - 7) + abs(f[4] - 30) - (30 - 7)) / max.getDf();//df
        f[5] = (abs(f[5] - 262.4) + abs(f[5] - 820) - (820 - 262.4)) / max.getVita();//vita
        f[6] = (f[6] > 0.512 ? 0 : 0.512 - f[6]) / max.getVitb1();//vitb1
        f[7] = (f[7] > 0.512 ? 0 : 0.512 - f[7]) / max.getVitb2();//vitb2
        f[8] = (abs(f[8] - 32) + abs(f[8] - 480) - (480 - 32)) / max.getVitc();//vitc
        f[9] = (f[9] > 672 ? 0 : 672 - f[9]) / max.getK();//k
        f[10] = (abs(f[10] - 512) + abs(f[10] - 832) - (832 - 512)) / max.getNa();//na
        f[11] = (abs(f[11] - 256) + abs(f[11] - 800) - (800 - 256)) / max.getCa();//ca
        f[12] = (f[12] > 102.4 ? 0 : 102.4 - f[12]) / max.getMg();//mg
        f[13] = (abs(f[13] - 3.68) + abs(f[13] - 10.028) - (10.028 - 3.68)) / max.getZn();//zn
        f[14] = (abs(f[14] - 5.76) + abs(f[14] - 21.6) - (21.6 - 5.76)) / max.getFe();//fe

        return Vec.of(f);
    }

    static Vec<double[]> f3(final int[] x) {
        final double[] f = new double[OBJECTIVES];
        final double[] fr = new double[8];
        //目标函数是8个的测试
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

        fr[0] = (abs(f[0] - 1044) + abs(f[0] - 1392) - (1392 - 1044)) / max.getEnergy();//energy
        fr[1] = (abs(f[1] - 27) + abs(f[1] - 39) - (39 - 27)) / max.getProtein();//protein
        fr[2] = (abs(f[2] - 0.25) + abs(f[2] - 0.3) - (0.3 - 0.25)) / 0.4;//fat
        fr[3] = (abs(f[3] - 0.5) + abs(f[3] - 0.65) - (0.65 - 0.5)) / 0.7;//carbohydrate
        fr[4] = (abs(f[14] - 5.76) + abs(f[14] - 21.6) - (21.6 - 5.76)) / max.getFe();//fe
        fr[5] = (abs(f[11] - 256) + abs(f[11] - 800) - (800 - 256)) / max.getCa();//ca
        fr[6] = (abs(f[5] - 262.4) + abs(f[5] - 820) - (820 - 262.4)) / max.getVita() + (abs(f[13] - 3.68) + abs(f[13] - 10.028) - (10.028 - 3.68)) / max.getZn();//vita+zn

        fr[7] = (abs(f[4] - 7) + abs(f[4] - 30) - (30 - 7)) / max.getDf() + (f[6] > 0.512 ? 0 : 0.512 - f[6]) / max.getVitb1()
        + (f[7] > 0.512 ? 0 : 0.512 - f[7]) / max.getVitb2() + (abs(f[8] - 32) + abs(f[8] - 480) - (480 - 32)) / max.getVitc()
        + (f[9] > 672 ? 0 : 672 - f[9]) / max.getK() + (abs(f[10] - 512) + abs(f[10] - 832) - (832 - 512)) / max.getNa()
        + (f[12] > 102.4 ? 0 : 102.4 - f[12]) / max.getMg();//df vitb1 vitb2 vitc k na mg

        return Vec.of(fr);
    }

    /**
     * Problem<T, G, C> 描述将使用GA解决的问题
     * T 适应度函数的(原生)参数类型
     * G 进化引擎所使用的基因类型
     * C 适应度函数的结果类型
     */
//    static final Problem<int[], IntegerGene, Vec<double[]>>
//            PROBLEM = Problem.of(
//            JeneTest :: f,  //Native fitness function
//            Codecs.ofVector(IntRange.of(80, 160), VARIABLES)  //Problem encoding
//    );
    //每个自变量的范围不同
    static final Problem<int[], IntegerGene, Vec<double[]>>
            PROBLEM = Problem.of(
            JeneTest :: f,  //Native fitness function
            Codecs.ofVector(IntRange.of(120, 160), IntRange.of(80, 160), IntRange.of(80, 160), IntRange.of(80, 160))  //Problem encoding
    );

    static final Problem<int[], IntegerGene, Vec<double[]>>
            PROBLEM2 = Problem.of(
            JeneTest :: f2,  //Native fitness function
            Codecs.ofVector(IntRange.of(120, 160), IntRange.of(80, 160), IntRange.of(80, 160), IntRange.of(80, 160))  //Problem encoding
    );

    static final Problem<int[], IntegerGene, Vec<double[]>>
            PROBLEM3 = Problem.of(
            JeneTest :: f3,  //Native fitness function
            Codecs.ofVector(IntRange.of(120, 160), IntRange.of(80, 160), IntRange.of(80, 160), IntRange.of(80, 160))  //Problem encoding
    );

    /**
     * Engine<G, C> 引擎配置
     *G - the gene type
     *C - the fitness result type
     */
    //参数设置：种群数量、交叉概率、交叉方法、突变概率、突变方法、子代选择、幸存者选择
    static final Engine<IntegerGene, Vec<double[]>> ENGINE =
            Engine.builder(PROBLEM)
                    .populationSize(100)
                    .alterers( //用来改变后代种群
                            new SimulatedBinaryCrossover<>(1), //交叉
                            new Mutator<>(1.0 / VARIABLES)) //突变概率
                    .offspringFraction(0.8)
                    .offspringSelector(new TournamentSelector<>()) //子代选择器
                    .survivorsSelector(NSGA2Selector.ofVec()) //幸存者选择器
                    .minimizing()
                    .build();

    static final Engine<IntegerGene, Vec<double[]>> ENGINE2 =
            Engine.builder(PROBLEM2)
                    .populationSize(100)
                    .alterers( //用来改变后代种群
                            new SimulatedBinaryCrossover<>(1), //交叉
                            new Mutator<>(1.0 / VARIABLES)) //突变概率
                    .offspringFraction(0.8)
                    .offspringSelector(new TournamentSelector<>()) //子代选择器
                    .survivorsSelector(NSGA2Selector.ofVec()) //幸存者选择器
                    .minimizing()
                    .build();

    static final Engine<IntegerGene, Vec<double[]>> ENGINE3 =
            Engine.builder(PROBLEM3)
                    .populationSize(100)
                    .alterers( //用来改变后代种群
                            new SimulatedBinaryCrossover<>(1), //交叉
                            new Mutator<>(1.0 / 8)) //突变概率
                    .offspringFraction(0.8)
                    .offspringSelector(new TournamentSelector<>()) //子代选择器
                    .survivorsSelector(NSGA2Selector.ofVec()) //幸存者选择器
                    .minimizing()
                    .build();

    //参数设置：迭代次数、收集数量
    public static void main(final String[] args) {
        //ISeq 不可变的有序序列
        //MOEA.toParetoSet(size) 返回解集合
        //G - the gene type
        //T - the array type, e.g. double[]
        //V - the multi object result type vector
        final ISeq<Phenotype<IntegerGene, Vec<double[]>>> front = ENGINE.stream()
                .limit(Limits.byExecutionTime(Duration.ofMillis(500)))
                .limit(2500)
                .collect(MOEA.toParetoSet(IntRange.of(5, 10)));
        System.out.printf(front.toString());
    }

    public static int[][] JeneTestMain(List<Recipe> l) {
        list = l;
        final ISeq<Phenotype<IntegerGene, Vec<double[]>>> front = ENGINE.stream()
                .limit(Limits.byExecutionTime(Duration.ofMillis(500)))
                .limit(2500)
                .collect(MOEA.toParetoSet(IntRange.of(10, 15)));
        Iterator iterator = front.iterator();
        int length = front.length();
        int[][] result = new int[length][4];
        int i = 0;
        while (iterator.hasNext()) {
            Phenotype<IntegerGene, Vec<double[]>> temp = (Phenotype<IntegerGene, Vec<double[]>>) iterator.next();
            Genotype<IntegerGene> genotype = temp.getGenotype();
            for (int k = 0; k < 4; k++) {
                result[i][k] = genotype.get(k, 0).getAllele();
            }
            System.out.println(temp);
            i++;
        }
        return result;
    }

    public static int[][] JeneTestMain2(List<Recipe> l, Recipe m) {
        list = l;
        max = m;
        final ISeq<Phenotype<IntegerGene, Vec<double[]>>> front = ENGINE2.stream()
                .limit(Limits.byExecutionTime(Duration.ofMillis(500)))
                .limit(2500)
                .collect(MOEA.toParetoSet(IntRange.of(10, 15)));
        Iterator iterator = front.iterator();
        int length = front.length();
        int[][] result = new int[length][4];
        int i = 0;
        while (iterator.hasNext()) {
            Phenotype<IntegerGene, Vec<double[]>> temp = (Phenotype<IntegerGene, Vec<double[]>>) iterator.next();
            Genotype<IntegerGene> genotype = temp.getGenotype();
            for (int k = 0; k < 4; k++) {
                result[i][k] = genotype.get(k, 0).getAllele();
            }
            System.out.println(temp);
            i++;
        }
        return result;
    }

    public static int[][] JeneTestMain3(List<Recipe> l, Recipe m) {
        list = l;
        max = m;
        final ISeq<Phenotype<IntegerGene, Vec<double[]>>> front = ENGINE3.stream()
                .limit(Limits.byExecutionTime(Duration.ofMillis(500)))
                .limit(2500)
                .collect(MOEA.toParetoSet(IntRange.of(10, 15)));
        Iterator iterator = front.iterator();
        int length = front.length();
        int[][] result = new int[length][4];
        int i = 0;
        while (iterator.hasNext()) {
            Phenotype<IntegerGene, Vec<double[]>> temp = (Phenotype<IntegerGene, Vec<double[]>>) iterator.next();
            Genotype<IntegerGene> genotype = temp.getGenotype();
            for (int k = 0; k < 4; k++) {
                result[i][k] = genotype.get(k, 0).getAllele();
            }
            System.out.println(temp);
            i++;
        }
        return result;
    }
}
