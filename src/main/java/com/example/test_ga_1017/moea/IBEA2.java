package com.example.test_ga_1017.moea;

import org.moeaframework.algorithm.AbstractEvolutionaryAlgorithm;
import org.moeaframework.core.*;
import org.moeaframework.core.comparator.FitnessComparator;
import org.moeaframework.core.fitness.IndicatorFitnessEvaluator;
import org.moeaframework.core.operator.TournamentSelection;

public class IBEA2 extends AbstractEvolutionaryAlgorithm {
    private IndicatorFitnessEvaluator2 fitnessEvaluator;
    private FitnessComparator fitnessComparator;
    private Selection selection;
    private Variation variation;

    public IBEA2(Problem problem, NondominatedPopulation archive, Initialization initialization, Variation variation, IndicatorFitnessEvaluator2 fitnessEvaluator) {
        super(problem, new Population(), archive, initialization);
        this.variation = variation;
        this.fitnessEvaluator = fitnessEvaluator;
        this.fitnessComparator = new FitnessComparator(fitnessEvaluator.areLargerValuesPreferred());
        this.selection = new TournamentSelection(this.fitnessComparator);
    }

    protected void initialize() {
        super.initialize();
        this.fitnessEvaluator.evaluate(this.population);
    }

    protected void iterate() {
        Population offspring = new Population();
        int populationSize = this.population.size();

        while(offspring.size() < populationSize) {
            Solution[] parents = this.selection.select(this.variation.getArity(), this.population);
            Solution[] children = this.variation.evolve(parents);
            offspring.addAll(children);
        }

        this.evaluateAll(offspring);
        this.population.addAll(offspring);
        this.fitnessEvaluator.evaluate(this.population);

        while(this.population.size() > populationSize) {
            int worstIndex = this.findWorstIndex();
            this.fitnessEvaluator.removeAndUpdate(this.population, worstIndex);
        }

    }

    private int findWorstIndex() {
        int worstIndex = 0;

        for(int i = 1; i < this.population.size(); ++i) {
            if (this.fitnessComparator.compare(this.population.get(worstIndex), this.population.get(i)) == -1) {
                worstIndex = i;
            }
        }

        return worstIndex;
    }
}
