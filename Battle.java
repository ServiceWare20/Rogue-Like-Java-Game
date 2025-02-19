interface Battle {
    int attack();
    void getDamage(Enemy enemy, int damage, int element);
    boolean isAlive();
    void useAbility(int abilityIndex, Enemy enemy);
}