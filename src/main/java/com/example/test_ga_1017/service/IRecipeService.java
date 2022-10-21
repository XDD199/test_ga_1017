package com.example.test_ga_1017.service;

import com.example.test_ga_1017.dto.Recipe;

import java.util.List;


public interface IRecipeService {
    List<Recipe> getRecipeList(String[] names);
    Recipe countRecipeList(List<Recipe> list, int[] weight) throws Exception;
    double countHuScore(List<Recipe> recipeList, int[] weightList, int time) throws Exception;
    void bestHuScore(List<Recipe> recipeList) throws Exception;
}
