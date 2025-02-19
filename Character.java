import java.util.Random;

abstract class Character extends Entity {
    protected String name;

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);  // Pass the current entity to the visitor
    }
    @Override
    public boolean isAlive() {
        return !dead;
    }
    class Factory {
        public static Character characterFactory(String profession, String name, int xp, int lvl) {
            if (profession == "Warrior")
                return new Warrior(name, xp, lvl);
            if (profession == "Rougue")
                return new Rougue(name, xp, lvl);
            if (profession == "Mage")
                return new Mage(name, xp, lvl);
            return null;
        }
    }
    public void reload(Character player){
        Random rdm = new Random();
        //player.abilities.clear(); // Clear existing abilities
        // player.abilityInventory = 0; // Reset ability inventory

        if (player.abilitiesArchive == null || player.abilitiesArchive.isEmpty()) {
            archive(player.abilitiesArchive);
            //throw new IllegalStateException("abilitiesArchive is empty or not initialized.");
        }

        while (player.abilityInventory < 6) {
            Spell newSpell = new Spell(player.abilitiesArchive.get(rdm.nextInt(player.abilitiesArchive.size())));
            // Adjust the spell damage based on the player's current level
            newSpell.damage += 50 * (player.level - 1);
            player.abilities.add(newSpell);
            player.abilityInventory++;
        }
    }
}