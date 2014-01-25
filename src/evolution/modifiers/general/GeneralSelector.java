/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.modifiers.general;

import evolution.EvolutionConstants;
import evolution.individual.AbstractIndividual;
import evolution.individual.box.BoxIndividual;
import evolution.modifiers.ISelector;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Andi
 */
public class GeneralSelector implements ISelector<AbstractIndividual> {

    @Override
    public ArrayList<AbstractIndividual> selection(ArrayList<AbstractIndividual> individuals, int amount) {

        sortIndividuals(individuals);
        List<AbstractIndividual> toBeRemoved = new ArrayList<>();
        for (AbstractIndividual e: individuals) {
            System.out.println(e.getFitness());
            if (e.getFitness() < EvolutionConstants.DELETE_THRESHOLD) {
                System.out.println("Individual: " + e.getId() + " below threshold.");
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
