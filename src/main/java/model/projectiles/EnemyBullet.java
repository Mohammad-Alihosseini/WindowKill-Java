package model.projectiles;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

import static controller.constants.EntityConstants.COLLECTIBLE_LIFE_TIME;

public class EnemyBullet extends BulletModel {
    public EnemyBullet(Point anchor, String motionPanelId, int damage, ShooterEntity enemy) {
        super(anchor, motionPanelId, damage, enemy);
        Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(COLLECTIBLE_LIFE_TIME.getValue()), e -> eliminate());
        timer.setCoalesce(true);
        timer.setRepeats(false);
        timer.start();
    }

}
