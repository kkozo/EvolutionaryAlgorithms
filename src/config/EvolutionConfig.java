package config;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;

/** TODO: Implement fully
 * EvolutionConfig
 * @author Andi
 */
public class EvolutionConfig implements Savable {

    private String recombiner;
    private String mutator;
    private String selector;
    private String individualType;
    private int populationSize;
    private int selection;
    private int kids;
    private float evalTime;
    private float density;
    private int jointMaxPerIndividual;
    private int jointMinPerIndividual;
    private int maxCreatureMutations;
    private int maxTerrainMutations;
    private float terrainMutStr;
    private float maxHeight;
    private float deleteThreshold;
    private String name;
    private int jointInitMax;

    public EvolutionConfig() {
        jointInitMax = 4;
        populationSize = 25;
        selection = 10;
        kids = 5;
        evalTime = 50f;
        density = 5.5f;
        jointMaxPerIndividual = 10;
        jointMinPerIndividual = 2;
        maxCreatureMutations = 3;
        maxTerrainMutations = 1;
        terrainMutStr = 0.7f;
        maxHeight = 4.5f;
        deleteThreshold = -5f;
        name = "Default";
        individualType = "BOX";
    }

    public String getRecombiner() {
        return recombiner;
    }

    public void setRecombiner(String recombiner) {
        this.recombiner = recombiner;
    }

    public String getMutator() {
        return mutator;
    }

    public void setMutator(String mutator) {
        this.mutator = mutator;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public String getIndividualType() {
        return individualType;
    }

    public void setIndividualType(String individualType) {
        this.individualType = individualType;
    }

    public int getJointInitMax() {
        return jointInitMax;
    }

    public void setJointInitMax(int jointInitMax) {
        this.jointInitMax = jointInitMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getKids() {
        return kids;
    }

    public void setKids(int kids) {
        this.kids = kids;
    }

    public float getEvalTime() {
        return evalTime;
    }

    public void setEvalTime(float evalTime) {
        this.evalTime = evalTime;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getJointMaxPerIndividual() {
        return jointMaxPerIndividual;
    }

    public void setJointMaxPerIndividual(int jointMaxPerIndividual) {
        this.jointMaxPerIndividual = jointMaxPerIndividual;
    }

    public int getJointMinPerIndividual() {
        return jointMinPerIndividual;
    }

    public void setJointMinPerIndividual(int jointMinPerIndividual) {
        this.jointMinPerIndividual = jointMinPerIndividual;
    }

    public int getMaxCreatureMutations() {
        return maxCreatureMutations;
    }

    public void setMaxCreatureMutations(int maxCreatureMutations) {
        this.maxCreatureMutations = maxCreatureMutations;
    }

    public int getMaxTerrainMutations() {
        return maxTerrainMutations;
    }

    public void setMaxTerrainMutations(int maxTerrainMutations) {
        this.maxTerrainMutations = maxTerrainMutations;
    }

    public float getTerrainMutStr() {
        return terrainMutStr;
    }

    public void setTerrainMutStr(float terrainMutStr) {
        this.terrainMutStr = terrainMutStr;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    public float getDeleteThreshold() {
        return deleteThreshold;
    }

    public void setDeleteThreshold(float deleteThreshold) {
        this.deleteThreshold = deleteThreshold;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(populationSize, "popSize", 25);
        out.write(jointInitMax, "jointInitMax", 4);
        out.write(selection, "selection", 10);
        out.write(kids, "kids", 5);
        out.write(evalTime, "evalTime", 150f);
        out.write(density, "density", 5.5f);
        out.write(jointMaxPerIndividual, "jointMaxPerIndividual", 10);
        out.write(jointMinPerIndividual, "jointMinPerIndividual", 2);
        out.write(maxCreatureMutations, "maxCreatureMutations", 3);
        out.write(maxTerrainMutations, "maxTerrainMutations", 1);
        out.write(terrainMutStr, "terrainMutStr", 0.7f);
        out.write(maxHeight, "maxHeight", 4.5f);
        out.write(deleteThreshold, "deleteThreshold", -5f);
        out.write(name, "name", "Default");
        out.write(individualType, "individualType", "BOX");
        out.write(recombiner, "recombiner", "BOX");
        out.write(mutator, "mutator", "BOX");
        out.write(selector, "selector", "BOX");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        jointInitMax = in.readInt("jointInitMax", 4);
        populationSize = in.readInt("popSize", 25);
        selection = in.readInt("selection", 10);
        kids = in.readInt("kids", 5);
        evalTime = in.readFloat("evalTime", 50f);
        density = in.readFloat("density", 5.5f);
        jointMaxPerIndividual = in.readInt("jointMaxPerIndividual", 10);
        jointMinPerIndividual = in.readInt("jointMinPerIndividual", 2);
        maxCreatureMutations = in.readInt("maxCreatureMutations", 3);
        maxTerrainMutations = in.readInt("maxTerrainMutations", 1);
        terrainMutStr = in.readFloat("terrainMutStr", 0.7f);
        maxHeight = in.readFloat("maxHeight", 4.5f);
        deleteThreshold = in.readFloat("deleteThreshold", -5f);
        name = in.readString("name", "Default");
        individualType = in.readString("individualType", "BOX");
        recombiner = in.readString("recombiner", "BOX");
        mutator = in.readString("mutator", "BOX");
        selector = in.readString("selector", "BOX");
    }
}
