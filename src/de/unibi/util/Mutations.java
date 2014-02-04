package de.unibi.util;

import de.unibi.evolution.individual.box.BoxIndividual;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.TerrainQuad;
import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.EvaluationLogger;
import de.unibi.evolution.individual.AbstractIndividual;
import de.unibi.evolution.individual.box.BoxCreature;
import java.util.ArrayList;
import java.util.List;
import de.unibi.evolution.individual.box.bodytypes.BoxBody;
import de.unibi.evolution.individual.box.bodytypes.Joint;
import de.unibi.evolution.individual.box.bodytypes.JointTypes;
import de.unibi.evolution.nodes.TNode;

/**
 * Provides a collection of mutations for other classes to use.
 *
 * @author Andi
 */
public class Mutations {

    private static java.util.Random random;
    private static long randomSeed;
    private final static float MUT_DEVIATION = 0.1f;
    private static float BOX_MUTATION_CONSTANT = 0.95f;
    private static float JOINT_MUTATION_CONSTANT = 0.95f;
    private static int mutationCount = 0;
    private static EvolutionConfig config = new EvolutionConfig();

    public static void mutateIndividual(AbstractIndividual indiv, EvolutionConfig cconfig) {
        config = cconfig;
        mutationCount = 0;
        if (indiv instanceof BoxIndividual) {
            mutationWalker(((BoxCreature) indiv.getCreature()).getRoot(), (BoxIndividual) indiv);
        }
        indiv.setTerrain((TerrainQuad) mutateTerrain(indiv.getTerrain(), config.getMaxTerrainMutations(), config.getTerrainMutStr()).deepClone());
    }

    public static TerrainQuad mutateTerrain(TerrainQuad terrain, int max, float strength) {
        for (int i = 0; i < max; ++i) {
            float p = FastMath.nextRandomFloat();
            if (p > strength) {
                float halfsize = terrain.getTerrainSize()/2;
                float locx = (FastMath.nextRandomFloat() * halfsize * 2) - halfsize;
                float locz = (FastMath.nextRandomFloat() * halfsize* 2) - halfsize;
                float radius = FastMath.nextRandomFloat() * terrain.getTerrainSize()/6 + terrain.getTerrainSize()/14;
                float str = FastMath.nextRandomFloat() * (config.getMaxHeight() * 2) - config.getMaxHeight();
                adjustHeight(new Vector3f(locx, 0, locz), radius, str, terrain);
            }
        }

        return terrain;
    }

    private static void mutationWalker(TNode tnode, BoxIndividual individual) {

        float pp = FastMath.nextRandomFloat();
        if (pp > BOX_MUTATION_CONSTANT) {

            mutateBox((BoxBody) tnode, ((BoxCreature) individual.getCreature()).getJoints());
            EvaluationLogger.BOXES_MUTATED++;
            mutationCount++;
        }
        if (mutationCount > config.getMaxCreatureMutations()) {
            return;
        }
        float p = FastMath.nextRandomFloat();
        if (p > JOINT_MUTATION_CONSTANT) {
            int whichMut = FastMath.nextRandomInt(0, 1);
            int jointmax = ((BoxCreature) individual.getCreature()).getJoints();
            if (whichMut == 0 && jointmax > 1) {
                int firstJoint = FastMath.nextRandomInt(0, jointmax - 1);
                int secJoint = FastMath.nextRandomInt(0, jointmax - 1);
                Joint one = getJoint((BoxBody) ((BoxCreature) individual.getCreature()).getRoot(), firstJoint);
                Joint two = getJoint((BoxBody) ((BoxCreature) individual.getCreature()).getRoot(), secJoint);
                BoxBody temp = (BoxBody) one.getChild();
                one.setChild(two.getChild());
                two.setChild(temp);
                mutationCount++;
                EvaluationLogger.JOINTS_MUTATED++;
            } else {
                if (!tnode.getChildren().isEmpty()) {
                    int whichJoint = FastMath.nextRandomInt(0, tnode.getChildren().size() - 1);
                    mutateJoint(tnode.getChildren().get(whichJoint));
                    EvaluationLogger.JOINTS_MUTATED++;
                    mutationCount++;
                }
            }
        }
        // traverse further
        for (Joint e : tnode.getChildren()) {
            TNode eNode = e.getChild();
            BoxBody eff = (BoxBody) eNode;
            if (mutationCount > config.getMaxCreatureMutations()) {
                return;
            }
            mutationWalker(eff, individual);
        }


    }

    public static Joint getJoint(BoxBody box, int i) {
        for (Joint e : box.getChildren()) {
            if (i == 0) {
                return e;
            } else {
                return jointWalker(e, --i);
            }

        }
        return box.getChildren().get(0);


    }

    private static Joint jointWalker(Joint joint, int i) {
        if (i == 0) {
            return joint;
        } else {
            if (!joint.getChild().getChildren().isEmpty()) {
                for (Joint e : joint.getChild().getChildren()) {
                    return jointWalker(e, --i);


                }
            }
        }
        return joint;
    }

    public static Vector3f mutateUnitVector(Vector3f oldr, float strength) {
        Vector3f old = new Vector3f(oldr);
        old.x = old.x + gaussianFloat(strength);
        old.y = old.y + gaussianFloat(strength);
        old.z = old.z + gaussianFloat(strength);
        if (old.x > 1f) {
            old.x = 1;
        } else if (old.x < -1f) {
            old.x = -1f;
        }
        if (old.y > 1f) {
            old.y = 1;
        } else if (old.y < -1f) {
            old.y = -1f;
        }
        if (old.z > 1f) {
            old.z = 1;
        } else if (old.z < -1f) {
            old.z = -1f;
        }
        return old;

    }

    public static Vector3f mutateSize(Vector3f old, float strength) {
        old.x = old.x + gaussianFloat(strength);
        old.y = old.y + gaussianFloat(strength);
        old.z = old.z + gaussianFloat(strength);
        if (old.x > 0.75f) {
            old.x = 0.75f;
        } else if (old.x < 0.1f) {
            old.x = 0.1f;
        }
        if (old.y > 0.75f) {
            old.y = 0.75f;
        } else if (old.y < 0.1f) {
            old.y = 0.1f;
        }
        if (old.z > 0.75f) {
            old.z = 0.75f;
        } else if (old.z < 0.1f) {
            old.z = 0.1f;
        }
        return old.normalize();
    }

    public static void mutateJoint(Joint j) {
        int jointType = FastMath.nextRandomInt(JointTypes.staticJoint.ordinal(), JointTypes.continuousJoint.ordinal());
        j.setJointType(JointTypes.values()[jointType]);
        j.setForces(mutateUnitVector(j.getForces(), MUT_DEVIATION));
        j.setAttachPoint(mutateUnitVector(j.getAttachPoint(), MUT_DEVIATION));
    }

    public static void mutateBox(BoxBody b, int nrOfJoints) {
        float whichMut = FastMath.nextRandomFloat();
        BoxIndividual creator = new BoxIndividual();
        if (whichMut > 0.95f) {
            int delOrAdd = FastMath.nextRandomInt(0, 1);
            if (delOrAdd == 0) {
                if (nrOfJoints < config.getJointMaxPerIndividual()) { //add a joint if nr is smaller
                    Joint myJoint = creator.createJoint();
                    BoxBody myBody = creator.createBoxBody(1);
                    myJoint.setChild(myBody);
                    b.getChildren().add(myJoint);
                } else {
                }
            } else if (nrOfJoints > config.getJointMinPerIndividual()) { // delete a joint if not at minimum
                if (b.getChildren().size() > 1) {
                    int whichJoint = FastMath.nextRandomInt(0, b.getChildren().size() - 1);
                    b.getChildren().remove(whichJoint);
                }

            }
        }
        b.setSize(mutateSize(b.getSize(), MUT_DEVIATION));
        b.setOrigin(mutateUnitVector(b.getOrigin(), MUT_DEVIATION));
    }

    static {
        randomSeed = System.currentTimeMillis();
        random = new java.util.Random(randomSeed);
    }

    public static float gaussianFloat(float dev) {
        return (float) random.nextGaussian() * dev;
    }

    public static Vector3f createRandomVector() {
        return new Vector3f(FastMath.nextRandomFloat() * 2 - 1, FastMath.nextRandomFloat() * 2 - 1, FastMath.nextRandomFloat() * 2 - 1);
    }

    public static void main(String[] args) {

        System.out.println("RANDOM JOINT TYPE TEST");
        int[] randoms = new int[3];
        for (int i = 0; i < 3500; ++i) {
            randoms[FastMath.nextRandomInt(JointTypes.staticJoint.ordinal(), JointTypes.continuousJoint.ordinal())]++;
        }
        System.out.println("Static Joints: " + randoms[JointTypes.staticJoint.ordinal()]);
        System.out.println("Continous Joints: " + randoms[JointTypes.continuousJoint.ordinal()]);
        System.out.println("Reactive Joints: " + randoms[JointTypes.reactiveJoint.ordinal()]);
        System.out.println("");
        System.out.println("RANDOM GAUSS TEST");
        float deviation = 0f;
        for (int i = 0; i < 5000; ++i) {
            float temp = gaussianFloat(MUT_DEVIATION);
            deviation += temp;
        }
        BoxIndividual indiv = (BoxIndividual) new BoxIndividual().createRandomIndividual(new EvolutionConfig());
        int jointmax = ((BoxCreature) indiv.getCreature()).getJoints();
        int[] jointCounts = new int[jointmax];
        for (int i = 0; i < 10000000; ++i) {
            if (jointmax > 1) {
                int firstJoint = FastMath.nextRandomInt(0, jointmax - 1);
                int secJoint = FastMath.nextRandomInt(0, jointmax - 1);
                jointCounts[firstJoint]++;
                jointCounts[secJoint]++;
            }
        }
        for (int i = 0; i < jointCounts.length; ++i) {
            System.out.println(i + ", " + jointCounts[i]);
        }
        System.out.println("AVERAGE = " + deviation);
        System.out.println("");
        System.out.println("RANDOM VECTOR CHANGES TEST");
        float distance = 0f;
        for (int i = 0; i < 5000; ++i) {
            Vector3f testVector = Mutations.createRandomVector();
            distance += testVector.distance(Mutations.mutateUnitVector(testVector, MUT_DEVIATION));
        }
        System.out.println(distance / 5000f);
        System.out.println("AVERAGE OF BOX SIZES");
        List<Vector3f> vectorList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            vectorList.add(createRandomVector());
        }
        for (int i = 0; i < 10000; ++i) {
            for (Vector3f e : vectorList) {
                e = (mutateSize(e, MUT_DEVIATION));
            }
        }
        Vector3f zero = Vector3f.ZERO;
        for (Vector3f e : vectorList) {
            zero = zero.add(e);
        }
        System.out.println(zero.divide(10000));
    }

    public static int getJoints(TNode node) {
        int joint = 0;

        for (Joint e : node.getChildren()) {
            joint++;
            joint += getJoints(e.getChild());
        }

        return joint;
    }

    public static void adjustHeight(Vector3f loc, float radius, float height, TerrainQuad terrain) {
        int radiusStepsX = (int) (radius / terrain.getLocalScale().x);
        int radiusStepsZ = (int) (radius / terrain.getLocalScale().z);

        float xStepAmount = terrain.getLocalScale().x;
        float zStepAmount = terrain.getLocalScale().z;
        List<Vector2f> locs = new ArrayList<>();
        List<Float> heights = new ArrayList<>();

        for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
            for (int x = -radiusStepsX; x < radiusStepsX; x++) {

                float locX = loc.x + (x * xStepAmount);
                float locZ = loc.z + (z * zStepAmount);

                if (isInRadius(locX - loc.x, locZ - loc.z, radius)) {
                    // see if it is in the radius of the tool
                    float h = calculateHeight(radius, height, locX - loc.x, locZ - loc.z);
                    Vector2f terrainLoc = new Vector2f(locX, locZ);
                    locs.add(terrainLoc);

                    heights.add(h);
                }
            }
        }
        terrain.adjustHeight(locs, heights);

        terrain.updateModelBound();
    }

    private static void smoothAll(TerrainQuad terrain, float radius) {

        int radiusStepsX = (int) (radius / (terrain).getLocalScale().x);
        int radiusStepsZ = (int) (radius / (terrain).getLocalScale().z);

        float xStepAmount = terrain.getLocalScale().x;
        float zStepAmount = terrain.getLocalScale().z;

        List<Vector2f> locs = new ArrayList<>();
        List<Float> heights = new ArrayList<>();
        for (float f = -65f; f < 65; f = f + 1) {
            Vector3f worldLoc = new Vector3f(f, 0, 0);
            for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
                for (int x = -radiusStepsX; x < radiusStepsX; x++) {

                    float locX = worldLoc.x + (x * xStepAmount);
                    float locZ = worldLoc.z + (z * zStepAmount);

                    // see if it is in the radius of the tool
                    if (isInRadius(locX - worldLoc.x, locZ - worldLoc.z, radius)) {

                        Vector2f terrainLoc = new Vector2f(locX, locZ);
                        // adjust height based on radius of the tool
                        float center = terrain.getHeightmapHeight(terrainLoc);
                        float left = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x - 1, terrainLoc.y));
                        float right = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x + 1, terrainLoc.y));
                        float up = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x, terrainLoc.y + 1));
                        float down = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x, terrainLoc.y - 1));
                        int count = 1;
                        float amount = center;
                        if (left != Float.NaN) {
                            amount += left;
                            count++;
                        }
                        if (right != Float.NaN) {
                            amount += right;
                            count++;
                        }
                        if (up != Float.NaN) {
                            amount += up;
                            count++;
                        }
                        if (down != Float.NaN) {
                            amount += down;
                            count++;
                        }

                        amount /= count; // take average

                        // weigh it
                        float diff = amount - center;
                        diff *= 1.1f;

                        locs.add(terrainLoc);
                        heights.add(diff);
                    }
                }
            }
        }
        for (float f = -65f; f < 65; f = f + 1) {
            Vector3f worldLoc = new Vector3f(0, 0, f);
            for (int z = -radiusStepsZ; z < radiusStepsZ; z++) {
                for (int x = -radiusStepsX; x < radiusStepsX; x++) {

                    float locX = worldLoc.x + (x * xStepAmount);
                    float locZ = worldLoc.z + (z * zStepAmount);

                    // see if it is in the radius of the tool
                    if (isInRadius(locX - worldLoc.x, locZ - worldLoc.z, radius)) {

                        Vector2f terrainLoc = new Vector2f(locX, locZ);
                        // adjust height based on radius of the tool
                        float center = terrain.getHeightmapHeight(terrainLoc);
                        float left = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x - 1, terrainLoc.y));
                        float right = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x + 1, terrainLoc.y));
                        float up = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x, terrainLoc.y + 1));
                        float down = terrain.getHeightmapHeight(new Vector2f(terrainLoc.x, terrainLoc.y - 1));
                        int count = 1;
                        float amount = center;
                        if (left != Float.NaN) {
                            amount += left;
                            count++;
                        }
                        if (right != Float.NaN) {
                            amount += right;
                            count++;
                        }
                        if (up != Float.NaN) {
                            amount += up;
                            count++;
                        }
                        if (down != Float.NaN) {
                            amount += down;
                            count++;
                        }

                        amount /= count; // take average

                        // weigh it
                        float diff = amount - center;
                        diff *= 1.1f;

                        locs.add(terrainLoc);
                        heights.add(diff);
                    }
                }
            }
        }
        terrain.adjustHeight(locs, heights);

//        terrain.updateModelBound();

    }

    private static boolean isInRadius(float x, float y, float radius) {
        Vector2f point = new Vector2f(x, y);
        // return true if the distance is less than equal to the radius
        return point.length() <= radius;
    }

    private static float calculateHeight(float radius, float heightFactor, float x, float z) {
        // find percentage for each 'unit' in radius
        Vector2f point = new Vector2f(x, z);
        float val = point.length() / radius;
        val = 1 - val;
        if (val <= 0) {
            val = 0;
        }
        return heightFactor * val;
    }
}
