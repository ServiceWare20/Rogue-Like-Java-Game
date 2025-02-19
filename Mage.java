class Mage extends Character {

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);  // Mage-specific logic can go here
    }

    public Mage() {
        archive(abilitiesArchive);
        //System.out.println(abilitiesArchive.size());
        this.name = "Mage";
        this.profession = "Mage";
        this.xp = 0;
        this.level = 1;
        this.xpToNextLevel = 400 * level;
        this.health = 100;
        this.maxHealth = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.damage = 50;
        this.element = 2;
        this.abilityInventory = 0;
        this.critChance = 0;
        reload(this);
        //abilities.add(new Spell())
    }

    public Mage(String name, int xp, int lvl) {
        archive(abilitiesArchive);
        //System.out.println(abilitiesArchive.size());
        this.name = name;
        this.profession = "Mage";
        this.xp = xp;
        this.level = lvl;
        this.xpToNextLevel = 400;
        this.health = 100;
        this.maxHealth = 100;
        this.mana = 100;
        this.maxMana = 100;
        this.damage = 50000;
        this.element = 2;
        this.abilityInventory = 0;
        this.critChance = 0;
        for (int i = 1; i < lvl; i++)
            levelUp(this, false);
        reload(this);
        //abilities.add(new Spell())
    }

    @Override
    public int attack() {
        return damage;
    }
}