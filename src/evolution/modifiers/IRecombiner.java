package evolution.modifiers;

import evolution.individual.AbstractIndividual;
import java.util.List;

/**
 * Recombines Individuals and returns kids.
 * @author Andi
 */
public interface IRecombiner<T extends AbstractIndividual> {

    public List<T> recombine(List<T> individuals, int size);
}
