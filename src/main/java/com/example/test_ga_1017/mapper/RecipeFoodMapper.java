package com.example.test_ga_1017.mapper;

import com.example.test_ga_1017.dto.RecipeFood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RecipeFoodMapper {
    @Select({"SELECT * FROM recipe_food WHERE recipeID = #{id} "})
    List<RecipeFood> selectByRecipeID(String id);
}
