package de.unibi.evolution.modifiers;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import java.util.List;

/**
 * Mutates an individual
 *
 * @author Andi
 */
public interface IMutator<T extends AbstractIndividual> {

    public List<T> mutate(List<T> individuals, EvolutionConfig config);

    public String getInfo();
}
