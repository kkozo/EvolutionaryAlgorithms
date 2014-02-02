package de.unibi.gui.screencontroller;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.unibi.evoalgo.EvoAlgoStart;
import de.unibi.evolution.Population;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Controller for the MainMenuScreen
 *
 * @author Andi
 */
public class MainMenuController implements ScreenController {

    private EvoAlgoStart app;
    private Screen screen;
    private Nifty nifty;

    public MainMenuController(EvoAlgoStart app) {
        this.app = app;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.screen = screen;
        this.nifty = nifty;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void quit() {
        app.requestClose(true);
    }

    public void load() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new J3oFileChooser());

        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Population pop = app.loadPop(file.getAbsolutePath());
            nifty.exit();
            try {
                app.startEvo(pop);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        nifty.gotoScreen("config");
    }

    public EvoAlgoStart getApp() {
        return app;
    }

    private class J3oFileChooser extends FileFilter {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("j3o")) {
                    return true;
                } else {
                    return false;
                }
            }


            return false;
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }

        @Override
        public String getDescription() {
            return "EvoAlgo";
        }
    }
}
