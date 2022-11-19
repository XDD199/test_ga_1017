package com.example.test_ga_1017;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.ga.MoeaDayTest;
import com.example.test_ga_1017.ga.MoeaTest;
import com.example.test_ga_1017.moea.BasicProblem1111;
import com.example.test_ga_1017.moea.DayProblem1111;
import com.example.test_ga_1017.moea.IBEA2Provider;
import com.example.test_ga_1017.service.IRecipeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.spi.AlgorithmFactory;
import org.moeaframework.core.variable.EncodingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class IBEA_Test {
    @Autowired
    private IRecipeService recipeService;


    @Test
    void TestMOEAForDay() throws Exception {
        String[] names = new String[]{"燕麦杂粮饭", "豆豉肉丁", "毛豆鸡丁", "清炒苋菜"};
        List<Recipe> list1 = recipeService.getRecipeList(names);
        names = new String[]{"白米饭", "尖椒爆鸭", "青瓜火腿", "蒜泥秋葵"};
        List<Recipe> list2 = recipeService.getRecipeList(names);
        List<Recipe> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        Recipe max = recipeService.countRecipeList(list, new int[]{160, 110, 110, 120, 160, 110, 110, 120});
        DayProblem1111.setMax(max);
        DayProblem1111.setList1(list1);
        DayProblem1111.setList2(list2);


        AlgorithmFactory.getInstance().addProvider(new IBEA2Provider());
        NondominatedPopulation result = new Executor()
                .withAlgorithm("IBEA2")
                .withProperty("indicator", "epsilon")
                .withProblemClass(DayProblem1111.class)
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
                System.out.println("__________________________________________________________");
            }
        }
    }

    @Test
    void TestMOEA_IBEA() throws Exception {

        String[] names = new String[]{"燕麦杂粮饭", "叉烧鸭胸", "青瓜火腿", "清炒苋菜"};
        List<Recipe> list = recipeService.getRecipeList(names);
//        Recipe max = recipeService.countRecipeList(list, new int[]{160, 160, 160, 160});
//        MoeaTest.setMax(max);
        BasicProblem1111.setList(list);
//        System.out.println("___max_______________________________________________________");
//        System.out.println(max);
//        System.out.println("___max_______________________________________________________");

        AlgorithmFactory.getInstance().addProvider(new IBEA2Provider());
        NondominatedPopulation result = new Executor()
                .withAlgorithm("IBEA2")
                .withProperty("indicator", "epsilon")
                .withProblemClass(BasicProblem1111.class)
                .withMaxEvaluations(10000)
                .run();

        int[] weight = new int[4];
        for (Solution solution : result) {
            weight[0] = EncodingUtils.getInt(solution.getVariable(0));
            weight[1] = EncodingUtils.getInt(solution.getVariable(1));
            weight[2] = EncodingUtils.getInt(solution.getVariable(2));
            weight[3] = EncodingUtils.getInt(solution.getVariable(3));
            Recipe temp = recipeService.countRecipeList(list, weight);
            if (recipeService.countHuScore(list, weight, 1) > 70) {
                System.out.println(weight[0] + "," + weight[1] + "," + weight[2] + "," + weight[3]);
                System.out.println(temp + "," + recipeService.countHuScore(list, weight, 1));
            }
        }
    }
}
