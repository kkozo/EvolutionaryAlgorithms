package evolution;

/** TODO: Make obsolete and replace with GUI.
 * Logger for each generation.
 * @author Andi
 */
public class EvaluationLogger {

    public static int BOXES_MUTATED = 0;
    public static int JOINTS_MUTATED = 0;
    public static int BEST_INDIVIDUAL = -1;
    public static float BEST_FITNESS = -1f;

    public static float FITNESS_AVERAGE = 0f;

    public static void flushLog() {
        System.out.println("BEST FITNESS: " + BEST_FITNESS);
        System.out.println("BEST INDIVIDUAL: " + BEST_INDIVIDUAL);
        System.out.println("POPULATION AVERAGE: " + (FITNESS_AVERAGE / (float) EvolutionConstants.POPULATION_SIZE));
//        System.out.println("BOXES MUTATED IN LAST EVALUATION: " + BOXES_MUTATED);
//        System.out.println("JOINT MUTATED IN LAST EVALUATION: " + JOINTS_MUTATED);
        resetLog();
    }

    private static void resetLog() {
        BOXES_MUTATED = 0;
        JOINTS_MUTATED = 0;
        BEST_INDIVIDUAL = -1;
        FITNESS_AVERAGE = 0;
    }
}
