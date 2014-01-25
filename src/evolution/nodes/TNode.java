package evolution.nodes;

import evolution.individual.box.bodytypes.Joint;
import com.jme3.export.Savable;
import com.jme3.scene.Geometry;
import java.util.List;

/**
 * Main Node for Bodies. Bodies can be connected with Joints.
 * @author Andi
 */
public abstract class TNode implements Savable{

    private TNode type;
    private TNode parent;
    protected List<Joint> children;

    public TNode getType() {
        return type;
    }

    public void setType(TNode type) {
        this.type = type;
    }

    public TNode getParent() {
        return parent;
    }

    public void setParent(TNode parent) {
        this.parent = parent;
    }

    public List<Joint> getChildren() {
        return children;
    }

    public void setChildren(List<Joint> children) {
        this.children = children;
    }
    public abstract TNode clone();
    public abstract Geometry getGeom();
    public abstract void makeGeometry();
}
