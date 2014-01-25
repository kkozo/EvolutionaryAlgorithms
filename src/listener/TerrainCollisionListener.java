/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.joints.SixDofJoint;
import java.util.ArrayList;
import java.util.List;
import evolution.individual.box.bodytypes.BoxBody;

/**
 *
 * @author Andi
 */
public class TerrainCollisionListener implements PhysicsCollisionListener {

    public static List<BoxBody> bodies = new ArrayList<>();

    public TerrainCollisionListener() {
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
//        System.out.println(event.getType());
//        System.out.println(event.getLifeTime());
//       event.getN
//        if (event.getType() == PhysicsCollisionEvent.TYPE_ADDED) {
//        if (event.getNodeA().getUserData("joint"))
            if (event.getNodeA().getName().equals("TERRAIN") || event.getNodeB().getName().equals("TERRAIN")) {
                System.out.println(event.getNodeA().getName());
                System.out.println(event.getNodeB().getName());
                Object objA = event.getNodeA().getUserData("cj");
                if (objA != null) {
                    if (objA instanceof SixDofJoint) {
                        SixDofJoint sdofJoint = (SixDofJoint) objA;
                        sdofJoint.getRotationalLimitMotor(0).setEnableMotor(true);
                        sdofJoint.getRotationalLimitMotor(1).setEnableMotor(true);
                        sdofJoint.getRotationalLimitMotor(2).setEnableMotor(true);
                        sdofJoint.getRotationalLimitMotor(0).setTargetVelocity(50f);
                        sdofJoint.getRotationalLimitMotor(1).setTargetVelocity(50f);
                        sdofJoint.getRotationalLimitMotor(2).setTargetVelocity(50f);
                    }
                }
                Object objB = event.getNodeB().getUserData("cj");
                if (objB != null) {
                    if (objB instanceof SixDofJoint) {
                        SixDofJoint sdofJoint = (SixDofJoint) objB;
                        sdofJoint.getRotationalLimitMotor(0).setEnableMotor(true);
                        sdofJoint.getRotationalLimitMotor(1).setEnableMotor(true);
                        sdofJoint.getRotationalLimitMotor(2).setEnableMotor(true);
                        sdofJoint.getRotationalLimitMotor(0).setTargetVelocity(50f);
                        sdofJoint.getRotationalLimitMotor(1).setTargetVelocity(50f);
                        sdofJoint.getRotationalLimitMotor(2).setTargetVelocity(50f);
                    }
                }
            }
//        }
//        System.out.println("boxcollide");
    }
}
