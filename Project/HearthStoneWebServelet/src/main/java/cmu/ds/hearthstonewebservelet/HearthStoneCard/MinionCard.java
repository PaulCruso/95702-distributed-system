package cmu.ds.hearthstonewebservelet.HearthStoneCard;
/**
 * MinionCard.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: March 31, 2024
 *
 * Represents a card which type is minion and extends the card class.
 * it has additional features race, attack, health.
 */
public class MinionCard extends Card {
    private String race;
    private int attack;
    private int health;
    /**
     * Constructs a new HeroCard instance with the specified attributes.
     *
     * @param id        the unique identifier of the card
     * @param name      the name of the card
     * @param text      the descriptive text of the card, with certain symbols and formatting removed
     * @param flavor    the flavor text of the card, providing lore or thematic context
     * @param cardClass the class or category of the card
     * @param cost      the mana or resource cost to play the card
     * @param rarity    the rarity level of the card
     * @param imageURL  the URL of the image representing the card
     * @param race      the race representing the card
     * @param attack    the attack of the card
     * @param health    the health of the card
     */
    public MinionCard(String id, String name, String text, String flavor, String cardClass, int cost, String rarity, String imageURL,
                      String race, int attack, int health) {
        super(id, name, text, flavor, cardClass, cost, rarity, imageURL);
        this.race = race;
        this.attack = attack;
        this.health = health;
        this.type = "MINION";
    }

    /**
     * Retrieve the race of the card
     * @return card race
     */
    public String getRace() {
        return race;
    }

    /**
     * Retrieve the attack of the card
     * @return card attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Retrieve the health of the card
     * @return card health
     */
    public int getHealth() {
        return health;
    }

}
