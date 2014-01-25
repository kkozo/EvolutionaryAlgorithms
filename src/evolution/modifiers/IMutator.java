/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.modifiers;

import evolution.individual.AbstractIndividual;
import java.util.List;

/**
 * Mutates an individual
 * @author Andi
 */
public interface IMutator<T extends AbstractIndividual> {
    public List<T> mutate(List<T> individuals);
    
    
}
