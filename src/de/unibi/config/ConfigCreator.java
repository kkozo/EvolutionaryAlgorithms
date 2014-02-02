package de.unibi.config;

import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.modifiers.IFitness;
import de.unibi.evolution.modifiers.IMutator;
import de.unibi.evolution.modifiers.IRecombiner;
import de.unibi.evolution.modifiers.ISelector;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

/**
 *
 * @author Andi
 */
public class ConfigCreator {

    private static final Logger logger = Logger.getLogger(ConfigCreator.class.getName());
    private static final Map<String, Class<? extends AbstractIndividual>> individuals = new HashMap<>();
    private static final Map<String, Class<? extends IRecombiner>> recombinerMap = new HashMap<>();
    private static final Map<String, Class<? extends IMutator>> mutatorMap = new HashMap<>();
    private static final Map<String, Class<? extends ISelector>> selectorMap = new HashMap<>();
    private static final Map<String, Class<? extends IFitness>> fitnessMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage("de.unibi"),
                new SubTypesScanner());
        Set<Class<? extends IRecombiner>> recombinerClasses = reflections.getSubTypesOf(IRecombiner.class);
        Set<Class<? extends AbstractIndividual>> abstractIndividualClasses = reflections.getSubTypesOf(AbstractIndividual.class);
        Set<Class<? extends ISelector>> selectorClasses = reflections.getSubTypesOf(ISelector.class);
        Set<Class<? extends IFitness>> fitnessClasses = reflections.getSubTypesOf(IFitness.class);
        Set<Class<? extends IMutator>> mutatorClasses = reflections.getSubTypesOf(IMutator.class);
        try {
            for (Class e : recombinerClasses) {
                Constructor<?> constr;
                constr = e.getConstructor();
                Object o = constr.newInstance();
                recombinerMap.put(((IRecombiner) o).getInfo(), e);
            }
            for (Class e : abstractIndividualClasses) {
                Constructor<?> constr;
                constr = e.getConstructor();
                Object o = constr.newInstance();
                individuals.put(((AbstractIndividual) o).getType(), e);
            }
            for (Class e : selectorClasses) {
                Constructor<?> constr;
                constr = e.getConstructor();
                Object o = constr.newInstance();
                selectorMap.put(((ISelector) o).getInfo(), e);
            }
            for (Class e : fitnessClasses) {
                Constructor<?> constr;
                constr = e.getConstructor();
                Object o = constr.newInstance();
                fitnessMap.put(((IFitness) o).getInfo(), e);
            }
            for (Class e : mutatorClasses) {
                Constructor<?> constr;
                constr = e.getConstructor();
                Object o = constr.newInstance();
                mutatorMap.put(((IMutator) o).getInfo(), e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<String> getAllCreatures() {
        List<String> strings = new ArrayList<>();
        for (Entry<String, Class<? extends AbstractIndividual>> e : individuals.entrySet()) {
            strings.add(e.getKey());
        }
        return strings;
    }

    public static List<String> getAllMutator() {
        List<String> strings = new ArrayList<>();
        for (Entry<String, Class<? extends IMutator>> e : mutatorMap.entrySet()) {
            strings.add(e.getKey());
        }
        return strings;
    }

    public static List<String> getAllFitness() {
        List<String> strings = new ArrayList<>();
        for (Entry<String, Class<? extends IFitness>> e : fitnessMap.entrySet()) {
            strings.add(e.getKey());
        }
        return strings;
    }

    public static List<String> getAllRecombiner() {
        List<String> strings = new ArrayList<>();
        for (Entry<String, Class<? extends IRecombiner>> e : recombinerMap.entrySet()) {
            strings.add(e.getKey());
        }
        return strings;
    }

    public static List<String> getAllSelector() {
        List<String> strings = new ArrayList<>();
        for (Entry<String, Class<? extends ISelector>> e : selectorMap.entrySet()) {
            strings.add(e.getKey());
        }
        return strings;
    }

    public static EvolutionConfig createConfig(String individual, String selector, String mutator, String recombiner, String fitness) {
        EvolutionConfig config = new EvolutionConfig();
        config.setSelector(getSelector(selector));
        config.setMutator(getMutator(mutator));
        config.setRecombiner(getRecombiner(recombiner));
        config.setIndividualType(getIndividual(individual));
        config.setFitnessFunction(getFitness(fitness));
        logger.log(Level.INFO, "Selector: {0}", config.getSelector().getInfo());
        logger.log(Level.INFO, "Mutator: {0}", config.getMutator().getInfo());
        logger.log(Level.INFO, "Recombiner: {0}", config.getRecombiner().getInfo());
        logger.log(Level.INFO, "Fitness function: {0}", config.getFitnessFunction().getInfo());
        logger.log(Level.INFO, "Individual: {0}", config.getIndividualType().getType());
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

    public static IFitness getFitness(String name) {
        if (fitnessMap.containsKey(name)) {
            try {
                Class<?> type = fitnessMap.get(name);
                Constructor<?> constr = type.getConstructor();
                Object o = constr.newInstance();
                return (IFitness) o;
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
