package com.example.test_ga_1017;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.test_ga_1017.mapper")
public class TestGa1017Application {

    public static void main(String[] args){
        SpringApplication.run(TestGa1017Application.class, args);
    }

}
