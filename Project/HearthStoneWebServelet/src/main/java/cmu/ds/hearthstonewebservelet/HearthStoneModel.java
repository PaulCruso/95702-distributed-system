package cmu.ds.hearthstonewebservelet;
/**
 * HearthStoneModel.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: April 2, 2024
 *
 * Provides functionality for interacting with a MongoDB database to store and analyze Hearthstone card data.
 * This class includes methods for fetching card data from the Hearthstone API, converting it into {@link Card}
 * objects, adding these objects to a MongoDB collection, and performing basic analytics on the stored data.
 * The analytics functionality includes calculating the average cost of cards, determining the most
 * common type of card, and generating a summary of cards stored in the database.
 */

import cmu.ds.hearthstonewebservelet.HearthStoneCard.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.*;
import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HearthStoneModel {
    // The database name of my mongoDB
    private static final String DATABASE_NAME = "hearthstone";
    // The database collection name of my mongoDB
    private static final String COLLECTION_NAME = "cards";
    // The specific String to connect
    private static ConnectionString CONNECTION_STRING = new ConnectionString("mongodb+srv://188217266:8KMSerqvLFeEnHMy@cluster0.qkyge7b.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0");
    // The connect method to connect to spefic collection
    private static MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(CONNECTION_STRING)
            .serverApi(ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build())
            .build();
    private static MongoClient mongoClient = MongoClients.create(settings);

    private static MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
    private static MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
    // Get Hearthstone card information from Hearthstone API
    private static String heartStoneJson = fetch("https://api.hearthstonejson.com/v1/195635/enUS/cards.collectible.json");

    private static double averageCost = 0;

    private static HashMap<String, Integer> typeCount = new HashMap<>();

    private static String mostType = null;
    private static int count;

    private static ArrayList<String> mongoDBString;

    /**
     * Adds a {@link Card} object to the MongoDB collection based on the card name provided.
     * If a card with the given name is found within the Hearthstone API data, it is converted to a MongoDB {@link Document}
     * and inserted into the collection. The card's type (Minion, Spell, Weapon, Hero) is also determined and stored.
     *
     * @param cardName The name of the card to be added to the database.
     */
    public static void addMongoDB(String cardName) {
        Card card = getCard(cardName);
        if (card != null) {
            // Create a MongoDB Document object from the Hearthstone Card object
            Document cardDocument = new Document("cardName", card.getName())
                    .append("cardId", card.getId())
                    .append("cardText", card.getText())
                    .append("cardClass", card.getCardClass())
                    .append("cardCost", card.getCost())
                    .append("cardFlavor", card.getFlavor())
                    .append("cardRarity", card.getRarity())
                    .append("cardImageUrl", card.getImageURL());

            // Add the appropriate fields based on the type of card
            if (card instanceof MinionCard) {
                cardDocument.append("cardType", "MINION");
            } else if (card instanceof SpellCard) {
                cardDocument.append("cardType", "SPELL");
            } else if (card instanceof WeaponCard) {
                cardDocument.append("cardType", "WEAPON");
            } else if (card instanceof HeroCard) {
                cardDocument.append("cardType", "HERO");
            } else {
                cardDocument.append("cardType", "Unknown Type");
            }
            // Insert the card information into the MongoDB collection
            collection.insertOne(cardDocument);
        }
    }

    /**
     * Retrieves and converts the MongoDB collection data into a JSON string format.
     * This method iterates over each document in the collection, converts it to JSON,
     * and removes the MongoDB-generated "_id" field.
     *
     * @return A JSON representation of the MongoDB collection data.
     */
    public static String getMongoDBJson() {
        JsonWriterSettings jsonSettings = JsonWriterSettings.builder()
                .outputMode(JsonMode.RELAXED)
                .indent(true)
                .build();
        JsonObject jsonObject = new JsonObject();
        for (Document doc : collection.find()) {
            // Convert document to JSON and remove the _id field
            String json = doc.toJson(jsonSettings);
            jsonObject = JsonParser.parseString(json).getAsJsonObject();
            jsonObject.remove("_id");
        }
        return jsonObject.toString();
    }

    /**
     * Fetches and constructs a {@link Card} object based on the given card name.
     * This method searches through the Hearthstone API data for a card matching the provided name.
     * If found, it creates an instance of the appropriate subclass of {@link Card}
     * (e.g., MinionCard, SpellCard) with the card's details.
     *
     * @param name The name of the card to retrieve.
     * @return A {@link Card} object representing the found card, or {@code null} if no matching card is found.
     */
    public static Card getCard(String name){
        // Convert the api string to Json array
        JsonArray cardDeck = JsonParser.parseString(heartStoneJson).getAsJsonArray();
        for (int i = 0; i < cardDeck.size(); i++){
            JsonObject card = cardDeck.get(i).getAsJsonObject();
            String currentCardName = card.get("name").getAsString();
            // Define the parameter of the mongoDB collections
            if (currentCardName.equalsIgnoreCase(name)){
                String id = card.get("id").getAsString();
                String text = card.get("text").getAsString();
                String flavor = card.get("flavor").getAsString();
                String cardClass = card.get("cardClass").getAsString();
                int cost = card.get("cost").getAsInt();
                String rarity = card.get("rarity").getAsString();
                String imageURL = getImageURL(id);

                int attack = 0;
                if (card.has("attack")) {
                    attack = card.get("attack").getAsInt();
                }

                int health = 0;
                if (card.has("health")) {
                    health = card.get("health").getAsInt();
                }

                int durability = 0;
                if (card.has("durability")) {
                    durability = card.get("durability").getAsInt();
                }

                int armor = 0;
                if (card.has("armor")) {
                    armor = card.get("armor").getAsInt();
                }

                String race = null;
                if (card.has("race")) {
                    race = card.get("race").getAsString();
                }

                // Define the type
                String type = card.get("type").getAsString();
                switch (type){
                    case "HERO":
                        return new HeroCard(id, name, text, flavor, cardClass, cost, rarity, imageURL, armor);
                    case "MINION":
                        return new MinionCard(id, name, text, flavor, cardClass, cost, rarity, imageURL, race,
                                attack, health);
                    case "SPELL":
                        return new SpellCard(id, name, text, flavor, cardClass, cost, rarity, imageURL);
                    case "WEAPON":
                        return new WeaponCard(id, name, text, flavor, cardClass, cost, rarity, imageURL, attack, durability);
                    default:
                        return new Card(id, name, text, flavor, cardClass, cost, rarity, imageURL);
                }
            }
        }
        return null;
    }
    /**
     * Copy from Lab2 InterestingPicture.
     * Make an HTTP request to a given URL.
     *
     * @param urlString The URL of the request
     * @return A string of the response from the HTTP GET.  This is identical
     * to what would be returned from using curl on the command line.
     */
    public static String fetch(String urlString) {
        String response = "";
        try {
            URL url = new URL(urlString);
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
            // Do something reasonable.  This is left for students to do.
        }
        return response;
    }



    /**
     * Performs analytics on the cards stored in the MongoDB collection.
     * This method calculates the average cost of the cards, determines the most common card type,
     * and generates a summary list of card details for all cards in the collection.
     */
    public static void analytic() {
        FindIterable<Document> iter = collection.find();
        MongoCursor<Document> cursor = iter.iterator();
        int totalCost = 0;
        count = 0;
        typeCount = new HashMap<>();
        mongoDBString = new ArrayList<>();

        // use loop to generate mongoDBString and calculate the total numbers of the collection
        while (cursor.hasNext()) {
            Document document = cursor.next();
            count++;
            totalCost += (int) document.get("cardCost");
            int getNumber = typeCount.getOrDefault((String) document.get("cardType"),1);
            typeCount.put((String) document.get("cardType"), getNumber);
            String cardName = (String) document.get("cardName");
            String cardId = (String) document.get("cardId");
            String cardText = (String) document.get("cardText");
            String cardClass = (String) document.get("cardClass");
            int cardCost = (int) document.get("cardCost");
            String cardFlavor = (String) document.get("cardFlavor");
            String cardRarity = (String) document.get("cardRarity");
            String cardImageUrl = (String) document.get("cardImageUrl");
            String cardType = (String) document.get("cardType");
            String formatOutput = cardName + "  " + cardId + "  " + cardText + "  " +
                    cardClass + "  " + cardCost + "  " + cardFlavor
                    + "  " + cardRarity + "  " +cardImageUrl + "  " + cardType;
            mongoDBString.add(formatOutput);
        }
        // Calculate average cost
        averageCost = (double) totalCost / count;
        int maxValue = Integer.MIN_VALUE;

        // find the most common type
        for (String key : typeCount.keySet()) {
            int value = typeCount.get(key);
            if (value > maxValue) {
                maxValue = value;
                mostType = key;
            }
        }
    }

    /**
     * Retrieve the average cost of the card collection.
     * @return the average cost of card collection
     */
    public static double getAverageCost() {
        return averageCost;
    }

    /**
     * Retrieve the most common type of the card collection.
     * @return the most common type of card collection
     */
    public static String getMostType() {
        return mostType;
    }

    /**
     * Retrieve the total number of card in the card collection.
     * @return the total number of card in the card collection
     */
    public static int getCount() {
        return count;
    }

    /**
     * Retrieve specific string which should be presented in dashboard
     * @return the specific string of mongodb card collections with certain format
     */
    public static ArrayList<String> getMongoDBString() {
        return mongoDBString;
    }

    /**
     * Retrieve the image URL of the card with specific format
     * @return image URL of card
     */
    private static String getImageURL(String id){
        return "https://art.hearthstonejson.com/v1/512x/"+ id + ".jpg";
    }
}
