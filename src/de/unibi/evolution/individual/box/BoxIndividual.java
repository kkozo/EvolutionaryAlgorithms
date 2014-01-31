package de.unibi.evolution.individual.box;

import de.unibi.evolution.individual.box.bodytypes.BoxBody;
import de.unibi.evolution.individual.box.bodytypes.JointTypes;
import de.unibi.evolution.individual.box.bodytypes.Joint;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.Mutations;
import static de.unibi.evolution.Population.number;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.util.Assets;
import java.io.IOException;

/**
 * A type of creature that connects boxes with joints. Joints can move.
 * @author Andi
 */
public class BoxIndividual extends AbstractIndividual<BoxCreature> {

    private BoxCreature creature;

    public BoxIndividual() {
    }

    public BoxIndividual(int newId) {
        fitness = 0f;
        id = newId;
    }

    @Override
    public BoxCreature getCreature() {
        return creature;
    }

    @Override
    public void setCreature(BoxCreature creature) {
        this.creature = creature;
    }

    @Override
    public BoxIndividual clone() {
        BoxIndividual indiv = new BoxIndividual(id);
        indiv.setCreature(creature.clone());
        TerrainQuad terrainf = new TerrainQuad("my terrain", 65, 129, terrain.getHeightMap().clone());

        terrainf.setMaterial(Assets.mat_terrain);
        terrainf.setLocalTranslation(0, -100, 0);
        terrainf.setLocalScale(1f, 1f, 1f);

        terrainf.setName("TERRAIN");
        indiv.setTerrain(terrainf);
        return indiv;
    }
   

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(creature, "creat", new BoxCreature());
        out.write(terrain, "terrain", new TerrainQuad());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        creature = (BoxCreature) in.readSavable("creat", new BoxCreature());
        terrain = (TerrainQuad) in.readSavable("terrain", new TerrainQuad());
        terrain.setLocalTranslation(0, -100, 0);
    }

    @Override
    public AbstractIndividual<BoxCreature> createRandomIndividual(EvolutionConfig config) {
        BoxIndividual newOne = new BoxIndividual(number++);
        newOne.setTerrain(createNewRandomTerrain());
        Vector3f rootBoxSize = new Vector3f(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat()).normalize();
        BoxBody rootBody = new BoxBody(rootBoxSize, "Main");
        int initialJoints = FastMath.nextRandomInt(1, config.getJointInitMax());
        for (int i = 0; i < initialJoints; ++i) {

            Joint myJoint = createJoint();
            myJoint.setForces(Mutations.createRandomVector());
            BoxBody myBody = createBoxBody(2);
            myJoint.setChild(myBody);
            rootBody.getChildren().add(myJoint);
        }
        BoxCreature creat = new BoxCreature(rootBody);
        newOne.setCreature(creat);
        return newOne;
    }
    
   

   public BoxBody createBoxBody(int max) {
        Vector3f boxSize = new Vector3f(FastMath.nextRandomFloat(), FastMath.nextRandomFloat(), FastMath.nextRandomFloat()).normalize();
        BoxBody boxBody = new BoxBody(boxSize, "bodyType");
        if (max > 0) {
            Joint myJoint = createJoint();
            BoxBody myBody = createBoxBody(--max);
            myJoint.setChild(myBody);
            boxBody.getChildren().add(myJoint);
        }
        return boxBody;
    }

    public Joint createJoint() {
        Vector3f attachPoint = Mutations.createRandomVector();
        int jointType = FastMath.nextRandomInt(JointTypes.staticJoint.ordinal(), JointTypes.continuousJoint.ordinal());
        Joint newJoint = new Joint(attachPoint, JointTypes.values()[jointType]);

        return newJoint;
    }

    @Override
    public final String getType() {
        return "BOX";
    }
    
    

}
