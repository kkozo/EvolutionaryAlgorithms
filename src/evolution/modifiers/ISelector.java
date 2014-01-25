/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.modifiers;

import evolution.individual.AbstractIndividual;
import java.util.ArrayList;

/**
 *
 * @author Andi
 */
public interface ISelector<T extends AbstractIndividual> {
    public ArrayList<T> selection(ArrayList<T> individuals, int amount);
}
