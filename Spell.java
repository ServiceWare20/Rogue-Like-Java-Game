class Spell extends Game {
    protected int damage;
    protected int manaCost;
    protected String name;
    protected int element;

    static class SpellVisitor implements Visitor<Entity> {
        private Spell spell;

        public SpellVisitor(Spell spell) {
            this.spell = spell;
        }

        @Override
        public void visit(Entity entity) {
            // Handle the interaction between the spell and the entity
            if (entity instanceof Character) {
                Character character = (Character) entity;
                character.getDamage((Enemy) entity, spell.damage, spell.element);  // Example interaction
            }
            // Add more cases if needed (e.g., Enemy)
        }
    }

    public Spell(String name, int damage, int manaCost, int element) {
        this.name = name;
        this.damage = damage;
        this.manaCost = manaCost;
        this.element = element;
    }

    public Spell(Spell other) {
        this.name = other.name;
        this.damage = other.damage;
        this.manaCost = other.manaCost;
        this.element = other.element;
    }


    @Override
    public String toString() {
        return name + ": Damage = " + damage + ", Mana Cost = " + manaCost + ", Element = " + element;
    }
}