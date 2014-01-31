package de.unibi.evolution.individual.box.bodytypes;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import de.unibi.evolution.Mutations;
import java.io.IOException;
import de.unibi.evolution.nodes.TNode;

/**
 * Joint class which connects two TNodes
 * @author Andi
 */
public class Joint implements Savable {

    private Vector3f upperBound;
    private Vector3f lowerBound;
    private Vector3f attachPoint;
    private TNode child;
    private JointTypes jointType;
    private Vector3f forces;
    public static Vector3f UPPER_ANGULAR_LIMIT = new Vector3f(FastMath.QUARTER_PI, FastMath.QUARTER_PI, FastMath.QUARTER_PI);
    public static Vector3f LOWER_ANGULAR_LIMIT = new Vector3f(-FastMath.QUARTER_PI, -FastMath.QUARTER_PI, -FastMath.QUARTER_PI);
    public static Vector3f STATIC_LIMIT = new Vector3f(0f, 0f, 0f);

    public Joint() {
    }

    public Joint(Vector3f attachPoint, JointTypes types) {
        if (types == JointTypes.staticJoint) {
//            this.upperBound = STATIC_LIMIT;
//            this.lowerBound = STATIC_LIMIT;
            this.upperBound = UPPER_ANGULAR_LIMIT;
            this.lowerBound = LOWER_ANGULAR_LIMIT;
        } else {
            this.upperBound = UPPER_ANGULAR_LIMIT;
            this.lowerBound = LOWER_ANGULAR_LIMIT;
        }
        this.attachPoint = attachPoint;

        jointType = types;
        forces = Mutations.createRandomVector();
    }

    public TNode getChild() {
        return child;
    }

    public void setChild(TNode node) {
        this.child = node;
    }

    public Vector3f getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(Vector3f lowerBound) {
        this.lowerBound = lowerBound;
    }

    public Vector3f getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(Vector3f upperBound) {
        this.upperBound = upperBound;
    }

    public Vector3f getAttachPoint() {
        return attachPoint;
    }

    public void setAttachPoint(Vector3f attachPoint) {
        this.attachPoint = attachPoint;
    }

    public JointTypes getJointType() {
        return jointType;
    }

    public void setJointType(JointTypes jointType) {
        this.jointType = jointType;
    }

    public Vector3f getForces() {
        return forces;
    }

    public void setForces(Vector3f forces) {
        this.forces = forces;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(upperBound, "upperBound", new Vector3f());
        capsule.write(lowerBound, "lowerBound", new Vector3f());
        capsule.write(attachPoint, "attachPoint", new Vector3f());
        capsule.write(forces, "forces", new Vector3f());
        capsule.write(child, "jointChild", new BoxBody());
        capsule.write(jointType, "jointType", JointTypes.continuousJoint);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this);
        upperBound = (Vector3f) capsule.readSavable("upperBound", new Vector3f());
        lowerBound = (Vector3f) capsule.readSavable("lowerBound", new Vector3f());
        attachPoint = (Vector3f) capsule.readSavable("attachPoint", new Vector3f());
        forces = (Vector3f) capsule.readSavable("forces", new Vector3f());
        child = (BoxBody) capsule.readSavable("jointChild", new BoxBody());
        child.makeGeometry();
        jointType = (JointTypes) capsule.readEnum("jointType", JointTypes.class, JointTypes.continuousJoint);
    }
}
