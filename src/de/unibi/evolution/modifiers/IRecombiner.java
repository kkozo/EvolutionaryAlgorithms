package de.unibi.evolution.modifiers;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import java.util.List;

/**
 * Recombines Individuals and returns kids.
 *
 * @author Andi
 */
public interface IRecombiner<T extends AbstractIndividual> {

    public List<T> recombine(List<T> individuals, EvolutionConfig config);

    public String getInfo();
}
