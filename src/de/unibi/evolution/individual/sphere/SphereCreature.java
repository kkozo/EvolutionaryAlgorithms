package de.unibi.evolution.individual.sphere;

import de.unibi.evolution.individual.sphere.bodytpes.SphereBody;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import de.unibi.evolution.individual.AbstractCreature;
import java.io.IOException;
import de.unibi.evolution.nodes.TNode;

/**
 * Creature implementation for the Sphere.
 * @author Andi
 */
public class SphereCreature extends AbstractCreature {

    private int id;
    private TNode rootNode;
    public SphereCreature() {
        rootNode = new SphereBody();
    }

  public SphereCreature(TNode root) {
        this.rootNode = root;
    }

    @Override
    public void getNode(Node rNode, PhysicsSpace space, int id, TerrainQuad quad) {
        rNode.attachChild(rootNode.getGeom());
        space.add(rootNode.getGeom().getControl(RigidBodyControl.class));
    }

    @Override
    public void resetCreature() {
        rootNode.makeGeometry();
    }

    @Override
    public TNode getRoot() {
        return rootNode;
    }

    @Override
    public SphereCreature clone() {
        SphereCreature creat = new SphereCreature(rootNode.clone());
        return creat;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(id, "creatId", id);
        capsule.write(rootNode, "mainSphere", new SphereBody());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        id = capsule.readInt("creatId", 1);
        rootNode = (SphereBody) capsule.readSavable("mainSphere", new SphereBody());

    }

}
