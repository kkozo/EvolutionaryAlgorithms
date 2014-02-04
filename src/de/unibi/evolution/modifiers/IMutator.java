package de.unibi.evolution.modifiers;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import java.util.ArrayList;

/**
 * Mutates an individual
 *
 * @author Andi
 */
public interface IMutator<T extends AbstractIndividual> {

    /**
     * Mutates a list of Individuals.
     * @param individuals
     * @param config
     * @return
     */
    public ArrayList<T> mutate(ArrayList<T> individuals, EvolutionConfig config);

    public String getInfo();
}
