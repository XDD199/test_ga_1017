package com.example.test_ga_1017.moea;

import org.moeaframework.core.*;
import org.moeaframework.core.indicator.Normalizer;

public abstract class IndicatorFitnessEvaluator2 implements FitnessEvaluator {
    private Problem problem;
    private static final double kappa = 0.05D;
    private double maxAbsIndicatorValue;
    private double[][] fitcomp;

    public IndicatorFitnessEvaluator2(Problem problem) {
        this.problem = problem;
    }

    public Problem getProblem() {
        return this.problem;
    }

    public void evaluate(Population population) {
        //Normalizer normalizer = new Normalizer(this.problem, population);
        //Population normalizedPopulation = normalizer.normalize(population);
        this.fitcomp = new double[population.size()][population.size()];
        //this.maxAbsIndicatorValue = -1.0D / 0.0;
        this.maxAbsIndicatorValue = -1000;

        int i;
        for(i = 0; i < population.size(); ++i) {
            for(int j = 0; j < population.size(); ++j) {
                //this.fitcomp[i][j] = this.calculateIndicator(normalizedPopulation.get(i), normalizedPopulation.get(j));
                this.fitcomp[i][j] = this.calculateIndicator(population.get(i), population.get(j));
                if (Math.abs(this.fitcomp[i][j]) > this.maxAbsIndicatorValue) {
                    this.maxAbsIndicatorValue = Math.abs(this.fitcomp[i][j]);
                }
            }
        }

        for(i = 0; i < population.size(); ++i) {
            double sum = 0.0D;

            for(int j = 0; j < population.size(); ++j) {
                if (i != j) {
                    sum += Math.exp(-this.fitcomp[j][i] / this.maxAbsIndicatorValue / 0.05D);
                }
            }

            population.get(i).setAttribute("fitness", sum);
        }

    }

    public void removeAndUpdate(Population population, int removeIndex) {
        if (this.fitcomp == null) {
            throw new FrameworkException("evaluate must be called first");
        } else {
            int i;
            for(i = 0; i < population.size(); ++i) {
                if (i != removeIndex) {
                    Solution solution = population.get(i);
                    double fitness = (Double)solution.getAttribute("fitness");
                    fitness -= Math.exp(-this.fitcomp[removeIndex][i] / this.maxAbsIndicatorValue / 0.05D);
                    solution.setAttribute("fitness", fitness);
                }
            }

            for(i = 0; i < population.size(); ++i) {
                for(int j = removeIndex + 1; j < population.size(); ++j) {
                    this.fitcomp[i][j - 1] = this.fitcomp[i][j];
                }

                if (i > removeIndex) {
                    this.fitcomp[i - 1] = this.fitcomp[i];
                }
            }

            population.remove(removeIndex);
        }
    }

    protected abstract double calculateIndicator(Solution var1, Solution var2);
}
