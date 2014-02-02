package de.unibi.evolution.individual.sphere;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.terrain.geomipmap.TerrainQuad;
import de.unibi.config.EvolutionConfig;
import static de.unibi.evolution.Population.number;
import de.unibi.evolution.individual.AbstractIndividual;
import java.io.IOException;
import de.unibi.evolution.individual.sphere.bodytpes.SphereBody;

/**
 * This Individual is a basic sphere which rolls across the terrain.
 *
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


        TerrainQuad terrainf = new TerrainQuad("my terrain", 65, terrain.getTerrainSize(), terrain.getHeightMap().clone());
        terrainf.setMaterial(terrain.getMaterial().clone());
        terrainf.setLocalTranslation(0, -100, 0);
        terrainf.setLocalScale(1f, 1f, 1f);

        terrainf.setName("TERRAIN");
        indiv.setTerrain(terrainf);
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
    public AbstractIndividual<SphereCreature> createRandomIndividual(EvolutionConfig config) {
        SphereIndividual newOne = new SphereIndividual(number++);
        newOne.setTerrain(createNewRandomTerrain(config));

        SphereCreature creat = new SphereCreature(new SphereBody());
        newOne.setCreature(creat);
        return newOne;
    }

    @Override
    public final String getType() {
        return "SPHERE";
    }
}
