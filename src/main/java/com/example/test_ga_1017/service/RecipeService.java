package com.example.test_ga_1017.service;

import com.example.test_ga_1017.dto.Recipe;
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

    @Override
    public List<Recipe> getRecipeList(String[] names) {
        List<Recipe> list = new ArrayList<>(4);
        list.add(recipeMapper.selectRecipeByName(names[0]));
        list.add(recipeMapper.selectRecipeByName(names[1]));
        list.add(recipeMapper.selectRecipeByName(names[2]));
        list.add(recipeMapper.selectRecipeByName(names[3]));
        return list;
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
}
