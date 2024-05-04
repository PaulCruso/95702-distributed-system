package cmu.ds.hearthstonewebservelet.HearthStoneCard;
/**
 * Card.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: March 31, 2024
 *
 * Represents a card with various attributes typically found in card games. This class provides methods to access the card's
 * properties such as ID, name, text description, flavor text, class, cost, rarity, and image URL.
 * The constructor processes the input text by removing specific symbols and formatting characters
 * to normalize the text content for consistent usage.
 */
public class Card {
    protected String id;
    protected String name;
    protected String text;
    protected String flavor;
    protected String cardClass;
    protected int cost;
    protected String rarity;
    protected String type;
    protected String imageURL;

    /**
     * Constructs a new Card instance with the specified attributes.
     *
     * @param id        the unique identifier of the card
     * @param name      the name of the card
     * @param text      the descriptive text of the card, with certain symbols and formatting removed
     * @param flavor    the flavor text of the card, providing lore or thematic context
     * @param cardClass the class or category of the card
     * @param cost      the mana or resource cost to play the card
     * @param rarity    the rarity level of the card
     * @param imageURL  the URL of the image representing the card
     */
    public Card(String id, String name, String text, String flavor, String cardClass, int cost, String rarity, String imageURL) {
        this.id = id;
        this.name = name;
        text = text.replace("[x]", "")
                .replace("<b>", "")
                .replace("</b>", "")
                .replace("<i>", "")
                .replace("</i>", "")
                .replace("\n", " ")
                .replace("$", "");
        this.text = text;
        this.flavor = flavor;
        this.cardClass = cardClass;
        this.cost = cost;
        this.rarity = rarity;
        this.imageURL = imageURL;
    }

    /**
     * Retrieve the id of the card
     * @return card id
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieve the name of the card
     * @return card name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the text of the card
     * @return card text
     */
    public String getText() {
        return text;
    }

    /**
     * Retrieve the flavor of the card
     * @return card flavor
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * Retrieve the class of the card
     * @return card class
     */
    public String getCardClass() {
        return cardClass;
    }

    /**
     * Retrieve the cost of the card
     * @return card cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Retrieve the rarity of the card
     * @return card rarity
     */
    public String getRarity() {
        return rarity;
    }

    /**
     * Retrieve the type of the card
     * @return card type
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve the image URL of the card
     * @return image URL of card
     */
    public String getImageURL() {
        return imageURL;
    }

}

