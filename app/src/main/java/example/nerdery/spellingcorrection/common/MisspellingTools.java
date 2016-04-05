package example.nerdery.spellingcorrection.common;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import example.nerdery.spellingcorrection.model.Person;

/**
 * Created by floater on 4/5/16.
 */
public class MisspellingTools {

    private static double INFO_THRESHOLD = .75;

    /**
     * Returns a list of people that are within the threshold of the mutual information they reveal
     * about each other
     *
     * @param searchTerm
     * @param people
     * @return
     */
    public static List<Person> bestMatches(String searchTerm, List<Person> people) {
        HashMap<Person, Double> matches = new HashMap<>();
        double searchEntropy = entropy(searchTerm);
        for (Person p : people) {
            double mutual = mutualInformation(searchTerm, p.getFullName());
            double theoreticalYield = (searchEntropy - mutual) / searchEntropy;
            Log.d("Entropy", "SearchTerm: " + searchEntropy);
            Log.d("Entropy", "Person: " + p.getFullName());
            Log.d("Entropy", "Mutual: " + mutual);
            Log.d("Entropy", "Yield: " + theoreticalYield);
            Log.d("Entropy", "");
            if(theoreticalYield > INFO_THRESHOLD) {
                matches.put(p, mutual);
            }
        }

        return new ArrayList<>(sortByComparator(matches).keySet());
    }


    /**
     * Takes in the matches found from mutual information and then sorts them based on the information left over
     * @param unsortedMap - HashMap from bestMatches
     * @return - sorted HashMap
     */
    private static Map<Person, Double> sortByComparator(Map<Person, Double> unsortedMap) {

        List<Map.Entry<Person, Double>> list = new LinkedList<>(unsortedMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Person, Double>>() {
            @Override
            public int compare(Map.Entry<Person, Double> o1, Map.Entry<Person, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<Person, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<Person, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    /**
     * This method takes in a single Searchable's searchable String and calculates the entropy of said string
     * using the Shannon definition for average information.
     *
     * @param param - a string; Can be a name of a person, company, title, etc
     * @return - the average bits of information that is needed to encoded the word in its current spelling
     */
    private static double entropy(String param) {
        double entropy = 0.0;

        HashMap<Character, Double> letterMap = uniqueLettersWithProportions(param);

        for (Character c : letterMap.keySet()) {
            double currentProportion = letterMap.get(c);
            double inverseProportion = 1.0 / currentProportion;
            double currentLogValue = log(inverseProportion);
            entropy += (currentProportion * currentLogValue);
        }

        return entropy;
    }

    /**
     * Given two variables (words) this calculates the entropy of the searchParam given the knownWord
     * The combined HashMap is used to computed the joint distribution of the words combined
     * @param searchParam - word typed into the autocompletetextview
     * @param knownWord - a single instance of a string from the adapter
     * @return
     */
    private static double conditionalEntropy(String searchParam, String knownWord) {
        HashMap<Character, Double> uniqueSearch = uniqueLettersWithProportions(searchParam);
        HashMap<Character, Double> uniqueKnown = uniqueLettersWithProportions(knownWord);
        HashMap<Character, Double> combined = uniqueLettersWithProportions(searchParam+knownWord);

        double entropy = 0.0;

        for (Character s : uniqueSearch.keySet()) {
            double currentSearchProb = combined.get(s);
            for (Character k : uniqueKnown.keySet()) {
                double currentKnownProb = combined.get(k);
                double jointProb = currentKnownProb * currentSearchProb;

                entropy += (jointProb * log((currentSearchProb / jointProb)));
            }
        }

        return entropy;
    }

    /**
     * Reveals how much information provided from the known word to the searched term.  A value closer to 0 is best
     * as it means that one word supplies the other will all the information it needs.
     *
     * There is a small rounding error when doing these calculations that can account for negative information.
     * This is corrected by taking the absolute value
     * @param searchTerm - what you enter into the autocompletetextview
     * @param knownWord - a single entry from the array adapter
     * @return
     */
    private static double mutualInformation(String searchTerm, String knownWord) {

        double result = entropy(searchTerm) - conditionalEntropy(searchTerm, knownWord);
        result = result * 10000;
        result = Math.round(result);
        result = result / 10000;
        return Math.abs(result);
    }


    /**
     * Takes in a string finds the unique letters and their corresponding proportions.
     *
     * @param word
     * @return
     */
    private static HashMap<Character, Double> uniqueLettersWithProportions(String word) {
        char[] arr = word.toCharArray();
        double length = (double)(word.length());
        HashMap<Character, Integer> letterMap = new HashMap<>();
        HashMap<Character, Double> result = new HashMap<>();

        for (Character c : arr) {
            if(letterMap.containsKey(c)) {
                int currentLetterCount = letterMap.get(c);
                currentLetterCount++;
                letterMap.put(c, currentLetterCount);
            }else {
                letterMap.put(c, 1);
            }
        }

        for(Character c : letterMap.keySet()) {
            double currentProportion = (double)letterMap.get(c) / length;
            result.put(c, currentProportion);
        }

        return result;
    }

    /**
     * uses log base change rule logb(x) = logc(x) / logc(b)
     */
    private static double log(double proportion) {
        return Math.log(proportion) / Math.log(2.0);
    }


    public double getInfoThreshold() {
        return INFO_THRESHOLD;
    }

    public void setInfoThreshold(double infoThreshold) {
        INFO_THRESHOLD = infoThreshold;
    }

}
