package evolution.modifiers.general;

import evolution.EvolutionConstants;
import evolution.individual.AbstractIndividual;
import evolution.modifiers.ISelector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Selects Individuals according to fitness.
 * @author Andi
 */
public class GeneralSelector implements ISelector<AbstractIndividual> {
    private static final Logger logger = Logger.getLogger(GeneralSelector.class.getName());
    @Override
    public ArrayList<AbstractIndividual> selection(ArrayList<AbstractIndividual> individuals, int amount) {

        sortIndividuals(individuals);
        List<AbstractIndividual> toBeRemoved = new ArrayList<>();
        for (AbstractIndividual e: individuals) {
            logger.log(Level.INFO, "{0}", e.getFitness());
            if (e.getFitness() < EvolutionConstants.DELETE_THRESHOLD) {
                logger.log(Level.WARNING, "Individual: {0} below threshold.", e.getId());
                toBeRemoved.add(e);
            }
        }
        individuals.removeAll(toBeRemoved);
        ArrayList<AbstractIndividual> newPop = new ArrayList<>();
        if (amount > individuals.size()) {
            amount = individuals.size(); // if too many are removed
        }
        for (int i = 0; i < amount; ++i) {
            newPop.add(individuals.get(i));
        }
        return newPop;

    }

    private void sortIndividuals(List<AbstractIndividual> individuals) {
        Collections.sort(individuals, new Comparator<AbstractIndividual>() {
            @Override
            public int compare(AbstractIndividual o1, AbstractIndividual o2) {
                return Float.compare(o2.getFitness(), o1.getFitness());
            }
        });
    }
}
