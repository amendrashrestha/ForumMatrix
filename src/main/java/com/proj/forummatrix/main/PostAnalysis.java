package com.proj.forummatrix.main;

import com.proj.forummatrix.utilities.IOProperties;
import com.proj.forummatrix.utilities.IOReadWrite;
import static com.proj.forummatrix.utilities.IOReadWrite.extractWords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author amendrashrestha
 */
public class PostAnalysis {

    public static List<String> bigramWords;
    public static List<String> bigramLetters;
    public static List<String> mostFreqWords;

    public static void init(boolean flag, String tableName, String validUsersTable) {
        if (flag) {

            File wordBigramFile = new File(IOProperties.ENG_WORD_BIGRAM_FILEPATH);
            File letterBigramFile = new File(IOProperties.ENG_LETTER_BIGRAM_FILEPATH);
            File mostFreqWordFile = new File(IOProperties.ENG_MOST_FREQ_WORD_FILEPATH);

            if (!wordBigramFile.exists()) {
                System.out.println("Running Word Bigram.....");
                IOReadWrite.bigramWordFrequencies(IOProperties.ENG_WORD_BIGRAM_FILEPATH, tableName, validUsersTable);
            }
            if (!letterBigramFile.exists()) {
                System.out.println("Running LetterBigram.....");
                IOReadWrite.bigramLettterFrequency(IOProperties.ENG_LETTER_BIGRAM_FILEPATH, tableName, validUsersTable);
            }

            if (!mostFreqWordFile.exists()) {
                System.out.println("Running Most frequent word.....");
                IOReadWrite.wordsFrequencies(IOProperties.ENG_MOST_FREQ_WORD_FILEPATH, tableName, validUsersTable);
            }

            //data dependent feature
            loadBigramWords(IOProperties.ENG_WORD_BIGRAM_FILEPATH);
            loadBigramLetter(IOProperties.ENG_LETTER_BIGRAM_FILEPATH);
            loadMostFreqWords(IOProperties.ENG_MOST_FREQ_WORD_FILEPATH);

        } else {
            loadBigramWords(IOProperties.ENG_WORD_BIGRAM_FILEPATH);
            loadBigramLetter(IOProperties.ENG_LETTER_BIGRAM_FILEPATH);
            loadMostFreqWords(IOProperties.ENG_MOST_FREQ_WORD_FILEPATH);
        }
    }

    /**
     * Load the bigram words into list
     *
     * @param path
     */
    private static void loadBigramWords(String path) {
        bigramWords = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                bigramWords.add(strLine);
            }
            br.close();
        } catch (IOException e) {
        }
    }

    /**
     * Load the bigram words into list
     *
     * @param path
     */
    private static void loadBigramLetter(String path) {
        bigramLetters = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                bigramLetters.add(strLine);
            }
            br.close();
        } catch (IOException e) {
        }
    }

    /**
     * Load the most freq words into list
     *
     * @param path
     */
    private static void loadMostFreqWords(String path) {
        mostFreqWords = new ArrayList<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));

            String strLine;
            while ((strLine = br.readLine()) != null) {
                mostFreqWords.add(strLine);
            }
            br.close();
        } catch (IOException e) {
        }
    }

    /**
     * Create a list containing the number of occurrences of the various bigram
     * words in the post (list of extracted words)
     *
     * @param words
     * @param wordSize
     * @return
     */
    public static List<Float> countbigramWords(List<String> words, int wordSize) {
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(bigramWords.size(), 0.0f));	// Initialize to zero
        words.stream().map((word1) -> word1.toLowerCase()).filter((word)
                -> (bigramWords.contains(word))).map((word)
                        -> bigramWords.indexOf(word)).forEach((place) -> {
                    float value = tmpCounter.get(place);
                    value++;
                    tmpCounter.set(place, value);
                });
        // "Normalize" the values by dividing with length of the post (nr of words in the post)
        for (int i = 0; i < tmpCounter.size(); i++) {
            Float wordCount = tmpCounter.get(i);
            if (wordCount != 0) {
                tmpCounter.set(i, wordCount / (float) wordSize);
            }
        }
//        System.out.println("Function Words: " + tmpCounter);
        return tmpCounter;
    }

    /**
     * Create a list containing the number of occurrences of the various bigram
     * letters in the post (list of extracted letters)
     *
     * @param words
     * @param wordSize
     * @return
     */
    public static List<Float> countbigramLetters(List<String> words, int wordSize) {
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(bigramLetters.size(), 0.0f));	// Initialize to zero
        words.stream().map((word1) -> word1.toLowerCase()).filter((word)
                -> (bigramLetters.contains(word))).map((word)
                        -> bigramLetters.indexOf(word)).forEach((place) -> {
                    float value = tmpCounter.get(place);
                    value++;
                    tmpCounter.set(place, value);
                });
        // "Normalize" the values by dividing with length of the post (nr of words in the post)
        for (int i = 0; i < tmpCounter.size(); i++) {
            Float wordCount = tmpCounter.get(i);
            if (wordCount != 0) {
                tmpCounter.set(i, wordCount / (float) wordSize);
            }
        }
//        System.out.println("Function Words: " + tmpCounter);
        return tmpCounter;
    }

    /**
     * Create a list containing the number of occurrences of most freq words in
     * the post (list of extracted words)
     *
     * @param words
     * @param wordSize
     * @return
     */
    public static List<Float> countMostFreqWords(List<String> words, int wordSize) {
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(mostFreqWords.size(), 0.0f));	// Initialize to zero
        words.stream().map((word1) -> word1.toLowerCase()).filter((word)
                -> (mostFreqWords.contains(word))).map((word)
                        -> mostFreqWords.indexOf(word)).forEach((place) -> {
                    float value = tmpCounter.get(place);
                    value++;
                    tmpCounter.set(place, value);
                });
        // "Normalize" the values by dividing with length of the post (nr of words in the post)
        for (int i = 0; i < tmpCounter.size(); i++) {
            Float wordCount = tmpCounter.get(i);
            if (wordCount != 0) {
                tmpCounter.set(i, wordCount / (float) wordSize);
            }
        }
//        System.out.println("Function Words: " + tmpCounter);
        return tmpCounter;
    }

    /**
     * Calculate the count of smilies in post
     *
     * @param words
     * @param wordSize
     * @return
     */
    public static List<Float> countSmiley(List<String> words, int wordSize) {
        String[] ch = {":\')", ":-)", ";-)", ":P", ":D", ":X", "<3", ":)", ";)", ":@", ":*", ":|", ":$", "%)"};
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(ch.length, 0.0f));	// Initialize to zero

        for (int i = 0; i < ch.length; i++) {
//            System.out.println("*********");
//            System.out.println("Smile: " + ch[i]);
            int value = countOccurrencesOfSmiley(words, ch[i]);
            tmpCounter.set(i, (float) value);
        }

        for (int j = 0; j < tmpCounter.size(); j++) {
            tmpCounter.set(j, (tmpCounter.get(j) / (float) wordSize));
        }
//        System.out.println("Smiley: " + tmpCounter);

        return tmpCounter;
    }

    /**
     * Create a list containing the number of occurrences of letters a to z in
     * the text
     *
     * @param post
     * @param realPost
     * @param ch
     * @return
     */
    public static List<Float> countCharactersAZ(String post, String realPost, char[] ch) {
        post = post.toLowerCase();	// Upper or lower case does not matter, so make all letters lower case first...
        
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(ch.length, 0.0f));
        for (int i = 0; i < ch.length; i++) {
            int value = countOccurrences(post, ch[i]);
            tmpCounter.set(i, (float) value);
        }

        // "Normalize" the values by dividing with total nr of characters in the post (excluding white spaces)
        int length = realPost.replaceAll(" ", "").length();
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) length);
        }
//        System.out.println("Characters Words: " + tmpCounter);
        return tmpCounter;
    }

    /**
     * Create a list containing the number of special characters in the text
     *
     * @param post
     * @param realPost
     * @param ch
     * @return
     */
    public static List<Float> countEnglishSpecialCharacters(String post, String realPost, char[] ch) {
        post = post.toLowerCase();	// Upper or lower case does not matter, so make all letters lower case first...
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(ch.length, 0.0f));
        for (int i = 0; i < ch.length; i++) {
            int value = countOccurrences(post, ch[i]);
            tmpCounter.set(i, (float) value);
        }

        // "Normalize" the values by dividing with total nr of characters in the post (excluding whitespaces)
        int length = realPost.replaceAll(" ", "").length();
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) length);
        }
//        System.out.println("Special Character Words: " + tmpCounter);
        return tmpCounter;
    }

    /**
     * Counts the frequency of various word lengths in the list of words.
     *
     * @param words
     * @param wordSize
     * @return
     */
    public static List<Float> countWordLengths(List<String> words, int wordSize) {
        ArrayList<Float> tmpCounter = new ArrayList<>(Collections.nCopies(20, 0.0f));	// Where 20 corresponds to the number of word lengths of interest
        int wordLength;
        for (String word : words) {
            wordLength = word.length();
            // We only care about wordLengths in the interval 1-20
            if (wordLength > 0 && wordLength <= 20) {
                float value = tmpCounter.get(wordLength - 1);	// Observe that we use wordLength-1 as index!
                value++;
                tmpCounter.set(wordLength - 1, value);
            }
        }

        // "Normalize" the values by dividing with length of the post (nr of words in the post)
        for (int i = 0; i < tmpCounter.size(); i++) {
            tmpCounter.set(i, tmpCounter.get(i) / (float) wordSize);
        }
//        System.out.println("Word Length Words: " + tmpCounter);
        return tmpCounter;
    }

    /**
     * Count the number of occurrences of certain character in a String
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    /**
     * Count the number of occurrences of smilies in post
     *
     * @param haystack
     * @param needle
     * @return
     */
    public static int countOccurrencesOfSmiley(List<String> haystack, String needle) {
        int count = 0;

        count = haystack.stream().filter((haystack1)
                -> (haystack1.equals(needle))).map((_item) -> 1).reduce(count, Integer::sum);
        return count;
    }

    /**
     * remove spaces from the text and create letter bigram
     *
     * @param text
     * @return
     */
    public static ArrayList<String> extractLetterBigrams(String text) {
        text = text.toLowerCase();

        ArrayList<String> bigrams = new ArrayList<>();

        text = IOReadWrite.filterPost(text);
//        text = removeRetweetTag(text);
        text = IOReadWrite.removeUrl(text);
        text = IOReadWrite.removePunct(text);
//        List<String> auxArray = extractWords(text.trim());
//        auxArray = IOReadWrite.removeHashTags(text.trim());
        String finalText = text.replaceAll("\\s+", "").replace("[", "").replace("]", "");
        finalText = finalText.replace(",", "");
        char[] charArray = finalText.toCharArray();

        for (int i = 0; i < charArray.length - 1; i++) {
            bigrams.add(charArray[i] + "" + charArray[i + 1]);
        }
        return bigrams;
    }

    public static ArrayList<String> extractWordBigrams(String text) {
        text = text.toLowerCase();

        ArrayList<String> bigrams = new ArrayList<>();
        text = IOReadWrite.filterPost(text);
//        text = removeRetweetTag(text);
        text = IOReadWrite.removeUrl(text);
        text = IOReadWrite.removePunct(text);
        List<String> auxArray = extractWords(text.trim());
//        auxArray = IOReadWrite.removeHashTags(text.trim());

        for (int i = 0; i < auxArray.size() - 1; i++) {
            bigrams.add(auxArray.get(i) + " " + auxArray.get(i + 1));
        }
        return bigrams;
    }

}
