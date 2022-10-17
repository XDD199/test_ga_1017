package com.example.test_ga_1017;

import com.example.test_ga_1017.ga.JeneTest;
import com.example.test_ga_1017.service.IRecipeService;
import com.example.test_ga_1017.dto.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
    }

    @Test
    void testJene() throws Exception {
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
            System.out.println(temp);
        }
    }

}
