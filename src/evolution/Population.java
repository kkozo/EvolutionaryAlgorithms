package evolution;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import evolution.individual.AbstractIndividual;
import evolution.modifiers.IMutator;
import evolution.modifiers.IRecombiner;
import evolution.modifiers.ISelector;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Population of an Individual. AbstractIndividual has to be extended.
 * Selector and Recombiner have to be implemented
 * @author Andi
 */
public class Population<T extends AbstractIndividual> implements Savable {

    private Class<T> type;
    private int size;
    private ArrayList<T> individuals;
    public static int number = 0;
    private int generation = 0;
    private ISelector selector;
    private IRecombiner<T> recombiner;
    private IMutator<T> mutator;

    public Population(int size, ArrayList<T> individuals) {
        this.size = size;
        this.individuals = individuals;
    }

    public Population() {
        individuals = new ArrayList<>();
    }

    public Population(Class<T> clazz) {
        individuals = new ArrayList<>();
        type = clazz;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<T> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(ArrayList<T> individuals) {
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
        out.write(individuals.get(0).getType(), "type", "");
        out.writeSavableArrayList(individuals, "individuals", new ArrayList<>());
        out.write(size, "popSize", EvolutionConstants.POPULATION_SIZE);
        out.write(generation, "gen", 0);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        number = in.readInt("nr", 0);
        String type = in.readString("type", "");

        individuals = in.readSavableArrayList("individuals", new ArrayList<>());
        size = in.readInt("popSize", EvolutionConstants.POPULATION_SIZE);
        generation = in.readInt("gen", 0);

    }

    public Class<T> getIndividualType() {
        return type;
    }

    public void setIndividualType(Class<T> clazz) {
        this.type = clazz;
    }

    public ISelector getSelector() {
        return selector;
    }

    public void setSelector(ISelector selector) {
        this.selector = selector;
    }

    public IRecombiner<T> getRecombiner() {
        return recombiner;
    }

    public void setRecombiner(IRecombiner<T> recombiner) {
        this.recombiner = recombiner;
    }

    public IMutator<T> getMutator() {
        return mutator;
    }

    public void setMutator(IMutator<T> mutator) {
        this.mutator = mutator;
    }
}
