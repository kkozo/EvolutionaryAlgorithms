package evolution.individual.sphere;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.terrain.geomipmap.TerrainQuad;
import static evolution.Population.number;
import evolution.individual.AbstractIndividual;
import java.io.IOException;
import evolution.individual.sphere.bodytpes.SphereBody;

/**
 * This Individual is a basic sphere which rolls across the terrain.
 * @author Andi
 */
public class SphereIndividual extends AbstractIndividual<SphereCreature> {

    private SphereCreature creature;

    public SphereIndividual() {
    }

    public SphereIndividual(int newId) {
        fitness = 0f;
        id = newId;
    }

    @Override
    public SphereCreature getCreature() {
        return creature;
    }

    @Override
    public void setCreature(SphereCreature creature) {
        this.creature = creature;
    }

    @Override
    public SphereIndividual clone() {
        SphereIndividual indiv = new SphereIndividual(id);
        indiv.setCreature(creature.clone());
        indiv.setTerrain((TerrainQuad)terrain.deepClone());
        return indiv;
    }
   

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(creature, "creat", new SphereCreature());
        out.write(terrain, "terrain", new TerrainQuad());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        creature = (SphereCreature) in.readSavable("creat", new SphereCreature());
        terrain = (TerrainQuad) in.readSavable("terrain", new TerrainQuad());
        terrain.setLocalTranslation(0, -100, 0);
    }

    @Override
    public AbstractIndividual<SphereCreature> createRandomIndividual() {
        SphereIndividual newOne = new SphereIndividual(number++);
        newOne.setTerrain(createNewRandomTerrain());
        
        SphereCreature creat = new SphereCreature(new SphereBody());
        newOne.setCreature(creat);
        return newOne;
    }
    

    @Override
    public final String getType() {
        return "SPHERE";
    }
    
    

}
