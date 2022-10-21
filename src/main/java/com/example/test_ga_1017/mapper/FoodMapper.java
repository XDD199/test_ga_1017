package com.example.test_ga_1017.mapper;

import com.example.test_ga_1017.dto.Food;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FoodMapper {
    @Select({"SELECT * FROM food WHERE ID = #{id}"})
    Food selectFoodByID(String id);

    @Select({"SELECT color FROM food WHERE ID = #{id}"})
    int selectFoodColorByID(String id);
}
