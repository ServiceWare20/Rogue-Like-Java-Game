import java.util.ArrayList;
import java.util.List;

abstract class Entity extends Game implements Battle {
    protected List<Spell> abilitiesArchive = new ArrayList<Spell>();
    protected List<Spell> abilities = new ArrayList<>();
    protected int abilityInventory;
    protected int xp;
    protected double level;
    protected double xpToNextLevel;
    protected int health;
    protected int maxHealth;
    protected int mana;
    protected int maxMana;
    protected int damage;
    protected int element;
    protected String profession;
    protected int critChance;
    protected boolean dead;

    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    public void archive(List<Spell> archive) {
        // Earth
        archive.add(new Spell("The ROCK", 250, 70, 0));
        archive.add(new Spell("Earth Bound", 180, 65, 0));
        archive.add(new Spell("Rupture", 150, 60, 0));
        archive.add(new Spell("Comet", 120, 55, 0));
        archive.add(new Spell("Earthquake", 70, 45, 0));
        archive.add(new Spell("Stone Age", 60, 25, 0));
        //archive.add(new Spell("Tribute to Earth") TODO: Health regen spell
        // Fire
        archive.add(new Spell("Fire Hazard", 300, 100, 1));
        archive.add(new Spell("Silviu", 150, 60, 1));
        archive.add(new Spell("Solar Flare", 120, 50, 1));
        archive.add(new Spell("HellFire", 100, 45, 1));
        archive.add(new Spell("Ignis", 75, 25, 1));
        archive.add(new Spell("Firewall", 50, 10, 1));
        // Ice
        archive.add(new Spell("Ice Blast", 250, 80, 2));
        archive.add(new Spell("Frost Bite", 200, 70, 2));
        archive.add(new Spell("Ice Splinters", 150, 55, 2));
        archive.add(new Spell("Blizzard", 100, 40, 2));
        archive.add(new Spell("Absolute 0", 75, 30, 2));
        archive.add(new Spell("Frigid Storm", 50, 20, 2));

    }

    public int elementDuplicator(int dmg, int enemyElement, int spellElement) {
        if (enemyElement == spellElement + 1 || (spellElement == 2 && enemyElement == 0))
            return dmg * 2;
        else if (enemyElement == spellElement - 1 || (spellElement == 0 && enemyElement == 2))
            return dmg / 2;
        else return dmg;
    }

    @Override
    public void getDamage(Enemy enemy, int damage, int element) {
        int calculatedDamage;
        if (element > -1)
            calculatedDamage = elementDuplicator(enemy.calculateDamage(damage, critChance), enemy.element, element);
        else calculatedDamage = enemy.calculateDamage(damage, critChance);
        health -= calculatedDamage;
        if (health <= 0) dead = true;
    }

    //TODO: ADD CRITCHANCE FOR ALL (INCLUDING ENEMIES

    public void useAbility(int abilityIndex, Enemy enemy) {
        if (abilityIndex < 0 || abilityIndex >= abilities.size()) return;
        Spell spell = abilities.get(abilityIndex);
        if (spell.manaCost <= mana) {
            mana -= spell.manaCost;

            enemy.getDamage(enemy, spell.damage, spell.element);
            abilities.remove(spell);
            abilityInventory--;
        }
        else {
            enemy.getDamage(enemy, this.attack(), -1);
        }

    }

    public void regenerateHealth(int amount) {
        health = Math.min(health + amount, this.maxHealth); // Assuming max health is 100
    }

    public void regenerateMana(int amount) {
        mana = Math.min(mana + amount, this.maxMana); // Assuming max mana is 100
    }

    public void levelUp(Character player, boolean display) {
        double multiplyIndex = 1;
        if (player.profession == "Rogue") {
            multiplyIndex = 1.2;
            critChance += 5;
        }
        else if (player.profession == "Warrior")
            multiplyIndex = 2;

        ++player.level;
        player.damage = (int) (player.damage + 10 * multiplyIndex * player.level);
        player.mana = (int) (player.mana + 30 * multiplyIndex * player.level);
        player.maxMana += 30 * player.level;
        player.health = (int) (player.health + 50 * multiplyIndex * player.level);
        player.maxHealth += 50 * multiplyIndex * player.level;
        player.xpToNextLevel *= 2;
        player.xp = 0;
//          System.out.println(level);
        if (display) {
            System.out.println("You are now level " + (int) player.level);
            System.out.println("Stats: ");
            System.out.println("Basic Damage: " + player.damage);
            System.out.println("Mana: " + player.mana);
        }
    }

    public void checkLvlUp(Character player) {
        if (player.xp == player.xpToNextLevel)
            levelUp(player, true);
    }
}