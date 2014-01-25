/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andi
 */
public class ConfigCreator {

    private static final Map<String, Class<? extends AbstractIndividual>> individuals = new HashMap<>();
    private static final Map<String, Class<? extends IRecombiner>> recombiner = new HashMap<>();
    private static final Map<String, Class<? extends IMutator>> mutator = new HashMap<>();
    private static final Map<String, Class<? extends ISelector>> selector = new HashMap<>();

    static {
        BoxIndividual box = new BoxIndividual();
        individuals.put(box.getType(), BoxIndividual.class);
        recombiner.put(box.getType(), BoxCreatureRecombiner.class);
        mutator.put(box.getType(), BoxMutator.class);
        selector.put(box.getType(), GeneralSelector.class);
        
        SphereIndividual sphere = new SphereIndividual();
        individuals.put(sphere.getType(), SphereIndividual.class);
        recombiner.put(sphere.getType(), NonRecombiner.class);
        mutator.put(sphere.getType(), BoxMutator.class);
        selector.put(sphere.getType(), GeneralSelector.class);
    }

    public static EvolutionConfig createConfig() {
        EvolutionConfig config = new EvolutionConfig();
        return config;
    }
}
