package evolution;

import evolution.individual.box.BoxIndividual;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.PhysicsJoint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapText;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import evolution.individual.AbstractIndividual;
import evolution.individual.AbstractCreature;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MainController for the evolution. Handles timings and evaluations for each individual.
 * @author Andi
 */
public class EvolutionController implements Control {

    private static final Logger logger = Logger.getLogger(EvolutionController.class.getName());
    private Population<AbstractIndividual> population;
    private float current_time;
    private Node rootNode;
    private BulletAppState bulletAppState;
    private int currentIndividualNr;
    private AbstractIndividual currentIndividual;
    private boolean PAUSE = true;
    private float PAUSE_LENGTH = 5f;
    private BitmapText currentIndividualGuiNode;
    public static boolean STARTED = false;
    public static float CURRENT_SPEED = 0f;
    private Camera cam;
    public static boolean CAM_ENABLED = false;
    private final Vector3f startVector = new Vector3f(0, -15, 0);
    private final Vector3f targetVector = new Vector3f(0.98028874f, -15.296371f, -61.696625f);
    private float maxDistance = 0f;
    private String extension = ".j3o";
    public EvolutionController(Node rootNode, BulletAppState bulletAppState, BitmapText bitmapText, Camera cam, Population pop) {

        current_time = 0f;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
        this.population = pop;
        this.currentIndividualNr = -1;

        currentIndividualGuiNode = bitmapText;
        this.cam = cam;
        maxDistance = targetVector.distance(startVector);
        nextIndividual();
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return this;
    }

    @Override
    public void setSpatial(Spatial spatial) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float tpf) {
        if (STARTED) {
            current_time += tpf * CURRENT_SPEED;
            if (PAUSE) {
                if (current_time >= PAUSE_LENGTH) { // give the individual a short time to resolve itself
                    current_time = 0f;
                    PAUSE = false;
                }
            } else if (current_time >= EvolutionConstants.EVAL_TIME) { // Time ran out
                current_time = 0f;
                PAUSE = true;
                nextIndividual();
            }
        } else {

            Spatial child = rootNode.getChild("creature");
            if (child != null) {
                if (child.getControl(RigidBodyControl.class) != null) {
                    RigidBodyControl control = child.getControl(RigidBodyControl.class);
                    if (!control.isActive()) { // checks if creature is active - important for Sphere which can stop prematurely
                        control.activate();
                    }
                    if (control.getPhysicsLocation().y < -400) {
                        logger.info("Individual fell off");
                        current_time = 0f;
                        PAUSE = true;
                        nextIndividual();

                    }
                }
            }
        }
        if (currentIndividual != null && CAM_ENABLED) {
            cam.lookAt(currentIndividual.getCreature().getRoot().getGeom().getLocalTranslation(), Vector3f.UNIT_Y);
            float p = EvolutionConstants.TERRAIN_MUT_STR;
            int max = EvolutionConstants.MAX_TERRAIN_MUTATIONS;
            EvolutionConstants.MAX_TERRAIN_MUTATIONS = 5;
            EvolutionConstants.TERRAIN_MUT_STR = 0f;
            Mutations.mutateIndividual(currentIndividual);
            EvolutionConstants.TERRAIN_MUT_STR = p;
            EvolutionConstants.MAX_TERRAIN_MUTATIONS = max;
            removeAllObjects();
            AbstractIndividual abs = currentIndividual.clone();
            rootNode.attachChild(abs.getTerrain());
            RigidBodyControl rig = new RigidBodyControl(0f);
            abs.getTerrain().addControl(rig);
            abs.getTerrain().getControl(RigidBodyControl.class).setFriction(1.5f);
            bulletAppState.getPhysicsSpace().add(rig);
        }
    }

    /**
     * Evaluates fitness, deletes Current individual and then adds the new one.
     */
    private void nextIndividual() {

        evaluateFitness();
        removeAllObjects();
        currentIndividualNr++;

        if (currentIndividualNr >= population.getSize()) {
            evaluatePopulation();
        } else {
            currentIndividualGuiNode.setText("Current Individual: " + currentIndividualNr + " Current Generation:" + population.getGeneration());
            currentIndividual = population.getIndividuals().get(currentIndividualNr);
            if (currentIndividual.getTerrain().getControl(RigidBodyControl.class) != null) {
                currentIndividual.getTerrain().removeControl(RigidBodyControl.class);
            }
            rootNode.attachChild(currentIndividual.getTerrain());
            RigidBodyControl rig = new RigidBodyControl(0f);
            currentIndividual.getTerrain().addControl(rig);
            currentIndividual.getTerrain().getControl(RigidBodyControl.class).setFriction(1.5f);
            bulletAppState.getPhysicsSpace().add(rig);

            AbstractCreature creature = currentIndividual.getCreature();
            creature.resetCreature();
            creature.getNode(rootNode, bulletAppState.getPhysicsSpace(), evoalgo.EvoAlgoStart.creatureID, currentIndividual.getTerrain());
        }
    }

    /**
     * TODO: Change to Fitness Function Class Evaluates the Fitness for a given
     * individual
     */
    private void evaluateFitness() {

        if (currentIndividualNr >= 0) {
            float fitness = maxDistance - (currentIndividual.getCreature().getRoot().getGeom().getLocalTranslation().distance(targetVector));
            EvaluationLogger.FITNESS_AVERAGE += fitness;
            currentIndividual.setFitness(fitness);
        }
    }

    /**
     * Evaluates the entire population
     */
    private void evaluatePopulation() {
        float bestIndividual = 0f;
        int bestNr = -1;
        for (AbstractIndividual e : population.getIndividuals()) {
            if (e.getFitness() > bestIndividual) {
                bestIndividual = e.getFitness();
                bestNr = e.getId();
            }

        }
        population.setIndividuals(population.getSelector().selection(population.getIndividuals(), EvolutionConstants.SELECTION));
        population.getIndividuals().addAll(population.getRecombiner().recombine(population.getIndividuals(), EvolutionConstants.KIDS));
        fillUpWithExisting();
        logger.log(Level.INFO, "Best Indivudal has been Nr: {0} with Fitness: {1}", new Object[]{bestNr, bestIndividual});
        EvaluationLogger.BEST_FITNESS = bestIndividual;
        EvaluationLogger.BEST_INDIVIDUAL = bestNr;
        for (int i = 0; i < population.getSize(); ++i) {
            logger.log(Level.INFO, "MUTATING: {0}", population.getIndividuals().get(i).getId());
            /*
             * TODO:
             * fix Cast
             */
            Mutations.mutateIndividual(population.getIndividuals().get(i));
        }

        EvaluationLogger.flushLog();
        population.setGeneration(population.getGeneration() + 1);

        currentIndividualNr = -1;
        PAUSE = false;
        nextIndividual();
    }

    /**
     * TODO: Fills the Individuals with new Creatures - right now BoxIndividuals
     */
    private void fillUpToSize() {
        while (population.getIndividuals().size() < EvolutionConstants.POPULATION_SIZE) {
            population.getIndividuals().add((BoxIndividual) new BoxIndividual().createRandomIndividual());
            logger.info("Fill up +1");
        }
    }

    /**
     * Fills up the Individual List with several existing ones
     */
    private void fillUpWithExisting() {
        while (population.getIndividuals().size() < EvolutionConstants.POPULATION_SIZE) {
            int index = FastMath.nextRandomInt(0, population.getIndividuals().size() - 1);
            AbstractIndividual indiv = population.getIndividuals().get(index).clone();
            population.getIndividuals().add(indiv);
        }
    }

    /**
     * Removes all objects from the scene
     */
    private void removeAllObjects() {

        rootNode.detachAllChildren();
        bulletAppState.getPhysicsSpace().getJointList().clear();

        for (PhysicsRigidBody rig : bulletAppState.getPhysicsSpace().getRigidBodyList()) {
            bulletAppState.getPhysicsSpace().remove(rig);

        }

        for (PhysicsJoint e : bulletAppState.getPhysicsSpace().getJointList()) {
            bulletAppState.getPhysicsSpace().remove(e);
        }
        bulletAppState.getPhysicsSpace().getRigidBodyList().clear();

    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(JmeImporter im) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void save() {
        String filename = "lastSave";
        String extension = ".j3o";

        int nr = 0;
        File check = new File(filename + "_" + nr + extension);
        while (check.exists()) {
            nr++;
            check = new File(filename + "_" + nr + extension);
        }
        logger.log(Level.INFO, "SAVING TO {0}_{1}", new Object[]{filename, nr});
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(filename + "_" + nr + extension);
        try {
            exporter.save(population, file);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error: Failed to save!", ex);
        }
    }

    public void save(String filename) {

        
        int nr = 0;
        File check = new File(filename + "_" + nr + extension);
        while (check.exists()) {
            nr++;
            check = new File(filename + "_" + nr + extension);
        }
        logger.log(Level.INFO, "SAVING TO {0}_{1}", new Object[]{filename, nr});
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(filename + "_" + nr + extension);
        try {
            exporter.save(population, file);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error: Failed to save!", ex);
        }
    }
}
