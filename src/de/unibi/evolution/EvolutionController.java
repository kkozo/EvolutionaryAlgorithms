package de.unibi.evolution;

import de.unibi.util.Mutations;
import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.util.Assets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MainController for the evolution. Handles timings and evaluations for each
 * individual.
 *
 * @author Andi
 */
public class EvolutionController implements Control {

    private static final Logger logger = Logger.getLogger(EvolutionController.class.getName());
    private Population<AbstractIndividual> population;
    private BulletAppState bulletAppState;
    private int currentIndividualNr;
    private BitmapText currentIndividualGuiNode;
    public static boolean STARTED = false;
    public static float CURRENT_SPEED = 0f;
    public static boolean CAM_ENABLED = false;
    private String extension = ".j3o";
    private Geometry targetMarker;
    private Geometry targetMarkerNormal;
    private int numTargetsSet;
    private boolean raiseTerrain;
    private boolean lowerTerrain;
    private boolean deformComplete;
    private TerrainQuad terrain;
    private Camera cam;
    private AppSettings settings;
    private Vector3f currentTarget;
    private InputManager inputManager;
    private PrintWriter statsWriter;

    public EvolutionController(Node rootNode, BulletAppState bulletAppState, BitmapText bitmapText, Population pop, Camera cam, InputManager input, AppSettings settings) {
        this.bulletAppState = bulletAppState;
        this.cam = cam;
        this.population = pop;
        this.currentIndividualNr = -1;
        this.settings = settings;
        population.getConfig().getFitnessFunction().setSpatial(rootNode);
        population.getConfig().getFitnessFunction().setBullet(bulletAppState);
        currentIndividualGuiNode = bitmapText;
        currentIndividualGuiNode.setText("Please deform the terrain. Then press SPACE and set the targets for the fitness function.");
        /*
         * Setting up template terrain.
         */
        AbstractHeightMap heightmap = new ImageBasedHeightMap(Assets.heightMapImage129.getImage().clone(), 0.5f);;
        if (population.getConfig().getTerrainSize() == 129) {
            heightmap = new ImageBasedHeightMap(Assets.heightMapImage129.getImage().clone(), 0.5f);
        } else if (population.getConfig().getTerrainSize() == 257) {
            heightmap = new ImageBasedHeightMap(Assets.heightMapImage257.getImage().clone(), 0.5f);
        } else if (population.getConfig().getTerrainSize() == 513) {
            heightmap = new ImageBasedHeightMap(Assets.heightMapImage513.getImage().clone(), 0.5f);
        }
        heightmap.load();
        heightmap.smooth(0.9f, 1);

        terrain = new TerrainQuad("my terrain", 65, population.getConfig().getTerrainSize(), heightmap.getHeightMap());
        if (population.getConfig().getColor().equals("MINT")) {
            terrain.setMaterial(Assets.matTerrainMint);
        } else {
            terrain.setMaterial(Assets.matTerrainGreen);
        }
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(1f, 1f, 1f);

        terrain.setName("TERRAIN");
        terrain.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        /*
         * Setting up the markers.
         */
        Sphere sphere = new Sphere(8, 8, 0.5f);
        targetMarker = new Geometry("Marker");
        targetMarker.setLocalScale(5f);
        targetMarker.setMesh(sphere);
        targetMarker.setMaterial(Assets.unshaded);
        targetMarker.setLocalTranslation(Vector3f.ZERO);
        Arrow arrow = new Arrow(new Vector3f(0, 1, 0));
        targetMarkerNormal = new Geometry("MarkerNormal");
        targetMarkerNormal.setMesh(arrow);
        targetMarkerNormal.setMaterial(Assets.unshaded);
        targetMarkerNormal.setLocalTranslation(Vector3f.ZERO.add(0, 1000, 0));

        numTargetsSet = population.getConfig().getFitnessFunction().getCurrentNumTargets();
        /*
         * if the targets have been previously set just start the evolution otherwise start new deformation and target setting
         */
        if (numTargetsSet == population.getConfig().getFitnessFunction().getMaxTargets()) {
            deformComplete = true;
            STARTED = true;
        } else {
            deformComplete = false;
            rootNode.attachChild(targetMarker);
            rootNode.attachChild(targetMarkerNormal);
            rootNode.attachChild(terrain);
        }
        rootNode.addControl(population.getConfig().getFitnessFunction());
        /*
         * Setting up buttons for fitness
         */
        this.inputManager = input;
        inputManager.addListener(actionListener, "wireframe");
        inputManager.addMapping("Raise", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Raise");
        inputManager.addMapping("Lower", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "Lower");
        inputManager.addMapping("start", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "start");
        try {
            statsWriter = new PrintWriter(new FileWriter(new File(population.getConfig().getName() + "_stats.txt")));
            statsWriter.println("gen\t best\t avg");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            /*
             * When the deformation is done targets for the fitness function can be set.
             */
            if (deformComplete) {
                if (name.equals("Raise") && isPressed) {
                    Vector3f intersection = getWorldIntersection();
                    if (intersection != null) {
                        currentTarget = intersection;
                        targetMarker.setLocalTranslation(currentTarget);
                        targetMarkerNormal.setLocalTranslation(currentTarget);
                    }

                }
            } else {
                switch (name) {
                    case "Raise":
                        raiseTerrain = isPressed;
                        break;
                    case "Lower":
                        lowerTerrain = isPressed;
                        break;
                }
            }
            /*
             * When SPACE is pressed deformation of the terrain is done and more targets can be set.
             */
            if (name.equals("start") && isPressed) {
                if (numTargetsSet == population.getConfig().getFitnessFunction().getMaxTargets()) {
                    STARTED = true;
                    population.getConfig().getFitnessFunction().start();
                    CURRENT_SPEED = 1;
                    bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
                } else if (deformComplete) {
                    numTargetsSet++;
                    currentIndividualGuiNode.setText("Target saved. Please select another target. " + numTargetsSet + "/" + population.getConfig().getFitnessFunction().getMaxTargets() + " targets set.");
                    population.getConfig().getFitnessFunction().addTarget(currentTarget);
                } else {
                    deformComplete = true;
                    currentIndividualGuiNode.setText("Please set the targets now. Press SPACE to confirm the target. " + numTargetsSet + "/" + population.getConfig().getFitnessFunction().getMaxTargets() + " targets set.");
                }

            }
        }
    };

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return this;
    }

    @Override
    public void setSpatial(Spatial spatial) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * The update method handles providing new targets or individuals. Starts
     * population evaluation whenever a generation is done.
     *
     * @param tpf
     */
    @Override
    public void update(float tpf) {

        if (numTargetsSet == population.getConfig().getFitnessFunction().getMaxTargets()) { // targets are set
            currentIndividualGuiNode.setText(getInfoText()); // show info text
            if (population.getConfig().getFitnessFunction().isDone()) { // the fitness function is done evaluating so the next one is provided.
                currentIndividualNr++;
                if (currentIndividualNr >= population.getConfig().getPopulationSize()) { // the entire generation has been evaluated.
                    evaluatePopulation();
                } else {
                    population.getConfig().getFitnessFunction().evaluate(population.getIndividuals().get(currentIndividualNr), population.getConfig());
                }
            }

        } else {
            if (!deformComplete) { // if targets are not set try to deform the terrain.
                deform(tpf);
            }
        }
    }

    /**
     * Raises or lowers the terrain target point.
     *
     * @param tpf
     */
    private void deform(float tpf) {
        Vector3f intersection = getWorldIntersection();
        if (intersection != null) {
            if (raiseTerrain) {
                Mutations.adjustHeight(intersection, population.getConfig().getTerrainSize() / 8, tpf * 60, terrain);
            } else if (lowerTerrain) {
                Mutations.adjustHeight(intersection, population.getConfig().getTerrainSize() / 8, -tpf * 60, terrain);
            }
        }
    }

    /**
     * Evaluates the entire population.
     */
    private void evaluatePopulation() {
        float bestIndividual = 0f;
        int bestNr = -1;
        float avg = 0;
        for (AbstractIndividual e : population.getIndividuals()) {
            if (e.getFitness() > bestIndividual) {
                bestIndividual = e.getFitness();
                bestNr = e.getId();
            }
            avg += e.getFitness();

        }
        avg = avg / (float) population.getIndividuals().size();
        statsWriter.println(population.getGeneration() + "\t " + bestIndividual + "\t " + avg);
        statsWriter.flush();
        // Selection and recombination
        population.setIndividuals(population.getConfig().getSelector().selection(population.getIndividuals(), population.getConfig()));
        population.getIndividuals().addAll(population.getConfig().getRecombiner().recombine(population.getIndividuals(), population.getConfig()));
        // then fill up rest of the population size with existing individuals.
        fillUpWithExisting();
        logger.log(Level.INFO, "Best Indivudal has been Nr: {0} with Fitness: {1}", new Object[]{bestNr, bestIndividual});
        EvaluationLogger.BEST_FITNESS = bestIndividual;
        EvaluationLogger.BEST_INDIVIDUAL = bestNr;
        population.setIndividuals(population.getConfig().getMutator().mutate(population.getIndividuals(), population.getConfig()));

        EvaluationLogger.flushLog();
        population.setGeneration(population.getGeneration() + 1);

        currentIndividualNr = -1;
    }

    /**
     * Fills the Individuals with new Creatures. These creatures are randomly
     * generated.
     */
    private void fillUpToSize() {
        while (population.getIndividuals().size() < population.getConfig().getPopulationSize()) {
            population.getIndividuals().add(population.getConfig().getIndividualType().createRandomIndividual(population.getConfig()));
            logger.info("Fill up +1");
        }
    }

    /**
     * Fills up the Individual list with several existing ones that are provided
     * in the current list.
     *
     */
    private void fillUpWithExisting() {
        if (population.getIndividuals().size() > 0) {
            while (population.getIndividuals().size() < population.getConfig().getPopulationSize()) {
                int index = FastMath.nextRandomInt(0, population.getIndividuals().size() - 1);
                AbstractIndividual indiv = population.getIndividuals().get(index).clone();
                population.getIndividuals().add(indiv);
            }
        } else {
            fillUpToSize();
        }
    }

    /**
     * Intersects the terrain and searches for a contact point.
     *
     * @return
     */
    private Vector3f getWorldIntersection() {
        Vector3f origin = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2), 0.0f);
        Vector3f direction = cam.getWorldCoordinates(new Vector2f(settings.getWidth() / 2, settings.getHeight() / 2), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        int numCollisions = terrain.collideWith(ray, results);
        if (numCollisions > 0) {
            CollisionResult hit = results.getClosestCollision();
            return hit.getContactPoint();
        }
        return null;
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
    }

    @Override
    public void read(JmeImporter im) throws IOException {
    }

    /**
     * Saves the current run of evolution.
     */
    public void save() {
        int nr = 0;
        String filename = population.getConfig().getName();
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

    /**
     * Simple method that generates an info text to display on the screen.
     *
     * @return
     *
     */
    public String getInfoText() {
        StringBuilder builder = new StringBuilder();
        if (STARTED) {
            builder.append("Current Individual: ").
                    append(currentIndividualNr).
                    append(" Current Generation:").
                    append(population.getGeneration()).
                    append(". Current time: ").
                    append(String.format("%.2f", population.getConfig().getFitnessFunction().getCurrentTime())).
                    append(" / ").
                    append(population.getConfig().getEvalTime()).
                    append(" At speed: ").
                    append(String.format("%.2f", CURRENT_SPEED));
        } else {
            builder.append("EVOLUTION HALTED. PRESS SPACE TO START");
        }
        return builder.toString();
    }
}
