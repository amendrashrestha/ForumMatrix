package com.proj.forummatrix.utilities;

/**
 *
 * @author amendrashrestha
 */
import com.proj.forummatrix.model.RegularExpressionTokenizer;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mohamed Guendouz
 */
public class TFIDFCalculator {

    /**
     * @param doc list of strings
     * @param term String represents a term
     * @return term frequency of term in document
     */
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word)) {
                result++;
            }
        }
        return result / doc.size();
    }

    /**
     * @param docs list of list of strings represents the dataset
     * @param term String represents a term
     * @return the inverse term frequency of term in documents
     */
    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    /**
     * @param doc a text document
     * @param docs all documents
     * @param term term
     * @return the TF-IDF of term
     */
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);

    }

    public interface Tokenizer {

        List<String> tokenize(String text);
    }

    public static void main(String[] args) {

//        List<String> doc1 = Arrays.asList("Lorem", "ipsum", "dolor", "ipsum", "sit", "ipsum");
//        List<String> doc2 = Arrays.asList("Vituperata", "incorrupte", "at", "ipsum", "pro", "quo");
//        List<String> doc3 = Arrays.asList("Has", "persius", "disputationi", "id", "simul");
//        List<List<String>> documents = Arrays.asList(doc1, doc2, doc3);
        
        String text = "This is test";
        String text1 = "This is another test fuck";
        
        List<String> tokenList = Arrays.asList(text.split("\\s+"));
        List<String> tokenList1 = Arrays.asList(text1.split("\\s+"));
        
        List<List<String>> documents = Arrays.asList(tokenList, tokenList1);

        TFIDFCalculator calculator = new TFIDFCalculator();
        double tfidf = calculator.tfIdf(tokenList, documents, "fuck");
        System.out.println("TF-IDF (test) = " + tfidf);

    }

    public static void removeUrl(String text) {
        text = text.replaceAll("(https?|ftp|file|http)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "");
        System.out.println(text);
    }
}
