package config;

import evolution.individual.AbstractIndividual;
import evolution.individual.box.BoxIndividual;
import evolution.individual.sphere.SphereIndividual;
import evolution.individual.box.modifiers.BoxCreatureRecombiner;
import evolution.individual.box.modifiers.BoxMutator;
import evolution.modifiers.general.GeneralSelector;
import evolution.modifiers.IMutator;
import evolution.modifiers.IRecombiner;
import evolution.modifiers.ISelector;
import evolution.modifiers.general.NonRecombiner;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Implement fully Creates a config.
 *
 * @author Andi
 */
public class ConfigCreator {
    
    private static final Logger logger = Logger.getLogger(ConfigCreator.class.getName());
    private static final Map<String, Class<? extends AbstractIndividual>> individuals = new HashMap<>();
    private static final Map<String, Class<? extends IRecombiner>> recombinerMap = new HashMap<>();
    private static final Map<String, Class<? extends IMutator>> mutatorMap = new HashMap<>();
    private static final Map<String, Class<? extends ISelector>> selectorMap = new HashMap<>();
    
    static {
        BoxIndividual box = new BoxIndividual();
        individuals.put(box.getType(), BoxIndividual.class);
        recombinerMap.put(new BoxCreatureRecombiner().getInfo(), BoxCreatureRecombiner.class);
        mutatorMap.put(new BoxMutator().getInfo(), BoxMutator.class);
        selectorMap.put(new GeneralSelector().getInfo(), GeneralSelector.class);
        
        SphereIndividual sphere = new SphereIndividual();
        individuals.put(sphere.getType(), SphereIndividual.class);
        recombinerMap.put(new NonRecombiner().getInfo(), NonRecombiner.class);
        mutatorMap.put(new BoxMutator().getInfo(), BoxMutator.class);
        selectorMap.put(new GeneralSelector().getInfo(), GeneralSelector.class);
    }
    
    public static EvolutionConfig createConfig(String individual, String selector, String mutator, String recombiner) {
        EvolutionConfig config = new EvolutionConfig();
        config.setSelector(getSelector(selector));
        config.setMutator(getMutator(mutator));
        config.setRecombiner(getRecombiner(recombiner));
        config.setIndividualType(getIndividual(individual));
        return config;
    }
    
    public static ISelector getSelector(String name) {
        if (selectorMap.containsKey(name)) {
            try {
                Class<?> type = selectorMap.get(name);
                Constructor<?> constr = type.getConstructor();
                Object o = constr.newInstance();
                return (ISelector) o;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        } else {
            logger.log(Level.SEVERE, "{0} selector not found.", name);
            return null;
        }
    }
    
    public static IMutator getMutator(String name) {
        if (mutatorMap.containsKey(name)) {
            try {
                Class<?> type = mutatorMap.get(name);
                Constructor<?> constr = type.getConstructor();
                Object o = constr.newInstance();
                return (IMutator) o;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        } else {
            logger.log(Level.SEVERE, "{0} mutator not found.", name);
            return null;
        }
    }
    
    public static IRecombiner getRecombiner(String name) {
        if (recombinerMap.containsKey(name)) {
            try {
                Class<?> type = recombinerMap.get(name);
                Constructor<?> constr = type.getConstructor();
                Object o = constr.newInstance();
                return (IRecombiner) o;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        } else {
            logger.log(Level.SEVERE, "{0} recombiner not found.", name);
            return null;
        }
    }
    
    public static AbstractIndividual getIndividual(String name) {
        if (individuals.containsKey(name)) {
            try {
                Class<?> type = individuals.get(name);
                Constructor<?> constr = type.getConstructor();
                Object o = constr.newInstance();
                return (AbstractIndividual) o;
            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage());
                return null;
            }
        } else {
            logger.log(Level.SEVERE, "{0} recombiner not found.", name);
            return null;
        }
    }
}
