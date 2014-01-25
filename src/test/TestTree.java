package test;

import com.jme3.math.Vector3f;
import evolution.individual.box.bodytypes.BoxBody;
import evolution.individual.box.bodytypes.Joint;
import evolution.individual.box.bodytypes.JointTypes;

/**
 * Just a TestCreature
 * @author Andi
 */
public class TestTree {

    public BoxBody rootNode;

    public TestTree() {
        rootNode = new BoxBody(new Vector3f(0.5f, 0.5f, 0.5f),"main");


        Joint leftJoint = new Joint(new Vector3f(1.0f, 0, 0), JointTypes.staticJoint);
        rootNode.getChildren().add(leftJoint);
        BoxBody bb = new BoxBody(new Vector3f(0.5f, 0.5f, 0.5f),  "m1");
        leftJoint.setChild(bb);

        Joint rightJoint = new Joint(new Vector3f(-0.5f, 1f, 0), JointTypes.reactiveJoint);


        bb.getChildren().add(rightJoint);

        BoxBody bb2 = new BoxBody(new Vector3f(0.5f, 0.5f, 0.5f),  "m2");
        rightJoint.setChild(bb2);

        Joint rightJoint2 = new Joint(new Vector3f(-1f, -1f, 0), JointTypes.continuousJoint);


        bb2.getChildren().add(rightJoint2);

        BoxBody bb22 = new BoxBody(new Vector3f(0.5f, 0.5f, 0.5f),   "m2");
        rightJoint2.setChild(bb22);

    }
}
