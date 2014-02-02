package de.unibi.gui.screencontroller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.unibi.config.ConfigCreator;
import de.unibi.config.EvolutionConfig;
import de.unibi.evoalgo.EvoAlgoStart;
import de.lessvoid.nifty.tools.Color;
import de.unibi.evolution.Population;

/**
 *
 * @author Andi
 */
public class ConfigController implements ScreenController {

    private Color redColor = new Color(1, 0, 0, 1);
    private Color greenColor = new Color(0, 1, 0, 1);
    private Nifty nifty;
    private Screen screen;
    private EvoAlgoStart app;
    private TextField popTextField;
    private TextField selTextField;
    private TextField kidsTextField;
    private TextField tfevalTextField;
    private TextField maxmutTextField;
    private TextField maxterrmutTextField;
    private TextField mutStrTerrTextField;
    private TextField mutStrCreatTextField;
    private TextField delThreshTextField;
    private TextField nameTextField;
    private Label errorField;
    private DropDown dropDownIndividual;
    private DropDown dropDownFitness;
    private DropDown dropDownTerrainColor;
    private DropDown dropDownTerrainSize;
    private DropDown dropDownMutator;
    private DropDown dropDownSelector;
    private DropDown dropDownRecombiner;
    private Label popLabel;
    private Label sellabel;
    private Label kidslabel;
    private Label evallabel;
    private Label maxmutlabel;
    private Label maxTerrlabel;
    private Label terrMutStrlabel;
    private Label creatMutStrlabel;
    private Label ldelThreshold;
    private Label lname;
    private Label lcreatures;
    private Label lterrainColor;
    private Label lTerrainSize;
    private Label lfitness;
    private Label lmutation;
    private Label lrecombination;
    private Label lselection;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;

        this.screen = screen;
        this.app = ((MainMenuController) nifty.getScreen("MainScreen").getScreenController()).getApp();
    }

    public void setApp(final EvoAlgoStart app) {
        this.app = app;
    }

    @Override
    public void onStartScreen() {

        dropDownIndividual = screen.findNiftyControl("Creatures", DropDown.class);
        dropDownIndividual.clear();
        dropDownIndividual.addAllItems(ConfigCreator.getAllCreatures());

        dropDownFitness = screen.findNiftyControl("fitness", DropDown.class);
        dropDownFitness.clear();
        dropDownFitness.addAllItems(ConfigCreator.getAllFitness());

        dropDownTerrainColor = screen.findNiftyControl("TerrainColor", DropDown.class);
        dropDownTerrainColor.clear();
        dropDownTerrainColor.addItem("MINT");
        dropDownTerrainColor.addItem("GREEN");

        dropDownTerrainSize = screen.findNiftyControl("TerrainSize", DropDown.class);
        dropDownTerrainSize.clear();
        dropDownTerrainSize.addItem("129");
        dropDownTerrainSize.addItem("257");
        dropDownTerrainSize.addItem("513");

        dropDownMutator = screen.findNiftyControl("mutation", DropDown.class);
        dropDownMutator.clear();
        dropDownMutator.addAllItems(ConfigCreator.getAllMutator());

        dropDownSelector = screen.findNiftyControl("selection", DropDown.class);
        dropDownSelector.clear();
        dropDownSelector.addAllItems(ConfigCreator.getAllSelector());

        dropDownRecombiner = screen.findNiftyControl("recombination", DropDown.class);
        dropDownRecombiner.clear();
        dropDownRecombiner.addAllItems(ConfigCreator.getAllRecombiner());

        EvolutionConfig config = ConfigCreator.createConfig((String) dropDownIndividual.getSelection(), (String) dropDownSelector.getSelection(), (String) dropDownMutator.getSelection(), (String) dropDownRecombiner.getSelection(), (String) dropDownFitness.getSelection());

        popTextField = screen.findNiftyControl("tfpop", TextField.class);
        popTextField.setText(config.getPopulationSize() + "");

        selTextField = screen.findNiftyControl("tfsel", TextField.class);
        selTextField.setText(config.getSelectionSize() + "");

        kidsTextField = screen.findNiftyControl("tfkids", TextField.class);
        kidsTextField.setText(config.getKids() + "");

        tfevalTextField = screen.findNiftyControl("tfeval", TextField.class);
        tfevalTextField.setText(config.getEvalTime() + "");

        maxmutTextField = screen.findNiftyControl("tfmaxmut", TextField.class);
        maxmutTextField.setText(config.getMaxCreatureMutations() + "");

        maxterrmutTextField = screen.findNiftyControl("tfmaxterr", TextField.class);
        maxterrmutTextField.setText(config.getMaxTerrainMutations() + "");

        mutStrTerrTextField = screen.findNiftyControl("tfMutStrTerr", TextField.class);
        mutStrTerrTextField.setText(config.getTerrainMutStr() + "");

        mutStrCreatTextField = screen.findNiftyControl("tfMutStrCreat", TextField.class);
        mutStrCreatTextField.setText(config.getCreatMutStr() + "");

        delThreshTextField = screen.findNiftyControl("delThreshold", TextField.class);
        delThreshTextField.setText(config.getDeleteThreshold() + "");

        nameTextField = screen.findNiftyControl("name", TextField.class);
        nameTextField.setText(config.getName() + "");

        errorField = screen.findNiftyControl("Info", Label.class);
        errorField.setText("");

        popLabel = screen.findNiftyControl("poplabel", Label.class);
        sellabel = screen.findNiftyControl("sellabel", Label.class);
        kidslabel = screen.findNiftyControl("kidslabel", Label.class);
        evallabel = screen.findNiftyControl("evallabel", Label.class);
        maxmutlabel = screen.findNiftyControl("maxmutlabel", Label.class);
        maxTerrlabel = screen.findNiftyControl("maxTerrlabel", Label.class);
        terrMutStrlabel = screen.findNiftyControl("terrMutStrlabel", Label.class);
        creatMutStrlabel = screen.findNiftyControl("creatMutStrlabel", Label.class);
        ldelThreshold = screen.findNiftyControl("ldelThreshold", Label.class);
        lname = screen.findNiftyControl("lname", Label.class);
        lcreatures = screen.findNiftyControl("lcreatures", Label.class);
        lterrainColor = screen.findNiftyControl("lterrainColor", Label.class);
        lTerrainSize = screen.findNiftyControl("lTerrainSize", Label.class);
        lfitness = screen.findNiftyControl("lfitness", Label.class);
        lmutation = screen.findNiftyControl("lmutation", Label.class);
        lrecombination = screen.findNiftyControl("lrecombination", Label.class);
        lselection = screen.findNiftyControl("lselection", Label.class);
    }

    @Override
    public void onEndScreen() {
    }

    public void accept() {
        boolean works = true;
        EvolutionConfig config = ConfigCreator.createConfig((String) dropDownIndividual.getSelection(),
                (String) dropDownSelector.getSelection(),
                (String) dropDownMutator.getSelection(),
                (String) dropDownRecombiner.getSelection(),
                (String) dropDownFitness.getSelection());
        /*
         * PopSize
         */
        try {
            int popSize = Integer.parseInt(popTextField.getRealText());
            config.setPopulationSize(popSize);
            setColorOfLabel(popLabel, greenColor);
            if (popSize < 1) {
                setColorOfLabel(popLabel, redColor);
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(popLabel, redColor);
        }
        try {
            int selSize = Integer.parseInt(selTextField.getRealText());
            config.setSelectionSize(selSize);
            setColorOfLabel(sellabel, greenColor);
            if (selSize < 1) {
                setColorOfLabel(sellabel, redColor);
                works = false;
            }

        } catch (Exception e) {
            works = false;
            setColorOfLabel(sellabel, redColor);
        }
        /*
         * Kids
         */
        try {
            int kidSize = Integer.parseInt(kidsTextField.getRealText());
            config.setKids(kidSize);
            setColorOfLabel(kidslabel, greenColor);
            if (kidSize < 0) {
                setColorOfLabel(kidslabel, redColor);
                works = false;
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(kidslabel, redColor);

        }
        /*
         * evaltime
         */
        try {
            float evalTime = Float.parseFloat(tfevalTextField.getRealText());
            config.setEvalTime(evalTime);
            setColorOfLabel(evallabel, greenColor);
            if (evalTime < 1) {
                works = false;
                setColorOfLabel(evallabel, redColor);
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(evallabel, redColor);
        }
        /*
         * max creat mutations
         */
        try {
            int maxMut = Integer.parseInt(maxmutTextField.getRealText());
            config.setMaxCreatureMutations(maxMut);
            setColorOfLabel(maxmutlabel, greenColor);
            if (maxMut < 1) {
                setColorOfLabel(maxmutlabel, redColor);
                works = false;
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(maxmutlabel, redColor);
        }
        /*
         * max terr mutations
         */
        try {
            int maxMut = Integer.parseInt(maxterrmutTextField.getRealText());
            config.setMaxTerrainMutations(maxMut);
            setColorOfLabel(maxTerrlabel, greenColor);
            if (maxMut < 1) {
                setColorOfLabel(maxTerrlabel, redColor);
                works = false;
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(maxTerrlabel, redColor);
        }
        /*
         * terrMutStr
         */
        try {
            float terrMutStr = Float.parseFloat(mutStrTerrTextField.getRealText());
            config.setTerrainMutStr(terrMutStr);
            setColorOfLabel(terrMutStrlabel, greenColor);
            if (terrMutStr > 1 || terrMutStr < 0.01f) {
                works = false;
                setColorOfLabel(terrMutStrlabel, redColor);
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(terrMutStrlabel, redColor);
        }
        /*
         * creat mut str
         */
        try {
            float creatMutStr = Float.parseFloat(mutStrCreatTextField.getRealText());
            config.setCreatMutStr(creatMutStr);
            setColorOfLabel(creatMutStrlabel, greenColor);
            if (creatMutStr > 1 || creatMutStr < 0.01f) {
                works = false;
                setColorOfLabel(creatMutStrlabel, redColor);
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(creatMutStrlabel, redColor);
        }
        /*
         * del threshold
         */
        try {
            float delThresh = Float.parseFloat(delThreshTextField.getRealText());
            config.setDeleteThreshold(delThresh);
            setColorOfLabel(ldelThreshold, greenColor);
            if (delThresh > 0) {
                works = false;
                setColorOfLabel(ldelThreshold, redColor);
            }
        } catch (Exception e) {
            works = false;
            setColorOfLabel(ldelThreshold, redColor);
        }

        if (config.getPopulationSize() < (config.getKids() + config.getSelectionSize())) {
            setColorOfLabel(sellabel, redColor);
            setColorOfLabel(popLabel, redColor);
            setColorOfLabel(kidslabel, redColor);
            errorField.setText("Population size is not bigger than kids and selection size.");
        }
        if (works) {
            Population pop = new Population();
            config.setTerrainSize(Integer.parseInt((String) dropDownTerrainSize.getSelection()));
            config.setColor((String) dropDownTerrainColor.getSelection());
            config.setName(nameTextField.getRealText());
            pop.setConfig(config);
            nifty.exit();
            app.startEvo(pop);

        }
    }

    private void setColorOfLabel(Label field, Color color) {
        if (color.equals(redColor)) {
            if (!field.getText().contains("*")) {
                field.setText(field.getText() + "*");
            }
            field.setColor(color);
            errorField.setColor(color);
            errorField.setText("One or more errors.");
        } else {
            field.setColor(color);
            if (field.getText().contains("*")) {
                field.setText((String) field.getText().subSequence(0, field.getText().length() - 1));
            }
        }
    }

    public void back() {
        nifty.gotoScreen("MainScreen");
    }
}
