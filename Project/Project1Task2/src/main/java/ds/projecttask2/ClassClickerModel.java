/**
 * Author: Kaizhong Ying
 * Last Modified: Feb 4, 2024
 *
 * The ClassClickerModel gives Servlet the model under the website
 * It illustrates the model which provides the basic logic of the website
 */
package ds.projecttask2;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassClickerModel {
    private final Map<String, Integer> results = new ConcurrentHashMap<>();

    /**
     * The constructor of the model, which will set the count of all options to 0
     */
    public ClassClickerModel() {
        results.put("A",0);
        results.put("B",0);
        results.put("C",0);
        results.put("D",0);
    }

    /**
     * Update the hashmap with choice
     * the value of the choice will increase 1 according to the key value
     * key is the choice provided
     * @param choice the choice provided by user, which is "A","B","C" or "D"
     */

    public void recordChoice(String choice) {
        results.merge(choice, 1, Integer::sum);
    }

    /**
     * The method get the results of the hashmap
     *
     * @return the private hashmap results
     */

    public Map<String, Integer> getResults() {
        return new HashMap<>(results);
    }

    /**
     * this method clear all the result
     * set the hashmap to the default constructor
     */
    public void clearResults() {
        results.clear();
        results.put("A",0);
        results.put("B",0);
        results.put("C",0);
        results.put("D",0);

    }
}
