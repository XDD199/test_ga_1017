package com.example.test_ga_1017.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.example.test_ga_1017.dto.Recipe;
import java.util.List;


@Mapper
public interface RecipeMapper {
    @Select({"SELECT * FROM recipe WHERE type = #{type} ORDER BY serialNo"})
    List<Recipe> selectRecipeByType(String type);

    @Select({"SELECT * FROM recipe WHERE name = #{name}"})
    Recipe selectRecipeByName(String name);
}
