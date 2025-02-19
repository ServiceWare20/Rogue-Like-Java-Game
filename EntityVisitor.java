interface EntityVisitor<T extends Entity> {
    void visit(Mage mage);
    void visit(Rougue rogue);
    void visit(Warrior warrior);
    void visit(Enemy enemy);
}