package de.unibi.evolution.modifiers.general;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.PhysicsJoint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import de.unibi.config.EvolutionConfig;
import static de.unibi.evolution.EvolutionController.CURRENT_SPEED;
import de.unibi.evolution.individual.AbstractCreature;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.modifiers.IFitness;
import de.unibi.util.Assets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Andi
 */
public class TargetFitness implements IFitness<AbstractIndividual> {

    private static final Logger logger = Logger.getLogger(TargetFitness.class.getName());
    private AbstractIndividual currentIndividual;
    private float evalTime;
    private ArrayList<Vector3f> targets;
    private Vector3f start;
    private boolean done = true;
    private Node rootNode;
    private float current_time;
    private float PAUSE_LENGTH = 5f;
    private boolean PAUSE;
    private boolean STARTED;
    private BulletAppState bulletAppState;
    private Geometry targetMarker;
    private Geometry targetMarkerNormal;
    private Geometry startMarker;
    private Geometry startMarkerNormal;
    private int currentTargets;

    public TargetFitness() {
        targets = new ArrayList<>();
        current_time = 0;
        PAUSE = true;
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
//        targetMarkerNormal.setLocalTranslation(targetVector);
        startMarker = targetMarker.clone();
//        startMarker.setLocalTranslation(startVector);
        startMarker.getMaterial().setColor("Color", new ColorRGBA(130f / 255f, 255f / 255f, 0f, 0.6f));
        startMarkerNormal = targetMarkerNormal.clone();
//        startMarkerNormal.setLocalTranslation(startVector);
    }

    @Override
    public void evaluate(AbstractIndividual individual, EvolutionConfig config) {
        evalTime = config.getEvalTime();
        currentIndividual = individual;
        done = false;
        removeAllObjects();
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
        creature.getNode(rootNode, bulletAppState.getPhysicsSpace(), de.unibi.evoalgo.EvoAlgoStart.creatureID, currentIndividual.getTerrain(), start);
    }

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
        rootNode.attachChild(targetMarker);
        targetMarker.setLocalTranslation(targets.get(0));
        rootNode.attachChild(targetMarkerNormal);
        targetMarkerNormal.setLocalTranslation(targets.get(0));
        rootNode.attachChild(startMarker);
        startMarker.setLocalTranslation(start);
        rootNode.attachChild(startMarkerNormal);
        startMarkerNormal.setLocalTranslation(start);
    }

    @Override
    public String getInfo() {
        return "SINGLE_TARGET";
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void addTarget(Vector3f newTarget) {
        if (start == null) {
            start = newTarget;
        } else {
            targets.add(newTarget);
        }
        currentTargets++;
    }

    @Override
    public Vector3f getStart() {
        return start;
    }

    @Override
    public List<Vector3f> getTargets() {
        return targets;
    }

    @Override
    public Control cloneForSpatial(Spatial sptl) {
        return this;
    }

    @Override
    public void setSpatial(Spatial sptl) {
        this.rootNode = (Node) sptl;

    }

    private float calcFitness(Vector3f pos) {
        float fitness = start.distance(targets.get(0)) - (pos.distance(targets.get(0)));
        return fitness;
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
            } else if (current_time >= evalTime) { // Time ran out
                current_time = 0f;
                PAUSE = true;
                done = true;
                currentIndividual.setFitness(calcFitness(currentIndividual.getCreature().getRoot().getGeom().getLocalTranslation()));
            }

            Spatial child = rootNode.getChild("creature");
            if (child != null) {
                if (child.getControl(RigidBodyControl.class) != null) {
                    RigidBodyControl control = child.getControl(RigidBodyControl.class);
                    if (!control.isActive()) { // checks if creature is active - important for Sphere which can stop prematurely
                        control.activate();
                    }
                    if (control.getPhysicsLocation().y < -400) {
                        logger.info("Individual fell off");
                        currentIndividual.setFitness(-500f);
                        current_time = 0f;
                        PAUSE = true;
                        done = true;
                    }
                }
            }
        }
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void write(JmeExporter je) throws IOException {
        OutputCapsule out = je.getCapsule(this);
        out.write(start, "start", Vector3f.ZERO);
        out.writeSavableArrayList(targets, "targets", new ArrayList<>());
    }

    @Override
    public void read(JmeImporter ji) throws IOException {
        InputCapsule in = ji.getCapsule(this);
        start = (Vector3f) in.readSavable("start", Vector3f.ZERO);
        targets = (ArrayList) in.readSavableArrayList("targets", new ArrayList<>());
        currentTargets = getMaxTargets();
    }

    @Override
    public int getMaxTargets() {
        return 2;
    }

    @Override
    public void setBullet(BulletAppState appState) {
        this.bulletAppState = appState;
    }

    @Override
    public float getCurrentTime() {
        return current_time;
    }

    @Override
    public int getCurrentNumTargets() {
        return currentTargets;
    }

    @Override
    public void start() {
        STARTED = true;
    }
}
