package com.proj.forummatrix.utilities;

import com.proj.forummatrix.model.Connect;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author amendrashrestha
 */
public class IOReadWrite {

    private static final Pattern UNDESIRABLES = Pattern.compile("[](){},.…”:;؛،؟!?<>%\\/\"\\[]");

    public static String[] getAllFiles(String basePath) {
        FileDirectoryHandler handle = new FileDirectoryHandler();
        handle.rootlist(basePath);
        String[] filesList = new String[FileDirectoryHandler.getList().size()];
        FileDirectoryHandler.list.toArray(filesList);
        return filesList;
    }

    /**
     * Write the feature vector of a user in a file
     *
     * @param toWrite
     * @throws IOException
     */
    public static void writeToFile(Blogger toWrite) throws IOException {

//        String fileName = IOProperties.ENG_TWEET_FEATURE_VECTOR_FILE;
        String fileName = IOProperties.ENG_USER_FEATURE_VECTOR_FILE;
        try (FileWriter fw = new FileWriter(fileName, true) //the true will append the new data
                ) {
            fw.write(toWrite.output.toString().replace("[", "").replace("]", "").replace(",", ","));//appends the string to the file
            fw.write("," + toWrite.blogger + "\n");//appends the string to the file
        }
    }

    /**
     * Write the feature vector of a user in a file
     *
     * @param filePath
     * @param toWrite
     * @throws IOException
     */
    public static void createFileWithHeader(String filePath) throws IOException {
//        String fileName = IOProperties.NON_RAD_TWEET_FEATURE_VECTOR_FILE;
//        String fileName = IOProperties.USER_FEATURE_VECTOR_FILE;

//        String fileName = IOProperties.ENG_TWEET_FEATURE_VECTOR_FILE;
//        String fileName = IOProperties.ENG_USER_FEATURE_VECTOR_FILE;
        try (FileWriter fw = new FileWriter(filePath) //the true will append the new data
                ) {
//            String toWrite = 
//            fw.write(toWrite);//appends the string to the file
        }
    }

    public static void writeToFile(List<Float> toWrite, int userClass) {
//        String fileName = IOProperties.TWEET_FEATURE_VECTOR_FILE;
//        String fileName = IOProperties.USER_FEATURE_VECTOR_FILE;

//        String fileName = IOProperties.ENG_NON_RAD_TWEET_FEATURE_VECTOR_FILE;
        String fileName = IOProperties.ENG_USER_FEATURE_VECTOR_FILE;
        try (FileWriter fw = new FileWriter(fileName, true) //the true will append the new data
                ) {
            fw.write(toWrite.toString().replace("[", "").replace("]", ""));//appends the string to the file
            fw.write("," + userClass + "\n");//appends the string to the file
        } catch (IOException ex) {
            Logger.getLogger(IOReadWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Create a file with feature vectors header
     *
     * @param toWrite
     * @throws IOException
     */
//    public static void createFileWithHeader(List<String> toWrite, String filename) throws IOException {
//        String fileName = IOProperties.TWEET_FEATURE_VECTOR_FILE;
//        try (FileWriter fw = new FileWriter(fileName, true) //the true will append the new data
//                ) {
//            fw.write(toWrite.replace("[", "").replace("]", "").replace(",", "\t"));//appends the string to the file
//            fw.write("\t" + toWrite + "\n");//appends the string to the file
//        }
//    }
    /**
     * Write the list of file with username
     *
     * @param toWrite
     * @param fileName
     * @throws IOException
     */
    public static void writeToFile(String toWrite, String fileName) throws IOException {
//        fileName = IOProperties.NEW_RADICAL_USER_FILEPATH + fileName + ".txt";
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.write(toWrite + "\n");
        }
    }

    public static Float[] concatArrays(Float[]... sources) {

        int length = 0;
        for (Float[] array : sources) {
            length += array.length;
        }
        Float[] result = new Float[length];
        int destPos = 0;
        for (Float[] array : sources) {
            System.arraycopy(array, 0, result, destPos, array.length);
            destPos += array.length;
        }
        return result;
    }

    /**
     * return the normalized count of time
     *
     * @param timeVector
     * @param sum
     * @return
     */
    public static Float[] returnNormalizedVector(int[] timeVector, int sum) {
        Float[] tempTimeVector = new Float[timeVector.length];
        for (int index = 0; index < timeVector.length; index++) {
            float time = timeVector[index];
            float perc = (float) (time / sum);
//            int temp = (int) ((perc * 100) + 0.5);
//            float itemp = temp;
            tempTimeVector[index] = perc;
        }
        return tempTimeVector;
    }

    public static void bigramWordFrequencies(String filepath, String text) {
        HashMap<String, Integer> map = new HashMap<>();
        List<String> auxArray = extractWords(text.trim());

        for (int i = 0; i < auxArray.size() - 1; i++) {
            if (map.containsKey(auxArray.get(i) + " " + auxArray.get(i + 1))) {
                int frequency = map.get(auxArray.get(i) + " " + auxArray.get(i + 1));
                frequency++;
                map.put(auxArray.get(i) + " " + auxArray.get(i + 1), frequency);
            } else {
                map.put(auxArray.get(i) + " " + auxArray.get(i + 1), 1);
            }
        }

        map = sortByComparator(map, false);

        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            try {
                String key = entry.getKey();
                writeToFile(key, filepath);
                i++;
                if (i == 200) {
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(IOReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void bigramLettterFrequency(String filepath, String text) {
        HashMap<String, Integer> map = new HashMap<>();

        String finalText = text.replaceAll("\\s+", "");
        finalText = removePunct(finalText);
        char[] charArray = finalText.toCharArray();

        for (int i = 0; i < charArray.length - 1; i++) {
            if (map.containsKey(charArray[i] + "" + charArray[i + 1])) {
                int frequency = map.get(charArray[i] + "" + charArray[i + 1]);
                frequency++;
                map.put(charArray[i] + "" + charArray[i + 1], frequency);
            } else {
                map.put(charArray[i] + "" + charArray[i + 1], 1);
            }
        }

        map = sortByComparator(map, false);
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            try {
                String key = entry.getKey();
                writeToFile(key, filepath);
                i++;
                if (i == 200) {
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(IOReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void wordsFrequencies(String filename, String text) {

        HashMap<String, Integer> map = new HashMap<>();

        text = removeFunctionWord(text);
        List<String> auxArray = extractWords(text.trim());
        //System.out.println(auxArray);

        for (int i = 0; i < auxArray.size(); i++) {

            if (map.containsKey(auxArray.get(i))) {

                int frequency = map.get(auxArray.get(i));
                frequency++;
                map.put(auxArray.get(i), frequency);
            } else {
                map.put(auxArray.get(i), 1);
            }
        }

        map = sortByComparator(map, false);

        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            try {
                String key = entry.getKey();
                writeToFile(key, filename);
                i++;
                if (i == 200) {
                    break;
                }
            } catch (IOException ex) {
                Logger.getLogger(IOReadWrite.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * sort the hashmap wrt to key in descending order
     *
     * @param unsortMap
     * @param order
     * @return
     */
    private static HashMap<String, Integer> sortByComparator(HashMap<String, Integer> unsortMap, final boolean order) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());
        // Sorting the list based on values
        Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        list.stream().forEach((entry) -> {
            sortedMap.put(entry.getKey(), entry.getValue());
        });

        return sortedMap;
    }

    public static String removeFunctionWord(String text) {
        ArrayList<String> funcWordList = loadLiwcWord(IOProperties.LIWC_ENGLISH_WORD_FILEPATH, "Function");
        Pattern p;
        for (String single_Func_Word : funcWordList) {
            if (single_Func_Word.contains("*")) {
                String new_text = single_Func_Word.replace("*", "");
                String sPattern = "(?i)\\w*" + Pattern.quote(new_text) + "\\w*";

                p = Pattern.compile(sPattern);
            } else {
                p = Pattern.compile("\\b" + Pattern.quote(single_Func_Word) + "\\s");
            }

            Matcher m = p.matcher(text);
            while (m.find()) {
//                    text = text.replaceAll("\\b" + single_Func_Word + "[a-z]*\\s", "");
                text = text.replaceAll(m.group(), "");
                break;
            }
        }
        return text;
    }

    /**
     * load LIWC words into global hash map converting them in lower case
     *
     * @param filePath
     * @param categoryFileName
     * @return
     */
    public static ArrayList<String> loadLiwcWord(String filePath, String categoryFileName) {
        ArrayList<String> wordList = new ArrayList();
        try {
            Scanner scanner = new Scanner(new FileReader(filePath + "/" + categoryFileName));

            while (scanner.hasNextLine()) {
                String[] columns = scanner.nextLine().split(",");
                wordList.add(columns[0].toLowerCase().trim());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IOReadWrite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wordList;
    }

    /**
     * remove RT tag and user mention from tweet
     *
     * @param text
     * @return
     */
    public static String removeRetweetTag(String text) {

        String lastText = text;
        String[] tweet = text.split(" ");
        if (tweet[0].equals("rt") || tweet[0].equals("\'rt")) {
            lastText = "";
            for (int i = 2; i < tweet.length; i++) {
                lastText = lastText + tweet[i] + " ";
            }
        }
        return lastText;
    }

    /**
     * remove URLs from the tweets
     *
     * @param text
     * @return
     */
    public static String removeUrl(String text) {
        text = text.replaceAll("(https?|ftp|file|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
        return text;
    }

    private static List<String> extractHashTag(String text) {
        ArrayList<String> hashtags = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.startsWith("#")) {
                hashtags.add(token);
            }
        }
        return hashtags;
    }

    public static String filterPost(String post) {
        post = StringEscapeUtils.unescapeHtml(post);
        return post;
    }

    public static String removePunct(String x) {
        x = UNDESIRABLES.matcher(x).replaceAll("");
        x = x.replace("\\", "");
        return x;
    }

    public static List<String> extractWords(String text) {
        text = text.toLowerCase();
        List<String> wordList = new ArrayList<>();
        String[] words = text.split("\\s+");

        wordList.addAll(Arrays.asList(words));
        return wordList;
    }

    public static List<String> removeHashTags(String text) {
        List<String> wordList = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(text);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.startsWith("#")) {
                wordList.add(token);
            }
        }
        return wordList;
    }

}
