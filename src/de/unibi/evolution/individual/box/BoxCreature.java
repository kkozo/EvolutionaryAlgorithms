package de.unibi.evolution.individual.box;

import de.unibi.evolution.individual.box.bodytypes.BoxBody;
import de.unibi.evolution.individual.box.bodytypes.Joint;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.joints.SixDofJoint;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import de.unibi.evolution.Mutations;
import de.unibi.evolution.individual.AbstractCreature;
import java.io.IOException;
import de.unibi.listener.MovementListener;
import de.unibi.evolution.nodes.TNode;

/**
 * Extension of AbstractCreature for the Box Creature
 * @author Andi
 */
public class BoxCreature extends AbstractCreature {

    private BoxBody rootNode;
    private int id;

    public BoxCreature() {
        rootNode = new BoxBody(new Vector3f(1f, 0.5f, 1f).normalize(), "main");
    }

    public BoxCreature(BoxBody rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public void getNode(Node rNode, PhysicsSpace space, int id, TerrainQuad quad, Vector3f startVector) {
        rootNode.getGeom().setLocalTranslation(startVector);
        attachCreatureWalker(rNode, space, rootNode, quad);
    }

    @Override
    public void resetCreature() {
        resetCreature(rootNode);
    }

    @Override
    public TNode getRoot() {
        return rootNode;
    }

    private void attachCreatureWalker(Node node, PhysicsSpace space, TNode tnode, TerrainQuad quad) {
//        ((BoxBody) tnode).makeGeometry();
        node.attachChild(tnode.getGeom());
        space.add(tnode.getGeom().getControl(RigidBodyControl.class));
        tnode.getGeom().setUserData("ID", id);
        for (Joint e : tnode.getChildren()) {
            TNode eNode = e.getChild();
            BoxBody eff = (BoxBody) eNode;
            space.add(joinTwoEntities((BoxBody) tnode, eff, e, quad));
            Geometry beff = eff.getGeom();
            beff.setUserData("ID", id);
            space.add(beff.getControl(RigidBodyControl.class));

            attachCreatureWalker(node, space, eff, quad);
        }


    }

    public void resetCreature(TNode tnode) {
        ((BoxBody) tnode).makeGeometry();

        for (Joint e : tnode.getChildren()) {
            TNode eNode = e.getChild();
            BoxBody eff = (BoxBody) eNode;
            eff.makeGeometry();
            resetCreature(eff);
        }

    }

    private SixDofJoint joinTwoEntities(BoxBody a, BoxBody b, Joint joint, TerrainQuad terrain) {

        Vector3f point = new Vector3f(joint.getAttachPoint().x * a.getBox().xExtent, joint.getAttachPoint().y * a.getBox().yExtent, joint.getAttachPoint().z * a.getBox().zExtent);
        Vector3f otherPoint = new Vector3f(-b.getOrigin().x * b.getBox().xExtent * 1.8f, -b.getOrigin().y * b.getBox().yExtent * 1.8f, -b.getOrigin().z * b.getBox().zExtent * 1.8f);

        SixDofJoint cj = new SixDofJoint(a.getGeom().getControl(RigidBodyControl.class), b.getGeom().getControl(RigidBodyControl.class), point, otherPoint, false);
        a.getGeom().getControl(RigidBodyControl.class).setUserObject("body");
        b.getGeom().getControl(RigidBodyControl.class).setUserObject("body");
        a.getGeom().getControl(RigidBodyControl.class).setPhysicsLocation(Mutations.createRandomVector().mult(((FastMath.nextRandomFloat() * 10) - 5)));
        b.getGeom().getControl(RigidBodyControl.class).setPhysicsLocation(Mutations.createRandomVector().mult(((FastMath.nextRandomFloat() * 10) - 5)));
        cj.setCollisionBetweenLinkedBodys(false);
        a.getGeom().getControl(RigidBodyControl.class).setAngularDamping(1.1f);
//         a.getGeom().getControl(RigidBodyControl.class).setLinearDamping(1.5f);
        b.getGeom().getControl(RigidBodyControl.class).setAngularDamping(1.1f);
//         b.getGeom().getControl(RigidBodyControl.class).setLinearDamping(1.5f);
        cj.setAngularLowerLimit(joint.getUpperBound());
        cj.setAngularUpperLimit(joint.getLowerBound());
        a.getGeom().getControl(RigidBodyControl.class).clearForces();
        b.getGeom().getControl(RigidBodyControl.class).clearForces();

        cj.setLinearLowerLimit(Vector3f.ZERO);
        cj.setLinearUpperLimit(Vector3f.ZERO);
        cj.getRotationalLimitMotor(0).setBounce(0.5f);
        cj.getRotationalLimitMotor(1).setBounce(0.5f);
        cj.getRotationalLimitMotor(2).setBounce(0.5f);
        b.getGeom().setUserData("cj", cj);
        cj.getRotationalLimitMotor(0).setMaxMotorForce(50);
        cj.getRotationalLimitMotor(1).setMaxMotorForce(50);
        cj.getRotationalLimitMotor(2).setMaxMotorForce(50);
        b.getGeom().addControl(new MovementListener(joint, b.getGeom(), cj, terrain));
//        InsertListener.jointList.add(cj);
//        cj.getRotationalLimitMotor(0).setTargetVelocity(0.3f);
//        cj.getRotationalLimitMotor(1).setEnableMotor(true);
//        cj.getRotationalLimitMotor(1).setTargetVelocity(0.3f);
//        cj.getTranslationalLimitMotor()
        return cj;
    }

    @Override
    public BoxCreature clone() {
        BoxCreature creat = new BoxCreature(rootNode.clone());
        return creat;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(id, "creatId", id);
        capsule.write(rootNode, "mainBox", new BoxBody());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        id = capsule.readInt("creatId", 1);
        rootNode = (BoxBody) capsule.readSavable("mainBox", new BoxBody());

    }

    public int getJoints() {
        int joints = Mutations.getJoints(rootNode);
        return joints;
    }
}
