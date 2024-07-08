package model;

import model.entities.Skill;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Profile {
    private static Profile current;
    public String profileId = UUID.randomUUID().toString();
    public CopyOnWriteArrayList<Integer> WAVE_ENEMY_COUNT = new CopyOnWriteArrayList<>(List.of(2));
    public int UPS = 800;
    public int FPS = 80;
    public int EPSILON_MELEE_DAMAGE = 10;
    public int EPSILON_RANGED_DAMAGE = 5;
    public float SOUND_SCALE = 6;
    public float SIZE_SCALE = 0.75f;
    public float GAME_SPEED = 1.8f;
    public int EPSILON_SHOOTING_RAPIDITY = 1;
    public String activeSkillSaveName = "";
    public CopyOnWriteArrayList<String> acquiredSkillsNames = new CopyOnWriteArrayList<>();
    public int totalXP = 600;
    public int currentGameXP = 300;

    public static Profile getCurrent() {
        return current;
    }

    public static void setCurrent(Profile current) {
        Profile.current = current;
    }

    public void updateINSTANCE() {
        this.activeSkillSaveName = Skill.activeSkill != null ? Skill.activeSkill.getName() : "";
        this.acquiredSkillsNames.clear();
    }

    public void saveXP() {
        totalXP += Profile.getCurrent().currentGameXP;
        currentGameXP = 0;
    }
}
