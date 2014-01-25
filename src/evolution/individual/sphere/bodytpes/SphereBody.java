/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evolution.individual.sphere.bodytpes;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import evolution.EvolutionConstants;
import java.io.IOException;
import nodes.TNode;
import util.Assets;

/**
 *
 * @author Andi
 */
public class SphereBody extends TNode {

    private float radius = 1f;
    private Geometry geom;

    public SphereBody() {
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public TNode clone() {
        SphereBody body = new SphereBody();
        body.setRadius(radius);
        return body;
    }

    @Override
    public Geometry getGeom() {
        return geom;
    }

    @Override
    public void makeGeometry() {
        geom = new Geometry("creature", new Sphere(25, 25, radius));
        RigidBodyControl control = new RigidBodyControl(EvolutionConstants.DENSITY * 15);
        geom.addControl(control);
        geom.getControl(RigidBodyControl.class).setFriction(2f);
//        control.setPhysicsLocation(new Vector3f(0,getSize().y+0.5f,0));
        geom.setMaterial(Assets.stone_mat);
        control.setRestitution(1f);
        control.setDamping(0.1f, 0.1f);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(radius, "rad", 1f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        radius = (float) capsule.readFloat("rad", 1f);
        makeGeometry();
    }
}
