package evolution.individual;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainQuad;
import java.io.IOException;
import evolution.nodes.TNode;

/**
 * Basic abstract class for the creation of a creature.
 * This has to be extended and connected to an AbstractIndividual.
 * The Selector, Mutator and Recombiner should be specially implemented for a specific type of creature.
 * 
 * @author Andi
 */
public abstract class AbstractCreature implements Savable {

    private int id;

    public AbstractCreature() {
    }

    public abstract TNode getRoot();

    public abstract void getNode(Node rNode, PhysicsSpace space, int id, TerrainQuad quad);

    public abstract void resetCreature();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract AbstractCreature clone();

    public abstract void write(JmeExporter ex) throws IOException;

    public abstract void read(JmeImporter im) throws IOException;
}
