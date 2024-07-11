package model.projectiles;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static controller.constants.EntityConstants.COLLECTIBLE_LIFE_TIME;

public class OmenoctBullet extends BulletModel {
    public OmenoctBullet(Point anchor, String motionPanelId, int damage) {
        super(anchor, motionPanelId, damage, ShooterEntity.Omenoct);
        Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(COLLECTIBLE_LIFE_TIME.getValue()), e -> eliminate());
        timer.setCoalesce(true);
        timer.setRepeats(false);
        timer.start();
    }

}
