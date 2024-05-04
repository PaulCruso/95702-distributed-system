import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.Scanner;

/**
 * The ShakespeareAnalytics class utilizes Apache Spark to perform various textual analyses on a specified text file.
 * It can count lines, words, distinct words, symbols, and distinct symbols.
 * It also allows for searching text for specific words.
 *
 * Author: Kaizhong Ying
 * Andrew ID: kying
 */
public class ShakespeareAnalytics {
    /**
     * Configuration for the Spark application, set to run locally and named 'JD Word Counter'.
     */
    private static final SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
    /**
     * Spark context for RDD operations.
     */
    private static final JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

    /**
     * RDD representing the input file, each element is a line of text from the file.
     */
    private static JavaRDD<String> inputFile;

    /**
     * Calculates and prints the total number of lines in the text file.
     */
    private static void lineCount() {
        JavaRDD<String> lineFromFile = inputFile.flatMap(content -> Arrays.asList(content.split("\n")));

        System.out.println("Number of lines: " + lineFromFile.count());
    }

    /**
     * Calculates and prints the total number of words in the text file.
     * Words are defined as groups of alphabetic characters.
     */
    private static void wordCount() {
        JavaRDD<String> wordsFromFile = inputFile.flatMap(content ->
                Arrays.asList(content.split("[^a-zA-Z]+"))).filter(k -> ( !k.isEmpty()));

        System.out.println("Number of words: " + wordsFromFile.count());
    }

    /**
     * Calculates and prints the total number of distinct words in the text file.
     * Words are defined as groups of alphabetic characters.
     */
    private static void distinctWordCount() {
        JavaRDD<String> distinctWordsFromFile = inputFile.flatMap(content ->
                Arrays.asList(content.split("[^a-zA-Z]+"))).filter(k -> ( !k.isEmpty())).distinct();

        System.out.println("Number of distinct words: " + distinctWordsFromFile.count());
    }
    /**
     * Calculates and prints the total number of symbols
     * (including alphabets, digits, and special characters) in the text file.
     */
    private static void symbolCount() {
        JavaRDD<String> symbolsFromFile = inputFile.flatMap(content ->
                Arrays.asList(content.split("")));

        System.out.println("Number of symbols: " + symbolsFromFile.count());
    }

    /**
     * Calculates and prints the total number of distinct symbols in the text file.
     */
    private static void distinctSymbolCount() {
        JavaRDD<String> distinctSymbolsFromFile = inputFile.flatMap(content ->
                Arrays.asList(content.split(""))).distinct();

        System.out.println("Number of distinct symbols: " + distinctSymbolsFromFile.count());
    }

    /**
     * Calculates and prints the total number of distinct letters (alphabets only) in the text file.
     */
    private static void distinctLetterCount() {
        JavaRDD<String> distinctSymbolsFromFile = inputFile.flatMap(content ->
                Arrays.asList(content.split(""))).filter(k ->
                k.matches("[a-zA-Z]+")).distinct();

        System.out.println("Number of distinct letters: " + distinctSymbolsFromFile.count());

    }

    /**
     * Prompts the user to enter a word and searches for all lines in the text file that contain this word.
     * Prints each matching line to the console.
     */
    private static void search() {
        System.out.println("Enter a word: ");
        Scanner readInput = new Scanner(System.in);
        String key = readInput.nextLine();

        JavaRDD<String> lines = inputFile.flatMap(content -> Arrays.asList(content.split("\n")));

        JavaRDD<String> matchedLines = lines.filter(k -> k.contains(key));
        System.out.println("Lines containing the searched word:");
        for (String line : matchedLines.collect()) {
            System.out.println(line);
        }
    }

    /**
     * Main method to run the application. Expects path to the text file as a command-line argument.
     *
     * @param args Command-line arguments, expects a single argument specifying the path to the text file.
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("No files provided.");
            System.exit(0);
        }

        inputFile = sparkContext.textFile(args[0]);

        lineCount();

        wordCount();

        distinctWordCount();

        symbolCount();

        distinctSymbolCount();

        distinctLetterCount();

        search();
    }
}
