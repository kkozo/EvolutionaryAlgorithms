
package evolution;

import evolution.individual.box.BoxIndividual;
import evolution.individual.sphere.SphereIndividual;
import java.util.HashMap;
import java.util.Map;

/** TODO: Make obsolete and replace with EvolutionConfig
 * Constants for the evolution.
 * 
 * @author Andi
 */
public class EvolutionConstants {

    public static int JOINT_INIT_MAX = 4;
    public static int POPULATION_SIZE = 150;
    public static int SELECTION = 30;
    public static int KIDS = 5;
    public static float EVAL_TIME = 50f;
    public static float DENSITY = 5.5f;
    public static int JOINT_MAX_PER_INDIVIDUAL = 10;
    public static int JOINT_MIN_PER_INDIVIDUAL = 2;
    public static int MAX_CREATURE_MUTATIONS = 3;
    public static int MAX_TERRAIN_MUTATIONS = 1;
    public static float TERRAIN_MUT_STR = 0.7f;
    public static float MAX_HEIGHT = 4.5f;
    public static float DELETE_THRESHOLD = -5f;
    public static final Map<String, Class<?>> possibleIndividuals = new HashMap<>();

    static {
        BoxIndividual box = new BoxIndividual();
        possibleIndividuals.put(box.getType(), BoxIndividual.class);
        SphereIndividual sphere = new SphereIndividual();
        possibleIndividuals.put(sphere.getType(), SphereIndividual.class);
    }
}
