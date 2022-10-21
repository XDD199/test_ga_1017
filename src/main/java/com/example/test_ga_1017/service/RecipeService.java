package com.example.test_ga_1017.service;

import com.example.test_ga_1017.dto.Recipe;
import com.example.test_ga_1017.dto.RecipeFood;
import com.example.test_ga_1017.mapper.FoodMapper;
import com.example.test_ga_1017.mapper.RecipeFoodMapper;
import com.example.test_ga_1017.mapper.RecipeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeService implements IRecipeService {

    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private RecipeFoodMapper recipeFoodMapper;
    @Autowired
    private FoodMapper foodMapper;

    @Override
    public List<Recipe> getRecipeList(String[] names) {
        List<Recipe> list = new ArrayList<>(4);
        list.add(countDarkVegetable(recipeMapper.selectRecipeByName(names[0])));
        list.add(countDarkVegetable(recipeMapper.selectRecipeByName(names[1])));
        list.add(countDarkVegetable(recipeMapper.selectRecipeByName(names[2])));
        list.add(countDarkVegetable(recipeMapper.selectRecipeByName(names[3])));
        return list;
    }

    /**
     * 计算深色蔬菜含量
     */
    private Recipe countDarkVegetable(Recipe recipe) {
        double general = 0;
        double dark = 0;
        List<RecipeFood> rmCombine = recipeFoodMapper.selectByRecipeID(recipe.getID());
        for (RecipeFood rmCombineOne : rmCombine
        ) {
            int color = foodMapper.selectFoodColorByID(rmCombineOne.getFoodID());
            if (color > 0) {
                general += rmCombineOne.getWeight();
                if (color == 12 || color == 13 || color == 14 || color == 15 || color == 16) {
                    dark += rmCombineOne.getWeight();
                }
            }
        }
        recipe.setBoth(general);
        recipe.setDark(dark);
        return recipe;
    }


    @Override
    public Recipe countRecipeList(List<Recipe> list, int[] weight) throws Exception {
        Recipe result = new Recipe();
        Recipe temp;
        int w;
        String[] nutrients = new String[]{"Energy", "Carbohydrate", "Protein", "Fat", "Df", "Vita", "Vitb1", "Vitb2", "Vitc", "K", "Ca", "Na", "Mg", "Zn", "Fe"};
        for (int i = 0; i < list.size(); i++) {
            temp = list.get(i);
            w = weight[i];
            double value;
            result.setDark(result.getDark() + temp.getDark());
            result.setBoth(result.getBoth() + temp.getBoth());
            for (int j = 0; j < 15; j++) {
                value = dynamicNutrientGetter(nutrients[j], result);
                value = value + dynamicNutrientGetter(nutrients[j], temp) / temp.getRawWeight() * w;
                dynamicNutrientSetter(nutrients[j], result, value);
            }
        }
        return result;
    }

    private double dynamicNutrientGetter(String nutritionName, Recipe recipe) throws Exception {
        try {
            Class classZ = Class.forName("com.example.test_ga_1017.dto.Recipe");
            Method method = classZ.getMethod("get" + nutritionName);
            return (double) method.invoke(recipe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void dynamicNutrientSetter(String nutritionName, Recipe recipe, double value) throws Exception {
        try {
            Class classZ = Class.forName("com.example.test_ga_1017.dto.Recipe");
            Method method = classZ.getMethod("set" + nutritionName, double.class);
            method.invoke(recipe, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算胡承康指数
     * @return
     */
    @Override
    public double countHuScore(List<Recipe> recipeList, int[] weightList, int time) throws Exception {
        String[] nutritionName = {"Energy", "Protein", "Carbohydrate", "Fat", "Df", "Vita", "Vitb1", "Vitb2", "Vitc", "Ca", "K", "Na", "Mg", "Zn", "Fe"};
        double[] everyScore = {10, 10, 10, 7.5, 5, 6.25, 5, 5, 5, 7.5, 5, 5, 5, 6.25, 7.5};
        double[] std = {1160, 30, 0.575, 0.275, 10, 328, 0.64, 0.64, 40, 320, 840, 640, 128, 4.6, 7.2};
        if (time == 2) {
            std = new double[]{870, 22.5, 0.575, 0.275, 7.5, 246, 0.48, 0.48, 30, 240, 630, 480, 96, 3.45, 5.4};
        } else if (time == 3) {
            std = new double[]{2030, 52.5, 0.575, 0.275, 17.5, 574, 1.12, 1.12, 70, 560, 1470, 1120, 224, 8.05, 12.6};
        }
        double tempScore = 0;
        double mealHaveEnergy = 0;
        Recipe recipe = countRecipeList(recipeList, weightList);
        double mealHave = 0;
        double standardHave = 0;
        for (int i = 0; i < 15; i++) {
            //对于每一个营养素，计算分数
            mealHave = dynamicNutrientGetter(nutritionName[i], recipe);
            standardHave = std[i];
            if (i == 0) {
                mealHaveEnergy = mealHave;
            } else if (i == 2) {
                mealHave = mealHave * 4 / mealHaveEnergy;
            } else if (i == 3) {
                mealHave = mealHave * 9 / mealHaveEnergy;
            }
            if (standardHave != 0) {
                tempScore += countEveryScore(standardHave, mealHave, everyScore[i]);
            }
        }
        return tempScore;
    }

    /**
     *计算胡承康指数的子函数
     */
    private double countEveryScore(double std, double actual, double total) {
        if (actual <= std) {
            return total * actual / std;
        } else {
            if (std / actual > 0.6) {
                return total * std / actual;
            } else {
                return total * 0.6;
            }
        }
    }

    @Override
    public void bestHuScore(List<Recipe> recipeList) throws Exception {
        double best = 0;
        double worst = 100;
        int[] range = {0,0,0,0,0,0};
        for (int i = 80; i < 161; i++) {
            System.out.println(i);
            for (int j = 80; j < 161; j++) {
                for (int m = 80; m < 161; m++) {
                    for (int n = 80; n < 161; n++) {
                        int[] weight = {i, j, m, n};
                        double temp = countHuScore(recipeList, weight, 1);
//                        if (temp > 75) {
//                            System.out.println("最佳重量组合为：" + weight[0] + "，" + weight[1] + "，" + weight[2] + "，" + weight[3]);
//                            System.out.println(temp + "---------------------------------------------------");
//                        }
                        best = Math.max(best, temp);
                        worst = Math.min(worst, temp);
                        if (temp > 75) {
                            range[0]++;
                        } else if (temp > 73) {
                            range[1]++;
                        } else if (temp > 70) {
                            range[2]++;
                        } else if (temp > 68) {
                            range[3]++;
                        } else if (temp > 65) {
                            range[4]++;
                        } else {
                            range[5]++;
                        }
                    }
                }
            }
        }
        System.out.println("best = " + best + ",worst = " + worst);
        System.out.println(">75有" + range[0] + ", 73-75有" + range[1] + ", 70-73有" + range[2]
                + ", 68-70有" + range[3]
                + ", 65-68有" + range[4]
                + ", <65有" + range[5]);
    }
}
