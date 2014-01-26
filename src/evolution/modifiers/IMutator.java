package evolution.modifiers;

import config.EvolutionConfig;
import evolution.individual.AbstractIndividual;
import java.util.List;

/**
 * Mutates an individual
 *
 * @author Andi
 */
public interface IMutator<T extends AbstractIndividual> {

    public List<T> mutate(List<T> individuals, EvolutionConfig config);

    public String getInfo();
}
