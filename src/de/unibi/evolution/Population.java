package de.unibi.evolution;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import de.unibi.config.EvolutionConfig;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Population of an Individual. AbstractIndividual has to be extended. Selector
 * and Recombiner have to be implemented
 *
 * @author Andi
 */
public class Population<AbstractIndividual> implements Savable {

    private ArrayList<AbstractIndividual> individuals;
    public static int number = 0;
    private int generation = 0;
    private EvolutionConfig config;

    public Population(ArrayList<AbstractIndividual> individuals) {
        this.individuals = individuals;
    }

    public Population() {
        individuals = new ArrayList<>();
    }

    public EvolutionConfig getConfig() {
        return config;
    }

    public void setConfig(EvolutionConfig config) {
        this.config = config;
    }

    public ArrayList<AbstractIndividual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<AbstractIndividual> individuals) {
        this.individuals = individuals;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule out = ex.getCapsule(this);
        out.write(number, "nr", 0);
        out.writeSavableArrayList(individuals, "individuals", new ArrayList<>());
        out.write(generation, "gen", 0);
        out.write(config, "config", new EvolutionConfig());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        number = in.readInt("nr", 0);
        individuals = in.readSavableArrayList("individuals", new ArrayList<>());
        generation = in.readInt("gen", 0);
        config = (EvolutionConfig) in.readSavable("config", new EvolutionConfig());
    }
}
