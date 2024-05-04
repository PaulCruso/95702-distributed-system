package cmu.ds.hearthstonewebservelet.HearthStoneCard;
/**
 * SpellCard.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: March 31, 2024
 *
 * Represents a card which type is spell and extends the card class.
 * it has additional features attack and durability.
 */
public class WeaponCard extends Card {
    private int attack;
    private int durability;
    public WeaponCard(String id, String name, String text, String flavor, String cardClass,
                      int cost, String rarity, String imageURL, int attack, int durability) {
        super(id, name, text, flavor, cardClass, cost, rarity, imageURL);
        this.attack = attack;
        this.durability = durability;
    }

    /**
     * Retrieve the attack of the card
     * @return card attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Retrieve the durability of the card
     * @return card durability
     */
    public int getDurability() {
        return durability;
    }

}
