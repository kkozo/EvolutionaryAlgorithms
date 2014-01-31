package de.unibi.evoalgo;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsView;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.export.binary.BinaryImporter;

import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.shadow.PointLightShadowFilter;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.system.AppSettings;
import de.unibi.config.ConfigCreator;
import de.unibi.config.EvolutionConfig;
import de.unibi.evolution.EvolutionController;
import de.unibi.evolution.Population;
import de.unibi.gui.MainMenu;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.unibi.listener.MainControlsListener;
import de.unibi.util.Assets;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Start Program
 *
 * @author Andi
 */
public class EvoAlgoStart extends SimpleApplication {

    private static final Logger logger = Logger.getLogger(EvoAlgoStart.class.getName());
    public static int creatureID = 0;
    private static EvolutionController evolutionController;
    private StatsView statsView;

    public static void main(String args[]) {
        try {
            EvoAlgoStart app = new EvoAlgoStart();

            File file = new File("settings.cfg");
            try {
                if (file.exists()) {
                    AppSettings jmeSettings = new AppSettings(true);
                    jmeSettings.load(new FileInputStream(file));
                    app.setShowSettings(false);
                    app.setSettings(jmeSettings);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            app.start();
        } catch (Exception e) {
            e.printStackTrace();
            if (evolutionController != null) {
                System.out.println("EMERGENCY SAVE");
                evolutionController.save();
            }
        }
    }
    /**
     * Physics Application
     */
    private BulletAppState bulletAppState;

    @Override
    public void simpleInitApp() {
        PointLight light = new PointLight();
        light.setPosition(new Vector3f(0, -50, 0));
        light.setRadius(150f);
        rootNode.addLight(light);


        File file = new File("settings.cfg");
        getContext().getSettings().setTitle("Terrain Evolution");
        try {
            if (!file.exists()) {
                try {
                    getContext().getSettings().save(new FileOutputStream(file));
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "Error: Failed to save settings!", ex);
                }
            } else {
                getContext().getSettings().load(new FileInputStream(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * Set up Physics
         */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        assetManager.registerLocator("assets", FileLocator.class);
        /**
         * Load assets
         */
        this.setDisplayStatView(false);
        Assets.loadAssets(assetManager);
        /**
         * Configure cam to look at scene
         */
        cam.setLocation(new Vector3f(0, 6f, 6f));
        cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);

        new MainMenu(this);
//        
//        bulletAppState.getPhysicsSpace().setAccuracy(1f/60f);
    }

    protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");        // fake crosshairs :)
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
                settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
        statsView = new StatsView("Statistics View", assetManager, renderer.getStatistics());
//         move it up so it appears above fps text
        statsView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        guiNode.attachChild(statsView);
        guiNode.attachChild(fpsText);
    }

    public Population loadPop(String filename) {

        BinaryImporter imp = BinaryImporter.getInstance();
        imp.setAssetManager(assetManager);
        Population pop;
        try {
            pop = (Population) imp.load(new File(filename));
            return pop;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(EvoAlgoStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void startEvo(Population pop) throws Exception {
        this.setDisplayStatView(true);

        BitmapText currentIndividualNode = new BitmapText(guiFont, false);
        currentIndividualNode.setSize(guiFont.getCharSet().getRenderedSize());
        currentIndividualNode.setText("Current Individual: -1");
        currentIndividualNode.setLocalTranslation(400, currentIndividualNode.getLineHeight(), 0);
//  start the screen
        initCrossHairs();
        guiNode.attachChild(currentIndividualNode);
//        Population pop = loadPop(loadFilename);

        if (pop == null) {
            String typeString = "SPHERE";

            EvolutionConfig config = ConfigCreator.createConfig(typeString, "ALL", "BOX", "NON", "TARGET");

            Population genericPop = new Population<>();

            for (int i = 0; i < config.getPopulationSize(); ++i) {
                genericPop.getIndividuals().add(config.getIndividualType().createRandomIndividual(genericPop.getConfig()));
            }
            genericPop.setConfig(config);
            this.evolutionController = new EvolutionController(rootNode, bulletAppState, currentIndividualNode, cam, genericPop);
        } else {
            this.evolutionController = new EvolutionController(rootNode, bulletAppState, currentIndividualNode, cam, pop);
        }
        flyCam.setDragToRotate(false);
        MainControlsListener iL = new MainControlsListener(bulletAppState, evolutionController);
        inputManager.addMapping("reset",
                new KeyTrigger(KeyInput.KEY_NUMPAD0));
        inputManager.addMapping("speeddown",
                new KeyTrigger(KeyInput.KEY_SUBTRACT));
        inputManager.addMapping("speedup",
                new KeyTrigger(KeyInput.KEY_ADD));
        inputManager.addMapping("start",
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("del",
                new KeyTrigger(KeyInput.KEY_DELETE));
        inputManager.addMapping("focus",
                new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping("save",
                new KeyTrigger(KeyInput.KEY_F10));
        inputManager.addListener(iL, "reset", "speedup", "speeddown", "start", "del", "focus", "save");

        /**
         * Initialize the scene, materials, and physics space
         */
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        bulletAppState.setSpeed(0f);
        rootNode.addControl(evolutionController);
        flyCam.setMoveSpeed(250f);
        setPauseOnLostFocus(false);
//        Arrow bla = new Arrow(new Vector3f(0, 3, 0));
//        Geometry arrowGeom = new Geometry("ARROW", bla);
//        arrowGeom.setLocalTranslation(Vector3f.ZERO.add(new Vector3f(0, -15, 0)));
////        arrowGeom.lookAt(arrowGeom.localToWorld(new Vector3f(0, 20, 0).normalize(), new Vector3f()), Vector3f.UNIT_X);
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.getAdditionalRenderState().setWireframe(true);
//        mat.setColor("Color", ColorRGBA.Red);
//        arrowGeom.setMaterial(mat);
//        rootNode.attachChild(arrowGeom);
    }
}