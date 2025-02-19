import java.util.Random;

class Rougue extends Character {
    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);  // Mage-specific logic can go here
    }

    public Rougue() {
        this.name = "Rogue";
        this.profession = "Rogue";
        this.xp = 0;
        this.level = 1;
        this.xpToNextLevel = 400 * level;
        this.health = 200;
        this.maxHealth = 200;
        this.mana = 50;
        this.maxMana = 100;
        this.damage = 50;
        this.critChance = 15;
        this.element = 1;
        Random rdm = new Random();
        for(abilityInventory = 0; abilityInventory < 6; abilityInventory++)
            abilities.add(abilitiesArchive.get(rdm.nextInt(0,abilitiesArchive.size())));
        //abilities.add(new Spell())
    }

    public Rougue(String name, int xp, int lvl) {
        this.name = name;
        this.profession = "Rogue";
        this.xp = xp;
        this.level = lvl;
        this.xpToNextLevel = 400 * level;
        this.health = 200;
        this.maxHealth = 200;
        this.mana = 50;
        this.maxMana = 100;
        this.damage = 75;
        this.element = 1;
        this.critChance = 15;

        for (int i = 1; i < lvl; i++)
            levelUp(this, false);
    }

    @Override
    public int attack() {
        return damage;
    }
}