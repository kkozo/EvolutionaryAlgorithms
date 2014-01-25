package gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.EffectBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;
import de.lessvoid.nifty.examples.defaultcontrols.common.CommonBuilders;
import evoalgo.EvoAlgoStart;

/**
 * Builds the mainmenu
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
        // <screen>
        nifty.addScreen("mainScreen", new ScreenBuilder("Main Screen") {
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
        nifty.addScreen("startScreen", new ScreenBuilder("Start config screen") {
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
                                control(new TextFieldBuilder("Size", "Quit") {
                                    {
                                        backgroundColor("#a5cbf6");
                                        color("#a5cbf6");
                                        alignCenter();
                                        valignCenter();
                                        height("20%");
                                        width("25%");
                                    }
                                });
                                panel(builders.hspacer("10px"));
                                control(new TextFieldBuilder("Size", "Quit") {
                                    {
                                        backgroundColor("#a5cbf6");
                                        color("#a5cbf6");
                                        alignCenter();
                                        valignCenter();
                                        height("20%");
                                        width("25%");
                                    }
                                });
                                panel(builders.hspacer("10px"));
                            }
                        });

                    }
                });
            }
        }.build(nifty));
        nifty.gotoScreen("mainScreen");
    }
}
