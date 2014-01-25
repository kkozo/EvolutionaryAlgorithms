/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.modifiers.general;

import evolution.individual.AbstractIndividual;
import evolution.modifiers.IRecombiner;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andi
 */
public class NonRecombiner implements IRecombiner<AbstractIndividual>{

    @Override
    public List<AbstractIndividual> recombine(List<AbstractIndividual> individuals, int size) {
        return new ArrayList<>();
    }
    
}
