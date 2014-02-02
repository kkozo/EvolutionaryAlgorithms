package de.unibi.evolution.modifiers;

import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.control.Control;
import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.individual.AbstractIndividual;
import java.util.List;

/**
 *
 * @author Andi
 */
public interface IFitness<T extends AbstractIndividual> extends Control {

    /**
     * Evaluates one individual or sets it up
     *
     * @param individual
     * @param config
     * @return
     */
    public void evaluate(T individual, EvolutionConfig config);

    /**
     * Returns the Info necessary for the Config
     *
     * @return
     */
    public String getInfo();

    /**
     * returns if a creature is done
     */
    public boolean isDone();

    /**
     * Adds a new target for the Fitness function
     */
    public void addTarget(Vector3f newTarget);

    /**
     * Controller needs to know how many targets to provide
     */
    public int getMaxTargets();

    /**
     * Controller needs to know how many targets are currently set
     */
    public int getCurrentNumTargets();

    /**
     * Start Vector so it can be displayed on the terrain
     */
    public Vector3f getStart();

    /**
     * returns the number of targets so that they can be displayed on the
     * Terrain
     */
    public List<Vector3f> getTargets();

    public void setBullet(BulletAppState appState);

    public float getCurrentTime();
    
    public void start();
}
