package model.characters;

import model.collision.Collidable;
import model.movement.Direction;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static controller.UserInterfaceController.createCollectible;
import static controller.constants.EntityConstants.COLLECTIBLE_HEALTH;
import static controller.constants.EntityConstants.COLLECTIBLE_LIFE_TIME;
import static controller.constants.EntityConstants.EntityVertices.COLLECTIBLE_VERTICES;
import static controller.constants.ViewConstants.COLLECTIBLE_SIZE_OFFSET;
import static model.Utils.*;

public class CollectibleModel extends GeoShapeModel {
    public static volatile Random random = new Random();
    public GeoShapeModel ancestor;

    public CollectibleModel(GeoShapeModel geoShapeModel) {
        super(new Point2D.Float(0, 0), COLLECTIBLE_VERTICES.getValue(), COLLECTIBLE_HEALTH.getValue());
        this.isCircular = true;
        this.motionPanelId = geoShapeModel.motionPanelId;
        this.anchorSave = new Point2D.Float((COLLECTIBLE_SIZE_OFFSET.getValue() + geoShapeModel.collectibleValue) / 2f,
                (COLLECTIBLE_SIZE_OFFSET.getValue() + geoShapeModel.collectibleValue) / 2f);
        this.ancestor = geoShapeModel;
        createCollectible(modelId, geoShapeModel.modelId, geoShapeModel.collectibleValue, roundPoint(anchorSave), geoShapeModel.motionPanelId);
        Point2D spawnLocation = addUpPoints(geoShapeModel.getAnchor(), multiplyPoint(new Direction(random.nextFloat(0, 360)).getDirectionVector(),
                random.nextFloat(0, geoShapeModel.getRadius())));
        moveShapeModel(spawnLocation);
        movement.setAnchor(spawnLocation);
        new Timer((int) TimeUnit.SECONDS.toMillis(COLLECTIBLE_LIFE_TIME.getValue()), e -> eliminate()) {{
            setCoalesce(true);
            setRepeats(false);
        }}.start();
    }

    public static void bulkCreateCollectibles(GeoShapeModel geoShapeModel) {
        for (int i = 0; i < geoShapeModel.numberOfCollectibles; i++) new CollectibleModel(geoShapeModel);
    }

    public int getValue() {
        return ancestor.collectibleValue;
    }

    @Override
    public boolean collide(Collidable collidable) {
        return collidable instanceof EpsilonModel;
    }
}
