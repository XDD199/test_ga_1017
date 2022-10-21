package com.example.test_ga_1017;

import com.example.test_ga_1017.ga.JeneTest;
import com.example.test_ga_1017.ga.MoeaDayTest;
import com.example.test_ga_1017.ga.MoeaTest;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.dto.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class TestGa1017ApplicationTests {

    @Autowired
    private IRecipeService recipeService;

    /**
     * 验证套餐的营养素总和计算正确
     * @throws Exception
     */
    @Test
    void recipeCount() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "竹笋炒牛肉", "枸杞菠菜"};
        List<Recipe> list = recipeService.getRecipeList(names);
        int[] weight = new int[]{130,108,108,116};
        Recipe temp = recipeService.countRecipeList(list, weight);
        System.out.println(temp);
        System.out.println(recipeService.countHuScore(list, weight, 1));
        recipeService.bestHuScore(list);
    }

    @Test
    void testJene() throws Exception {
        //0.923076923  1.11111  1.3888889  1.2844827586
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "竹笋炒牛肉", "枸杞菠菜"};
        List<Recipe> list = recipeService.getRecipeList(names);
        int[][] weights = JeneTest.JeneTestMain(list);
        for (int i = 0; i < weights.length; i++) {
            Recipe temp = recipeService.countRecipeList(list, weights[i]);
            System.out.println("__________________________________________________________");
            System.out.println(temp + "," + recipeService.countHuScore(list, weights[i], 1));
        }
    }

    @Test
    void testJene2() throws Exception {
        //0.923076923  1.11111  1.3888889  1.2844827586
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "竹笋炒牛肉", "枸杞菠菜"};
        List<Recipe> list = recipeService.getRecipeList(names);
        Recipe max = recipeService.countRecipeList(list, new int[]{160, 160, 160, 160});
        System.out.println("___max_______________________________________________________");
        System.out.println(max);
        System.out.println("___max_______________________________________________________");
        int[][] weights = JeneTest.JeneTestMain2(list, max);
        for (int i = 0; i < weights.length; i++) {
            Recipe temp = recipeService.countRecipeList(list, weights[i]);
            System.out.println("__________________________________________________________");
            System.out.println(temp + "," + recipeService.countHuScore(list, weights[i], 1));
        }
    }

    @Test
    void testJene3() throws Exception {
        //0.923076923  1.11111  1.3888889  1.2844827586
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "竹笋炒牛肉", "枸杞菠菜"};
        List<Recipe> list = recipeService.getRecipeList(names);
        Recipe max = recipeService.countRecipeList(list, new int[]{160, 160, 160, 160});
        System.out.println("___max_______________________________________________________");
        System.out.println(max);
        System.out.println("___max_______________________________________________________");
        int[][] weights = JeneTest.JeneTestMain3(list, max);
        for (int i = 0; i < weights.length; i++) {
            Recipe temp = recipeService.countRecipeList(list, weights[i]);
            System.out.println("__________________________________________________________");
            System.out.println(temp + "," + recipeService.countHuScore(list, weights[i], 1));
        }
    }

    @Test
    void TestMOEA() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "清炒苋菜", "蒜泥秋葵", "尖椒爆鸭"};
        List<Recipe> list = recipeService.getRecipeList(names);
        Recipe max = recipeService.countRecipeList(list, new int[]{160, 160, 160, 160});
        MoeaTest.setMax(max);
        MoeaTest.setList(list);
        System.out.println("___max_______________________________________________________");
        System.out.println(max);
        System.out.println("___max_______________________________________________________");

        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII")
                .withProblemClass(MoeaTest.class)
                .withMaxEvaluations(10000)
                .run();

        int[] weight = new int[4];
        for (Solution solution : result) {
            weight[0] = EncodingUtils.getInt(solution.getVariable(0));
            weight[1] = EncodingUtils.getInt(solution.getVariable(1));
            weight[2] = EncodingUtils.getInt(solution.getVariable(2));
            weight[3] = EncodingUtils.getInt(solution.getVariable(3));
            System.out.println(weight[0] + "," + weight[1] + "," + weight[2] + "," + weight[3]);
//            System.out.println("目标函数为：" + solution.getObjective(0) + "," +
//                    solution.getObjective(1) + "," +
//                    solution.getObjective(2) + "," +
//                    solution.getObjective(3) + "," +
//                    solution.getObjective(4) + "," +
//                    solution.getObjective(5) + "," +
//                    solution.getObjective(6) + "," +
//                    solution.getObjective(7));
            Recipe temp = recipeService.countRecipeList(list, weight);
            System.out.println(temp + "," + recipeService.countHuScore(list, weight, 1));
//            System.out.println("__________________________________________________________");
        }
    }

    @Test
    void TestMOEAForDay() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "清炒苋菜", "豆豉肉丁", "毛豆鸡丁"};
        List<Recipe> list1 = recipeService.getRecipeList(names);
        names = new String[]{"燕麦杂粮饭", "蒜泥秋葵", "尖椒爆鸭", "火腿炒蛋"};
        List<Recipe> list2 = recipeService.getRecipeList(names);
        List<Recipe> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        Recipe max = recipeService.countRecipeList(list, new int[]{160, 160, 160, 160, 160, 160, 160, 160});
        MoeaDayTest.setMax(max);
        MoeaDayTest.setList1(list1);
        MoeaDayTest.setList2(list2);
        System.out.println("___max_______________________________________________________");
        System.out.println(max);
        System.out.println("___max_______________________________________________________");

        NondominatedPopulation result = new Executor()
                .withAlgorithm("NSGAII")
                .withProblemClass(MoeaDayTest.class)
                .withMaxEvaluations(10000)
                .run();

        int[] weight1 = new int[4];
        int[] weight2 = new int[4];
        int[] weight;
        for (Solution solution : result) {
            if (!solution.violatesConstraints()) {
                weight1[0] = EncodingUtils.getInt(solution.getVariable(0));
                weight1[1] = EncodingUtils.getInt(solution.getVariable(1));
                weight1[2] = EncodingUtils.getInt(solution.getVariable(2));
                weight1[3] = EncodingUtils.getInt(solution.getVariable(3));
                System.out.println(weight1[0] + "," + weight1[1] + "," + weight1[2] + "," + weight1[3]);
                Recipe temp = recipeService.countRecipeList(list1, weight1);
                System.out.println(temp + "," + recipeService.countHuScore(list1, weight1, 1));

                weight2[0] = EncodingUtils.getInt(solution.getVariable(4));
                weight2[1] = EncodingUtils.getInt(solution.getVariable(5));
                weight2[2] = EncodingUtils.getInt(solution.getVariable(6));
                weight2[3] = EncodingUtils.getInt(solution.getVariable(7));
                System.out.println(weight2[0] + "," + weight2[1] + "," + weight2[2] + "," + weight2[3]);
                temp = recipeService.countRecipeList(list2, weight2);
                System.out.println(temp + "," + recipeService.countHuScore(list2, weight2, 2));

                weight = new int[]{weight1[0], weight1[1], weight1[2], weight1[3], weight2[0], weight2[1], weight2[2], weight2[3]};
                temp = recipeService.countRecipeList(list, weight);
                System.out.println(temp + "," + recipeService.countHuScore(list, weight, 3));
                System.out.println(solution.getConstraint(0));
                System.out.println(solution.getConstraint(1));
                System.out.println("__________________________________________________________");
            }
        }
    }

    @Test
    void TestMOEARandom() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "尖椒爆鸭", "竹笋炒牛肉", "枸杞菠菜"};
        List<Recipe> list = recipeService.getRecipeList(names);
        Recipe max = recipeService.countRecipeList(list, new int[]{160, 160, 160, 160});
        MoeaTest.setMax(max);
        MoeaTest.setList(list);
        System.out.println("___max_______________________________________________________");
        System.out.println(max);
        System.out.println("___max_______________________________________________________");

        NondominatedPopulation result = new Executor()
                .withAlgorithm("Random")
                .withProblemClass(MoeaTest.class)
                .withMaxEvaluations(10000)
                .run();

        int[] weight = new int[4];
        for (Solution solution : result) {
            weight[0] = EncodingUtils.getInt(solution.getVariable(0));
            weight[1] = EncodingUtils.getInt(solution.getVariable(1));
            weight[2] = EncodingUtils.getInt(solution.getVariable(2));
            weight[3] = EncodingUtils.getInt(solution.getVariable(3));
            System.out.println(weight[0] + "," + weight[1] + "," + weight[2] + "," + weight[3]);
//            System.out.println("目标函数为：" + solution.getObjective(0) + "," +
//                    solution.getObjective(1) + "," +
//                    solution.getObjective(2) + "," +
//                    solution.getObjective(3) + "," +
//                    solution.getObjective(4) + "," +
//                    solution.getObjective(5) + "," +
//                    solution.getObjective(6) + "," +
//                    solution.getObjective(7));
            Recipe temp = recipeService.countRecipeList(list, weight);
            System.out.println(temp + "," + recipeService.countHuScore(list, weight, 1));
//            System.out.println("__________________________________________________________");
        }
    }
}
