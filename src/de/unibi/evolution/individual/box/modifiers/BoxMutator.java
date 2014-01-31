package de.unibi.evolution.individual.box.modifiers;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.box.BoxIndividual;
import de.unibi.evolution.modifiers.IMutator;
import java.util.List;

/**
 * TODO: Implement
 *
 * @author Andi
 */
public class BoxMutator implements IMutator<BoxIndividual> {

    @Override
    public List<BoxIndividual> mutate(List<BoxIndividual> individuals, EvolutionConfig config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getInfo() {
        return "BOX";
    }
}
