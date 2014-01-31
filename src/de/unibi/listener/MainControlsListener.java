package de.unibi.listener;

import com.jme3.bullet.BulletAppState;
import com.jme3.input.controls.ActionListener;
import de.unibi.evolution.EvolutionController;
import de.unibi.evolution.modifiers.IRecombiner;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

/**
 * TODO: Add BitmapInfoText for Controls This listener handles simple input like
 * speeding up the evolution and slowing down.
 *
 * @author Andi
 */
public class MainControlsListener implements ActionListener {

    private BulletAppState bulletAppState;
    private final float STANDARD_SPEED;
    private EvolutionController evoController;

    public MainControlsListener(BulletAppState bulletAppState, EvolutionController cont) {
        this.bulletAppState = bulletAppState;
        STANDARD_SPEED = bulletAppState.getSpeed();

        this.evoController = cont;

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals("speedup") && isPressed) {
            float predictedSpeed = bulletAppState.getSpeed() + (250.0f * tpf);
            if (predictedSpeed > 20) {
                predictedSpeed = 20;
            }
            System.out.println("Speed now: " + predictedSpeed);
            EvolutionController.CURRENT_SPEED = predictedSpeed;
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
        } else if (name.equals("speeddown") && isPressed) {
            float predictedSpeed = bulletAppState.getSpeed() - (250.0f * tpf);
            if (predictedSpeed < 0) {
                predictedSpeed = 0;
            }
            EvolutionController.CURRENT_SPEED = predictedSpeed;
            System.out.println("Speed now: " + predictedSpeed);
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
        } else if (name.equals("reset") && isPressed) {
            EvolutionController.CURRENT_SPEED = STANDARD_SPEED;
            bulletAppState.setSpeed(STANDARD_SPEED);
            System.out.println("Speed now: " + STANDARD_SPEED);

        } else if (name.equals("start") && isPressed) {
            EvolutionController.STARTED = true;
            EvolutionController.CURRENT_SPEED = STANDARD_SPEED;
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
            System.out.println("EVOLUTION STARTED");
        } else if (name.equals("del") && isPressed) {
            EvolutionController.STARTED = false;
            EvolutionController.CURRENT_SPEED = 0f;
            bulletAppState.setSpeed(EvolutionController.CURRENT_SPEED);
            System.out.println("EVOLUTION HALTED");
        } else if (name.equals("focus") && isPressed) {
            if (EvolutionController.CAM_ENABLED) {
                EvolutionController.CAM_ENABLED = false;
                
            } else {
                EvolutionController.CAM_ENABLED = true;
            }
        } else if (name.equals("save") && isPressed) {
            evoController.save();
        }

    }
}
