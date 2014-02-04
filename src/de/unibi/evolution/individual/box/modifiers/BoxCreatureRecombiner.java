package de.unibi.evolution.individual.box.modifiers;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import de.unibi.config.EvolutionConfig;
import de.unibi.util.Mutations;
import de.unibi.evolution.Population;
import de.unibi.evolution.individual.box.BoxIndividual;
import java.util.ArrayList;
import java.util.List;
import de.unibi.evolution.individual.box.bodytypes.BoxBody;
import de.unibi.evolution.individual.box.bodytypes.Joint;
import de.unibi.evolution.modifiers.IRecombiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Swaps joints for 2 creatures and returns a list of kids
 *
 * @author Andi
 */
public class BoxCreatureRecombiner implements IRecombiner<BoxIndividual> {

    public final static Logger logger = Logger.getLogger(BoxCreatureRecombiner.class.getName());

    @Override
    public List<BoxIndividual> recombine(List<BoxIndividual> individuals, EvolutionConfig config) {
        ArrayList<BoxIndividual> recombinants = new ArrayList<>();
        int size = config.getKids();
        for (int i = 0; i < size; ++i) {
            int firstIndivIndex = FastMath.nextRandomInt(0, individuals.size() - 1);
            int secIndivIndex = FastMath.nextRandomInt(0, individuals.size() - 1);
            BoxIndividual firstIndividual = individuals.get(firstIndivIndex).clone();
            BoxIndividual secIndividual = individuals.get(secIndivIndex).clone();
            int firstJoint = FastMath.nextRandomInt(0, firstIndividual.getCreature().getJoints() - 1);
            int secJoint = FastMath.nextRandomInt(0, secIndividual.getCreature().getJoints() - 1);
            Joint jointOne = Mutations.getJoint((BoxBody) firstIndividual.getCreature().getRoot(), firstJoint);
            Joint jointTwo = Mutations.getJoint((BoxBody) secIndividual.getCreature().getRoot(), secJoint);

            BoxBody oneBox = (BoxBody) jointOne.getChild();
            BoxBody secBox = (BoxBody) jointTwo.getChild();

            Vector3f newSize = oneBox.getSize().add(secBox.getSize()).divide(2).normalize();
            Vector3f newOrigin = oneBox.getOrigin().add(secBox.getOrigin()).divide(2);

            oneBox.setOrigin(newOrigin);
            oneBox.setSize(newSize);
            jointOne.setChild(oneBox);
            jointOne.setForces((jointOne.getForces().add(jointTwo.getForces())).divide(2));
            jointOne.setAttachPoint((jointOne.getAttachPoint().add(jointTwo.getAttachPoint())).divide(2));
            ++Population.number;
            logger.log(Level.INFO, "KID {0} created out of {1} and {2}", new Object[]{Population.number, firstIndividual.getId(), secIndividual.getId()});
            firstIndividual.setId(Population.number);
            recombinants.add(firstIndividual);
        }
        return recombinants;
    }

    @Override
    public String getInfo() {
        return "BOX";
    }
}
