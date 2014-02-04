package de.unibi.evolution.modifiers.general;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.modifiers.IMutator;
import de.unibi.util.Mutations;
import java.util.ArrayList;

/**
 * Simple mutator that uses premade methods.
 *
 * @author Andi
 */
public class GeneralMutator implements IMutator<AbstractIndividual> {

    @Override
    public ArrayList<AbstractIndividual> mutate(ArrayList<AbstractIndividual> individuals, EvolutionConfig config) {
        for (AbstractIndividual e: individuals) {
            Mutations.mutateIndividual(e, config);
        }
        return individuals;
    }

    @Override
    public String getInfo() {
        return "BOX";
    }
}
