package com.example.test_ga_1017.moea;

import org.moeaframework.algorithm.IBEA;
import org.moeaframework.core.*;
import org.moeaframework.core.fitness.AdditiveEpsilonIndicatorFitnessEvaluator;
import org.moeaframework.core.fitness.HypervolumeFitnessEvaluator;
import org.moeaframework.core.fitness.IndicatorFitnessEvaluator;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.spi.AlgorithmProvider;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.core.spi.ProviderLookupException;
import org.moeaframework.core.spi.ProviderNotFoundException;
import org.moeaframework.util.TypedProperties;

import java.util.Properties;

public class IBEA2Provider extends AlgorithmProvider {

    @Override
    public Algorithm getAlgorithm(String s, Properties properties, Problem problem) {
        if (s.equals("IBEA2")) {
            TypedProperties typedProperties = new TypedProperties(properties);
            if (problem.getNumberOfConstraints() > 0) {
                throw new ProviderNotFoundException("IBEA", new ProviderLookupException("constraints not supported"));
            } else {
                int populationSize = (int)typedProperties.getDouble("populationSize", 100.0D);
                String indicator = typedProperties.getString("indicator", "hypervolume");
                IndicatorFitnessEvaluator2 fitnessEvaluator = null;
                Initialization initialization = new RandomInitialization(problem, populationSize);
                Variation variation = OperatorFactory.getInstance().getVariation((String)null, properties, problem);
                if ("hypervolume".equals(indicator)) {
                    //fitnessEvaluator = new HypervolumeFitnessEvaluator(problem);
                } else {
                    if (!"epsilon".equals(indicator)) {
                        throw new IllegalArgumentException("invalid indicator: " + indicator);
                    }

                    fitnessEvaluator = new AdditiveEpsilonIndicatorFitnessEvaluator2(problem);
                }
                return new IBEA2(problem, (NondominatedPopulation)null, initialization, variation, (IndicatorFitnessEvaluator2)fitnessEvaluator);
            }
        } else {
            return null;
        }
    }
}
