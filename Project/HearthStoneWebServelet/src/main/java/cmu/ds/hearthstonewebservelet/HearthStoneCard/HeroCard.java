package cmu.ds.hearthstonewebservelet.HearthStoneCard;
/**
 * HeroCard.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: March 31, 2024
 *
 * Represents a card which type is hero and extends the card class.
 * it has additional feature armor.
 */
public class HeroCard extends Card {
    int armor;

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
     * @param armor     the armor representing the card
     */
    public HeroCard(String id, String name, String text, String flavor, String cardClass, int cost, String rarity, String imageURL,
                    int armor) {
        super(id, name, text, flavor, cardClass, cost, rarity, imageURL);
        this.armor = armor;
        this.type = "HERO";
    }

    /**
     * Retrieve the armor of the card
     * @return card armor
     */
    public int getArmor() {
        return armor;
    }
}
