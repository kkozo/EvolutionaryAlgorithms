/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.unibi.evolution.modifiers.general;

import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.modifiers.IFitness;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Andi
 */
public class TargetFitness implements IFitness<AbstractIndividual> {

    private static final Logger logger = Logger.getLogger(TargetFitness.class.getName());

    @Override
    public float evaluate(List<AbstractIndividual> individual, EvolutionConfig config) {
        logger.info("nothing yet");
        return 5f;
    }

    @Override
    public String getInfo() {
        return "TARGET";
    }
}
