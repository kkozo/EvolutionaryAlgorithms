package de.unibi.evolution.modifiers.general;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.modifiers.IRecombiner;
import java.util.ArrayList;
import java.util.List;

/**
 * This class doesn't recombine.
 * @author Andi
 */
public class NonRecombiner implements IRecombiner<AbstractIndividual>{

    @Override
    public List<AbstractIndividual> recombine(List<AbstractIndividual> individuals, EvolutionConfig config) {
        return new ArrayList<>();
    }

    @Override
    public String getInfo() {
        return "NON";
    }
    
    
    
}
