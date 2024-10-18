package me.orbitium.oFHeroes.skill;

public class SkillCooldown {
    public String skillID;
    public long date;

    public SkillCooldown(String skillID, long date) {
        this.skillID = skillID;
        this.date = date;
    }
}
