package de.unibi.config;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.modifiers.IFitness;
import de.unibi.evolution.modifiers.IMutator;
import de.unibi.evolution.modifiers.IRecombiner;
import de.unibi.evolution.modifiers.ISelector;
import de.unibi.evolution.modifiers.general.TargetFitness;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Implement fully EvolutionConfig
 *
 * @author Andi
 */
public class EvolutionConfig implements Savable {

    private static final Logger logger = Logger.getLogger(EvolutionConfig.class.getName());
    private IRecombiner recombiner;
    private IMutator mutator;
    private ISelector selector;
    private IFitness fitnessFunction;
    private AbstractIndividual individualType;
    private int populationSize;
    private int selectionSize;
    private int kids;
    private float evalTime;
    private int jointMaxPerIndividual;
    private int jointMinPerIndividual;
    private int maxCreatureMutations;
    private int maxTerrainMutations;
    private float terrainMutStr;
    private float maxHeight;
    private float deleteThreshold;
    private String name;
    private int jointInitMax;
    private String color;
    private int terrainSize;
    private float creatMutStr;

    public EvolutionConfig() {
        jointInitMax = 4;
        populationSize = 25;
        selectionSize = 10;
        kids = 5;
        evalTime = 20f;
        jointMaxPerIndividual = 10;
        jointMinPerIndividual = 2;
        maxCreatureMutations = 3;
        maxTerrainMutations = 1;
        terrainMutStr = 0.3f;
        maxHeight = 6.5f;
        deleteThreshold = -15f;
        name = "Default";
        color = "MINT";
        terrainSize = 129;
        creatMutStr = 0.9f;
    }

    public float getCreatMutStr() {
        return creatMutStr;
    }

    public void setCreatMutStr(float creatMutStr) {
        this.creatMutStr = creatMutStr;
    }

    public int getTerrainSize() {
        return terrainSize;
    }

    public void setTerrainSize(int size) {
        this.terrainSize = size;
    }

    public IFitness getFitnessFunction() {
        return fitnessFunction;
    }

    public void setFitnessFunction(IFitness fitness) {
        this.fitnessFunction = fitness;
    }

    public IRecombiner getRecombiner() {
        return recombiner;
    }

    public void setRecombiner(IRecombiner recombiner) {
        this.recombiner = recombiner;
    }

    public IMutator getMutator() {
        return mutator;
    }

    public void setMutator(IMutator mutator) {
        this.mutator = mutator;
    }

    public ISelector getSelector() {
        return selector;
    }

    public void setSelector(ISelector selector) {
        this.selector = selector;
    }

    public AbstractIndividual getIndividualType() {
        return individualType;
    }

    public void setIndividualType(AbstractIndividual individualType) {
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

    public int getSelectionSize() {
        return selectionSize;
    }

    public void setSelectionSize(int selection) {
        this.selectionSize = selection;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(populationSize, "popSize", 25);
        out.write(jointInitMax, "jointInitMax", 4);
        out.write(selectionSize, "selection", 10);
        out.write(kids, "kids", 5);
        out.write(evalTime, "evalTime", 150f);
        out.write(jointMaxPerIndividual, "jointMaxPerIndividual", 10);
        out.write(jointMinPerIndividual, "jointMinPerIndividual", 2);
        out.write(maxCreatureMutations, "maxCreatureMutations", 3);
        out.write(maxTerrainMutations, "maxTerrainMutations", 1);
        out.write(terrainMutStr, "terrainMutStr", 0.7f);
        out.write(creatMutStr, "creatureMutStr", 0.9f);
        out.write(maxHeight, "maxHeight", 4.5f);
        out.write(deleteThreshold, "deleteThreshold", -5f);
        out.write(name, "name", "Default");
        out.write(individualType.getType(), "individualType", "BOX");
        out.write(recombiner.getInfo(), "recombiner", "BOX");
        out.write(mutator.getInfo(), "mutator", "BOX");
        out.write(selector.getInfo(), "selector", "BOX");
        out.write(fitnessFunction, "fitnessFunc", new TargetFitness());
        out.write(color, "color", "MINT");
        out.write(terrainSize, "terrSize", 129);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        jointInitMax = in.readInt("jointInitMax", 4);
        populationSize = in.readInt("popSize", 25);
        selectionSize = in.readInt("selection", 10);
        kids = in.readInt("kids", 5);
        evalTime = in.readFloat("evalTime", 50f);
        jointMaxPerIndividual = in.readInt("jointMaxPerIndividual", 10);
        jointMinPerIndividual = in.readInt("jointMinPerIndividual", 2);
        maxCreatureMutations = in.readInt("maxCreatureMutations", 3);
        maxTerrainMutations = in.readInt("maxTerrainMutations", 1);
        terrainMutStr = in.readFloat("terrainMutStr", 0.7f);
        maxHeight = in.readFloat("maxHeight", 4.5f);
        deleteThreshold = in.readFloat("deleteThreshold", -5f);
        name = in.readString("name", "Default");
        color = in.readString("color", "MINT");
        terrainSize = in.readInt("terrSize", 129);
        fitnessFunction = (IFitness) in.readSavable("fitnessFunc", new TargetFitness());
        String individualTypeS = in.readString("individualType", "SPHERE");
        String recombinerS = in.readString("recombiner", "NON");
        String mutatorS = in.readString("mutator", "BOX");
        String selectorS = in.readString("selector", "ALL");
        creatMutStr = in.readFloat("creatureMutStr", 0.9f);
        selector = ConfigCreator.getSelector(selectorS);
        recombiner = ConfigCreator.getRecombiner(recombinerS);
        mutator = ConfigCreator.getMutator(mutatorS);
        individualType = ConfigCreator.getIndividual(individualTypeS);

        logger.log(Level.INFO, "Selector used: {0}", selector.getClass().getName());
        logger.log(Level.INFO, "Individual used: {0}", individualType.getClass().getName());
        logger.log(Level.INFO, "Mutator used: {0}", mutator.getClass().getName());
        logger.log(Level.INFO, "Recombiner used: {0}", recombiner.getClass().getName());

    }
}
