/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.ConeJoint;
import com.jme3.bullet.joints.HingeJoint;
import com.jme3.bullet.joints.PhysicsJoint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import evolution.EvolutionController;
import java.util.ArrayList;
import java.util.List;
import util.Assets;
import evolution.individual.box.BoxCreature;

/**
 *
 * @author Andi
 */
public class MainControlsListener implements ActionListener {

    private BulletAppState bulletAppState;
    private Node rootNode;
    private RigidBodyControl brick_phy;
    private final Box box;
    private RigidBodyControl ball_phy;
    private final Box box2;
    private static final float brickLength = 0.48f;
    private static final float brickWidth = 0.24f;
    private static final float brickHeight = 0.12f;
    private Camera cam;
    private final float STANDARD_SPEED;
//    public static ArrayList<SixDofJoint> jointList = new ArrayList<>();
    private BoxCreature creature;
    private EvolutionController evoController;
    public MainControlsListener(BulletAppState bulletAppState, Node rootNode, Camera cam, EvolutionController cont) {
        this.bulletAppState = bulletAppState;
        STANDARD_SPEED = bulletAppState.getSpeed();
        this.rootNode = rootNode;
        box2 = new Box(brickLength, brickHeight, brickWidth);
        box2.scaleTextureCoordinates(new Vector2f(1f, 1f));
        /**
         * Initialize the brick geometry
         */
        box = new Box(brickLength, brickHeight, brickWidth);
        box.scaleTextureCoordinates(new Vector2f(1f, 1f));

        this.cam = cam;


        Geometry ball_geo = new Geometry("cannon ball", box2);
        ball_geo.setMaterial(Assets.stone_mat);
//        rootNode.attachChild(ball_geo);
        /**
         * Position the cannon ball
         */
        ball_geo.setLocalTranslation(new Vector3f(0, 1f, 0));
        /**
         * Make the ball physcial with a mass > 0.0f
         */
        ball_phy = new RigidBodyControl(1f);
        /**
         * Add physical ball to physics space.
         */
        ball_geo.addControl(ball_phy);

//        bulletAppState.getPhysicsSpace().add(ball_phy);
        this.evoController = cont;

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals("shoot") && !isPressed) {
//            makeCannonBall();
        } else if (name.equals("speedup") && isPressed) {
            float predictedSpeed = bulletAppState.getSpeed() + (250.0f * tpf);
            if (predictedSpeed > 20) {
                predictedSpeed = 20;
            }
            System.out.println("Speed now: " + predictedSpeed);
            EvolutionController.CURRENT_SPEED = predictedSpeed;
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
        } else if (name.equals("speeddown") && isPressed) {
            float predictedSpeed = bulletAppState.getSpeed() - (250.0f * tpf);
            if (predictedSpeed < 0) {
                predictedSpeed = 0;
            }
            EvolutionController.CURRENT_SPEED = predictedSpeed;
            System.out.println("Speed now: " + predictedSpeed);
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
        } else if (name.equals("reset") && isPressed) {
            EvolutionController.CURRENT_SPEED = STANDARD_SPEED;
            bulletAppState.setSpeed(STANDARD_SPEED);
            System.out.println("Speed now: " + STANDARD_SPEED);

        } else if (name.equals("motoron") && isPressed) {
            EvolutionController.STARTED = true;
            EvolutionController.CURRENT_SPEED = STANDARD_SPEED;
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
            System.out.println("EVOLUTION STARTED");
        } else if (name.equals("del") && isPressed) {
            if (creature != null) {
//                removeAllObjects();
            }
            EvolutionController.STARTED = false;
            EvolutionController.CURRENT_SPEED = 0f;
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
            System.out.println("EVOLUTION HALTED");
        } else if (name.equals("mut") && isPressed) {
            if (EvolutionController.CAM_ENABLED) {
                EvolutionController.CAM_ENABLED = false;
                
            } else {
                EvolutionController.CAM_ENABLED = true;
            }
        } else if (name.equals("save") && isPressed) {
            evoController.save();
        }

    }

    private void removeAllObjects() {


        while (rootNode.detachChildNamed("m2") != -1) {
//            System.out.println("detaching");
        }
        bulletAppState.getPhysicsSpace().getJointList().clear();
        List<PhysicsRigidBody> removal = new ArrayList<>();

        for (PhysicsRigidBody e : bulletAppState.getPhysicsSpace().getRigidBodyList()) {
            if (e.getUserObject() != null) {
                if (e.getUserObject() instanceof String) {
                    String object = (String) e.getUserObject();
                    if (object.equals("body")) {
                        removal.add(e);
                    }
                }
            }
        }
        for (PhysicsRigidBody e : removal) {
            bulletAppState.getPhysicsSpace().remove(e);
        }

        for (PhysicsJoint e : bulletAppState.getPhysicsSpace().getJointList()) {
            bulletAppState.getPhysicsSpace().remove(e);
        }

    }

    public void makeCannonBall() {
        /**
         * Create a cannon ball geometry and attach to scene graph.
         */
        Geometry ball_geo = new Geometry("cannon ball", box2);
        ball_geo.setMaterial(Assets.stone_mat);
        rootNode.attachChild(ball_geo);
        /**
         * Position the cannon ball
         */
        ball_geo.setLocalTranslation(cam.getLocation());
        /**
         * Make the ball physcial with a mass > 0.0f
         */
        ball_phy = new RigidBodyControl(1f);
        /**
         * Add physical ball to physics space.
         */
        ball_geo.addControl(ball_phy);

        bulletAppState.getPhysicsSpace().add(ball_phy);
        /**
         * Accelerate the physcial ball to shoot it.
         */
        ball_phy.setLinearVelocity(cam.getDirection().mult(25));

        Geometry brick_geo = new Geometry("brick", box);
        brick_geo.setMaterial(Assets.wall_mat);
        rootNode.attachChild(brick_geo);
        /**
         * Position the brick geometry
         */
        brick_geo.setLocalTranslation(cam.getLocation().add(new Vector3f(0, 0, -2 * box2.zExtent)));

        /**
         * Make brick physical with a mass > 0.0f.
         */
        brick_phy = new RigidBodyControl(1.0f);
        /**
         * Add physical brick to physics space.
         */
        brick_geo.addControl(brick_phy);

        HingeJoint joint = new HingeJoint(brick_geo.getControl(RigidBodyControl.class),
                ball_geo.getControl(RigidBodyControl.class),
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0, 2 * box2.zExtent),
                Vector3f.UNIT_Z,
                Vector3f.UNIT_Z);

        ConeJoint cjoint = new ConeJoint(brick_geo.getControl(RigidBodyControl.class),
                ball_geo.getControl(RigidBodyControl.class),
                new Vector3f(0f, 0f, 0f),
                new Vector3f(0f, 0, 2 * box2.zExtent));

        cjoint.setCollisionBetweenLinkedBodys(false);

//        joint.enableMotor(true, -1, 50.5f);
//        cjoint.setLimit(FastMath.QUARTER_PI, FastMath.QUARTER_PI, FastMath.QUARTER_PI);
        bulletAppState.getPhysicsSpace().add(cjoint);
        bulletAppState.getPhysicsSpace().add(brick_phy);

    }
}
