package edu.touro.mcon364.finalreview.treesandthreads.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * In-class Exercise 1 - Word Frequency Counter (TreeMap + Streams)
 *
 * Scenario: you receive a list of words (already lowercased and cleaned).
 * You need to count how many times each word appears and then answer
 * several questions about those counts - all in sorted order.
 *
 * This exercise practises:
 * - Why TreeMap gives us sorted-key iteration for free.
 * - How Collectors.groupingBy + Collectors.counting builds a frequency map.
 * - How NavigableMap operations (firstKey, lastKey, headMap, tailMap) let us
 *   slice the sorted map without iterating manually.
 * - How a stream pipeline can rank or filter the frequency entries.
 *
 * Before coding, think about:
 * - If we use HashMap instead of TreeMap, which methods would break, and why?
 * - What is the difference between headMap(key) and headMap(key, true)?
 * - Should getTopN return words with the highest count or the lowest count?
 *
 * Requirements:
 * - The constructor receives the list of words to analyze.
 * - buildFrequencyMap() returns a TreeMap<String, Long> where every key is a
 *   unique word and every value is how many times that word appeared.
 * - getTopN(n) returns the n words with the highest frequency, sorted
 *   descending by count. Ties may appear in any order.
 *   Note that you have to sort the frequency map by value, not by key, to get the top N.
 * - getWordsStartingWith(prefix) returns a sorted list of all words whose
 *   first character equals the given prefix character (e.g., 'a').
 * - getMostFrequentInRange(from, to) returns the word with the highest count
 *   among words in the alphabetical range [from, to] inclusive.
 *   Return Optional.empty() if the range is empty.
 *
 * Do not use explicit loops anywhere. Use streams and collectors instead.
 */
public class WordFrequencyCounter {

    private final List<String> words;

    public WordFrequencyCounter(List<String> words) {
        // validate that words is not null
        if (words == null) {
            throw new IllegalArgumentException();
        }
        // store a defensive copy so outside code cannot mutate this object
        this.words = List.copyOf(words);
    }

    /**
     * Counts how many times each word appears.
     * The returned map must be sorted alphabetically by word.
     * @return sorted frequency map
     */
    public TreeMap<String, Long> buildFrequencyMap() {
        if (this.words.isEmpty()) {
            return new TreeMap<>();
        }
        TreeMap<String, Long> map = new TreeMap<>();
        for (String word : words) {
            map.compute(word, (k,v) -> v == null ? 1L : v + 1L);
        }
        return map;
    }

    /**
     * Returns the n most frequent words, highest count first.
     *
     * @param n number of top words to return
     * @return list of words, most frequent first
     */
    public List<String> getTopN(int n) {
        if (n <= 0 || this.words.isEmpty()) {
            return new ArrayList<>();
        }
        TreeMap<String, Long> map = buildFrequencyMap();
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    /**
     * Returns all words whose first letter equals the given prefix letter,
     * in alphabetical order.
     *
     * @param prefix the starting letter (e.g., 'b')
     * @return sorted list of matching words
     */
    public List<String> getWordsStartingWith(char prefix) {
        if (this.words.isEmpty()) {
            return new ArrayList<>();
        }
        TreeMap<String, Long> map = buildFrequencyMap();
        return map.keySet().stream()
                .filter(key -> key.charAt(0) == prefix)
                .toList();
    }

    /**
     * Finds the most frequent word in the alphabetical range [from, to] inclusive.
     *
     *
     * @param from lower bound word (inclusive)
     * @param to   upper bound word (inclusive)
     * @return Optional containing the most frequent word in range, or empty if none
     */
    public Optional<String> getMostFrequentInRange(String from, String to) {
        if (this.words.isEmpty()) {
            return Optional.empty();
        }
        TreeMap<String, Long> map = buildFrequencyMap();
        return map.entrySet().stream()
                .filter(entry -> entry.getKey().compareTo(from) >= 0 && entry.getKey().compareTo(to) <= 0)
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }
}
