package evolution.modifiers;

import config.EvolutionConfig;
import evolution.individual.AbstractIndividual;
import java.util.List;

/**
 * Recombines Individuals and returns kids.
 *
 * @author Andi
 */
public interface IRecombiner<T extends AbstractIndividual> {

    public List<T> recombine(List<T> individuals, EvolutionConfig config);

    public String getInfo();
}
