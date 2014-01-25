/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.modifiers;

import evolution.individual.AbstractIndividual;
import java.util.List;

/**
 *
 * @author Andi
 */
public interface IRecombiner<T extends AbstractIndividual> {

    public List<T> recombine(List<T> individuals, int size);
}
