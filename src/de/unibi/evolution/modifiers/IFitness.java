package de.unibi.evolution.modifiers;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import java.util.List;

/**
 *
 * @author Andi
 */
public interface IFitness<T extends AbstractIndividual> {

    public float evaluate(List<T> individual, EvolutionConfig config);

    public String getInfo();
}
