/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.individual.box.bodytypes;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import evolution.EvolutionConstants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import nodes.TNode;
import util.Assets;

/**
 *
 * @author Andi
 */
public class BoxBody extends TNode {

    private Vector3f size;
    private Box body;
    private Geometry geom;
    private RigidBodyControl control;
    private Vector3f origin;

    public BoxBody() {
//        setChildren(new ArrayList<Joint>());
    }

    public BoxBody(Vector3f size, String name) {

        this.size = size;

        origin = new Vector3f(FastMath.nextRandomFloat() * 2 - 1, FastMath.nextRandomFloat() * 2 - 1, FastMath.nextRandomFloat() * 2 - 1);
        body = new Box(size.x, size.y, size.z);
        setChildren(new CopyOnWriteArrayList<Joint>());
        makeGeometry();
    }

    @Override
    public void makeGeometry() {
        control = new RigidBodyControl(getSize().x * getSize().y * getSize().z * EvolutionConstants.DENSITY);

        Box b = new Box(getSize().x, getSize().y, getSize().z);
        body= b;
        geom = new Geometry("creature", b);
        geom.addControl(control);
        geom.getControl(RigidBodyControl.class).setFriction(1f);
//        control.setPhysicsLocation(new Vector3f(0,getSize().y+0.5f,0));
        geom.setMaterial(Assets.stone_mat);
        control.setRestitution(0f);
    }

    public Vector3f getSize() {
        return size;
    }

    public void setSize(Vector3f size) {
        this.size = size.normalize();
    }

    public Box getBox() {
        return body;
    }

    @Override
    public Geometry getGeom() {
//        makeGeometry();
        return geom;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public void setOrigin(Vector3f origin) {
        this.origin = origin;
    }

    public RigidBodyControl getControl() {
        return control;
    }

    public void setControl(RigidBodyControl control) {
        this.control = control;
    }

    @Override
    public BoxBody clone() {
        BoxBody box = new BoxBody(size.clone(), "creature");
        List<Joint> joints = getChildren();
        box.setOrigin(origin.clone());
        List<Joint> newJoints = new ArrayList<>();
        for (Joint e : joints) {
            Joint tempJoint = new Joint(e.getAttachPoint(), e.getJointType());
            tempJoint.setChild(e.getChild().clone());
            newJoints.add(tempJoint);
        }
        box.setChildren(newJoints);
        return box;

    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(size, "size", new Vector3f());
        capsule.write(origin, "origin", new Vector3f());
        ArrayList<Joint> savableList = new ArrayList<>();
        savableList.addAll(getChildren());
        capsule.writeSavableArrayList(savableList, "children", new ArrayList<>());
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        size = (Vector3f) capsule.readSavable("size", new Vector3f());
        origin = (Vector3f) capsule.readSavable("origin", new Vector3f());
        children = new CopyOnWriteArrayList(capsule.readSavableArrayList("children", new ArrayList()));
        makeGeometry();
    }
}
