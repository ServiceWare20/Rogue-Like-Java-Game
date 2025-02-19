import java.util.Random;

class Enemy extends Entity {


    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);  // Pass the enemy to the visitor
    }

    public Enemy() {
        Random rdm = new Random();
        this.health = rdm.nextInt(100 + 50 * (floor - 1), 300 + 100 * (floor - 1));
        this.mana = rdm.nextInt(50, 150);
        this.damage = rdm.nextInt(20 + 50 * (floor - 1), 100 + 50 * (floor - 1));
        this.element = rdm.nextInt(0, 2);
        this.critChance = rdm.nextInt((1 + 2 * floor) % 101, 5 * floor);
        this.critChance %= 100;
    }

    public int calculateDamage(int damage, int critChance) {
        Random rdm = new Random();
        if (rdm.nextInt(1, 100) <= critChance)
            return damage * 2;
        else if (rdm.nextInt(1, 100) <= 50)
            return damage / 2;
        else return damage;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void useAbility(int abilityIndex, Enemy enemy) {

    }

    @Override
    public int attack() {
        return damage;
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
}