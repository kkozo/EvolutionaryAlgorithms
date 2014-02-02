package de.unibi.gui;

import de.unibi.gui.screencontroller.MainMenuController;
import de.unibi.gui.screencontroller.ConfigController;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import de.unibi.evoalgo.EvoAlgoStart;

/**
 * Builds the mainmenu
 *
 * @author Andi
 */
public class MainMenu {

    private final EvoAlgoStart app;
    private static CommonBuilders builders = new CommonBuilders();

    public MainMenu(final EvoAlgoStart app) {
        this.app = app;
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        app.getGuiViewPort().addProcessor(niftyDisplay);
        app.getFlyByCamera().setDragToRotate(true);

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadStyleFile("button.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.loadStyleFile("textfield.xml");



        ConfigController configC = new ConfigController();
        configC.setApp(app);
        nifty.addXml("config.xml");
        nifty.registerScreenController(configC);
        
        nifty.addScreen("MainScreen", new ScreenBuilder("MainScreen") {
            {
                controller(new MainMenuController(app));
                layer(new LayerBuilder("layerId") {
                    {
                        color("#a5cbf6");
                        childLayoutCenter();
                        onStartScreenEffect(new EffectBuilder("fade") {
                            {
                                length(800);
                                effectParameter("start", "#0");
                                effectParameter("end", "#a5cbf6");
                            }
                        });
                        onActiveEffect(new EffectBuilder("gradient") {
                            {
                                effectValue("offset", "0%", "color", "#a5cbf6ff");
                                effectValue("offset", "85%", "color", "#a5cbf6ff");
                                effectValue("offset", "100%", "color", "#a5cbf6ff");
                            }
                        });
                        paddingTop("30%");
                        panel(new PanelBuilder("panelId") {
                            {
                                height("100%");
                                width("100%");
                                childLayoutVertical();
                                color("#a5cbf6");
                                control(new ButtonBuilder("StartButton", "Start new Evolution") {
                                    {
                                        color("#0078ff");
                                        selectionColor("#0078ff");
                                        alignCenter();
                                        valignCenter();
                                        height("20%");
                                        width("25%");
                                        interactOnClick("start()");
                                    }
                                });
                                control(new ButtonBuilder("LoadButton", "Load old evolution") {
                                    {
                                        color("#0078ff");
                                        selectionColor("#0078ff");
                                        alignCenter();
                                        valignCenter();
                                        height("20%");
                                        width("25%");
                                        interactOnClick("load()");
                                    }
                                });
                                panel(builders.hspacer("10px"));
                                control(new ButtonBuilder("QuitButton", "Quit") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        height("20%");
                                        width("25%");
                                        interactOnClick("quit()");
                                    }
                                });
                            }
                        });

                    }
                });
            }
        }.build(nifty));
        nifty.gotoScreen("MainScreen");
        
    }
}
