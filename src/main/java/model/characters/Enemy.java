package model.characters;

import java.util.concurrent.atomic.AtomicInteger;

public interface Enemy {
    AtomicInteger numOfKilledEnemies = new AtomicInteger(0);

    static void EnemyEliminated() {
        numOfKilledEnemies.incrementAndGet();
    }

    static int getNumOfKilledEnemies() {
        return numOfKilledEnemies.get();
    }

    static void resetNumOfKilledEnemies() {
        numOfKilledEnemies.set(0);
    }
}
