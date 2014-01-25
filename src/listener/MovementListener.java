/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import com.jme3.bullet.joints.SixDofJoint;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.FastMath;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.io.IOException;
import java.util.Iterator;
import evolution.individual.box.bodytypes.Joint;
import evolution.individual.box.bodytypes.JointTypes;

/**
 *
 * @author Andi
 */
public class MovementListener implements Control {

    private Joint joint;
    private Geometry box;
    private boolean enabled = false;
    private float movement;
    private SixDofJoint sixDof;
    private TerrainQuad terrain;

    public MovementListener(Joint j, Geometry spatial, SixDofJoint sixdof, TerrainQuad quad) {
        this.joint = j;
        this.box = spatial;
        movement = 0f;
        this.sixDof = sixdof;
        this.terrain = quad;
        if (j.getJointType() == JointTypes.continousJoint) {
            enabled = true;

            sixDof.getRotationalLimitMotor(0).setEnableMotor(true);
            sixDof.getRotationalLimitMotor(1).setEnableMotor(true);
            sixDof.getRotationalLimitMotor(2).setEnableMotor(true);
        }
    }

    public SixDofJoint getSixDof() {
        return sixDof;
    }

    public void setSixDof(SixDofJoint sixDof) {
        this.sixDof = sixDof;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        return new MovementListener(joint, box, sixDof, terrain);

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setSpatial(Spatial spatial) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float tpf) {
//        System.out.println("IS ENABLED: "+isEnabled());
        if (isEnabled()) {
            doMovement(tpf);
//            System.out.println("Type:"+joint.getJointType() + " Doing movement");
//            System.out.println(joint.getForces());
//            System.out.println(joint.getUpperBound());
//            System.out.println(joint.getLowerBound());
        }
        if (joint.getJointType() == JointTypes.reactiveJoint) {
            if (checkCollision()) {
                setEnabled(true);
//                doMovement(tpf);
                setMovement(true);
            } else {
                setEnabled(false);
                setMovement(false);
//                movement = 0f;
            }
        }
    }

    private void doMovement(float tpf) {
        movement += tpf;

        float factor = FastMath.sin(movement);

        if (movement > FastMath.TWO_PI) {
            movement = 0f;
        }

        sixDof.getBodyA().activate();
        sixDof.getBodyB().activate();
        sixDof.getRotationalLimitMotor(0).setTargetVelocity(factor * joint.getForces().x);
        sixDof.getRotationalLimitMotor(1).setTargetVelocity(factor * joint.getForces().y);
        sixDof.getRotationalLimitMotor(2).setTargetVelocity(factor * joint.getForces().z);

    }

    private void setMovement(boolean state) {
        if (state) {
//            System.out.println("MOTOR ON");
            sixDof.getRotationalLimitMotor(0).setEnableMotor(true);
            sixDof.getRotationalLimitMotor(1).setEnableMotor(true);
            sixDof.getRotationalLimitMotor(2).setEnableMotor(true);
        } else {
//            System.out.println("MOTOR OFF");
            sixDof.getRotationalLimitMotor(0).setEnableMotor(false);
            sixDof.getRotationalLimitMotor(1).setEnableMotor(false);
            sixDof.getRotationalLimitMotor(2).setEnableMotor(false);
        }
    }

    private boolean checkCollision() {
//        System.out.println("CHECKING COLLISION");
        terrain.updateGeometricState();
        CollisionResults res = new CollisionResults();
        int nr = terrain.collideWith(box.getWorldBound(), res);
        Iterator<CollisionResult> it = res.iterator();
        if (nr > 0) {
            while (it.hasNext()) {
                CollisionResult cr = it.next();
//                    System.out.println(cr.);
//                    System.out.println(cr.getGeometry());
                return true;

            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void read(JmeImporter im) throws IOException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
