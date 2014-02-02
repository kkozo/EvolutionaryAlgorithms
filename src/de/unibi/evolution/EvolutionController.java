package de.unibi.evolution;

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
import com.jme3.math.ColorRGBA;
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
import java.io.IOException;
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



    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
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
                if (name.equals("Raise")) {

                    raiseTerrain = isPressed;

                } else if (name.equals("Lower")) {

                    lowerTerrain = isPressed;

                }
            }
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

    @Override
    public void update(float tpf) {

        if (numTargetsSet == population.getConfig().getFitnessFunction().getMaxTargets()) {
            currentIndividualGuiNode.setText(getInfoText());
            if (population.getConfig().getFitnessFunction().isDone()) {
                currentIndividualNr++;
                if (currentIndividualNr >= population.getConfig().getPopulationSize()) {
                    evaluatePopulation();
                } else {
                    population.getConfig().getFitnessFunction().evaluate(population.getIndividuals().get(currentIndividualNr), population.getConfig());
                }
            }

        } else {
            if (!deformComplete) {
                deform(tpf);
            }
        }
    }

    public void deform(float tpf) {
        Vector3f intersection = getWorldIntersection();
        if (raiseTerrain) {
            if (intersection != null) {
                adjustHeight(intersection, population.getConfig().getTerrainSize() / 8, tpf * 60);
            }
        } else if (lowerTerrain) {
            if (intersection != null) {
                adjustHeight(intersection, population.getConfig().getTerrainSize() / 8, -tpf * 60);
            }
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
        population.setIndividuals(population.getConfig().getSelector().selection(population.getIndividuals(), population.getConfig()));
        population.getIndividuals().addAll(population.getConfig().getRecombiner().recombine(population.getIndividuals(), population.getConfig()));
        fillUpWithExisting();
        logger.log(Level.INFO, "Best Indivudal has been Nr: {0} with Fitness: {1}", new Object[]{bestNr, bestIndividual});
        EvaluationLogger.BEST_FITNESS = bestIndividual;
        EvaluationLogger.BEST_INDIVIDUAL = bestNr;
        for (int i = 0; i < population.getConfig().getPopulationSize(); ++i) {
            logger.log(Level.INFO, "MUTATING: {0}", population.getIndividuals().get(i).getId());
            Mutations.mutateIndividual(population.getIndividuals().get(i), population.getConfig());
        }

        EvaluationLogger.flushLog();
        population.setGeneration(population.getGeneration() + 1);

        currentIndividualNr = -1;
    }

    /**
     * Fills the Individuals with new Creatures
     */
    private void fillUpToSize() {
        while (population.getIndividuals().size() < population.getConfig().getPopulationSize()) {
            population.getIndividuals().add(population.getConfig().getIndividualType().createRandomIndividual(population.getConfig()));
            logger.info("Fill up +1");
        }
    }

    /**
     * Fills up the Individual List with several existing ones
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

    private void adjustHeight(Vector3f loc, float radius, float height) {

        int radiusStepsX = (int) (radius / terrain.getLocalScale().x);
        int radiusStepsZ = (int) (radius / terrain.getLocalScale().z);

        float xStepAmount = terrain.getLocalScale().x;
        float zStepAmount = terrain.getLocalScale().z;
        long start = System.currentTimeMillis();
        List<Vector2f> locs = new ArrayList<>();
        List<Float> heights = new ArrayList<>();

        for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
            for (int x = -radiusStepsX; x < radiusStepsX; x++) {

                float locX = loc.x + (x * xStepAmount);
                float locZ = loc.z + (z * zStepAmount);

                if (isInRadius(locX - loc.x, locZ - loc.z, radius)) {
                    float h = calculateHeight(radius, height, locX - loc.x, locZ - loc.z);
                    locs.add(new Vector2f(locX, locZ));
                    heights.add(h);
                }
            }
        }
        terrain.adjustHeight(locs, heights);
        terrain.updateModelBound();
    }

    private boolean isInRadius(float x, float y, float radius) {
        Vector2f point = new Vector2f(x, y);
        return point.length() <= radius;
    }

    private float calculateHeight(float radius, float heightFactor, float x, float z) {
        Vector2f point = new Vector2f(x, z);
        float val = point.length() / radius;
        val = 1 - val;
        if (val <= 0) {
            val = 0;
        }
        return heightFactor * val;
    }

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

    public String getInfoText() {
        StringBuilder builder = new StringBuilder();
        if (STARTED){
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
