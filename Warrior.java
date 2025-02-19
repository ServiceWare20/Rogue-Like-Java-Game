class Warrior extends Character {
    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);  // Mage-specific logic can go here
    }

    public Warrior() {
        this.name = "Warrior";
        this.profession = "Warrior";
        this.xp = 0;
        this.level = 1;
        this.xpToNextLevel = 1000 * level;
        this.health = 300;
        this.maxHealth = 300;
        this.mana = 0;
        this.maxMana = 100;
        this.damage = 100;
        this.element = 2;
        this.critChance = 5;
    }

    public Warrior(String name, int xp, int lvl) {
        this.name = name;
        this.profession = "Warrior";
        this.xp = xp;
        this.level = lvl;
        this.xpToNextLevel = 400 * level;
        this.health = 300;
        this.maxHealth = 300;
        this.mana = 0;
        this.maxMana = 100;
        this.damage = 100;
        this.element = 2;
        this.critChance = 5;
        for (int i = 1; i < lvl; i++)
            levelUp(this, false);
    }

    @Override
    public int attack() {
        return damage;
    }
}