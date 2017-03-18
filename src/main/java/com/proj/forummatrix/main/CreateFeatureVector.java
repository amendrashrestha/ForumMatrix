package com.proj.forummatrix.main;

import com.proj.forummatrix.model.Database;
import com.proj.forummatrix.utilities.AbstractThreadProcessor;
import com.proj.forummatrix.utilities.BlogInfo;
import com.proj.forummatrix.utilities.Blogger;
import com.proj.forummatrix.utilities.IOProperties;
import com.proj.forummatrix.utilities.IOReadWrite;
import com.proj.forummatrix.utilities.Table;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amendrashrestha
 */
public class CreateFeatureVector extends AbstractThreadProcessor<BlogInfo, Blogger, Table> {

    public CreateFeatureVector() {
        super(8);
    }

    /*
     * @param user
     * @param tableName
     * @param user @return 
     */
    public static List<Float> createPostFeatureVectorForUser(String user, String tableName) {
        List<List<Float>> tempFvforUser = new ArrayList();
        String fileName = IOProperties.ENG_USER_FEATURE_VECTOR_FILE;
        File fv_file = new File(fileName);

        char[] characters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char[] digits_punc = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '?', '!', ',', ';', ':', '(', ')', '"', '-', '\''};

        if (!fv_file.exists()) {
            IOReadWrite.createFileWithHeader(fileName, characters, digits_punc);
        }

        Database.getPosts(user, tableName, (String post) -> {
//            System.out.println(user);
//            System.out.println(tempFvforUser);
            if (!post.isEmpty()) {
                List<Float> postFeatVector = new ArrayList();

                String filteredPost = IOReadWrite.filterPost(post);
                String removePunc = IOReadWrite.removePunct(filteredPost);

                List<String> filteredWordInPost = IOReadWrite.extractWords(filteredPost);
                List<String> filteredPuncInPost = IOReadWrite.extractWords(removePunc);
                int wordInPostSize = filteredWordInPost.size();
                
                if (filteredPost.length() > 1 && removePunc.length() > 1 && wordInPostSize > 1) {
//                    System.out.println(wordInPostSize);
                    List<Float> wordLength = PostAnalysis.countWordLengths(filteredPuncInPost, wordInPostSize);
//                    System.out.println(wordLength);
                    //English text feature vector
                    List<Float> characterLength = PostAnalysis.countCharactersAZ(removePunc, filteredPost, characters);
//                System.out.println(characterLength);
                    List<Float> specialCharacter = PostAnalysis.countEnglishSpecialCharacters(filteredPost, post, digits_punc);

                    List<String> bigramWords = PostAnalysis.extractWordBigrams(filteredPost);
                    List<String> bigramLetters = PostAnalysis.extractLetterBigrams(filteredPost);

                    List<Float> bigramWord = PostAnalysis.countbigramWords(bigramWords, wordInPostSize);
                    List<Float> bigramLetter = PostAnalysis.countbigramLetters(bigramLetters, wordInPostSize);
                    List<Float> mostFreqWord = PostAnalysis.countMostFreqWords(filteredPuncInPost, wordInPostSize);

                    postFeatVector.addAll(wordLength);
                    postFeatVector.addAll(characterLength);
                    postFeatVector.addAll(specialCharacter);
                    postFeatVector.addAll(bigramWord);
                    postFeatVector.addAll(bigramLetter);
                    postFeatVector.addAll(mostFreqWord);

                    tempFvforUser.add(postFeatVector);
//                    System.out.println(tempFvforUser.size());
                }
            }
        });

        int nrOfFeatures = tempFvforUser.get(0).size();
        List<Float> FVforUser = new ArrayList<>(Collections.nCopies(nrOfFeatures, 0.0f));

        for (int i = 0; i < nrOfFeatures; i++) {
            float value = 0.0f;
            for (int j = 0; j < tempFvforUser.size(); j++) {
                value += tempFvforUser.get(j).get(i);
            }
            value /= tempFvforUser.size(); // normalizing single featureVector count wrt total post
            FVforUser.set(i, value);
        }
        return FVforUser;
    }

    @Override
    public Blogger processInput(BlogInfo user, Table tableName) {

        List<Float> postFV = createPostFeatureVectorForUser(user.Blogger, tableName.Table);
        List<String> Feat_Vect = new ArrayList();

        postFV.stream().map((FV) -> String.valueOf(FV)).forEach((strFV) -> {
            Feat_Vect.add(strFV);
        });

        Feat_Vect.add(0, user.Blogger);

        String radical = "1";
        String nonradical = "0";

        Blogger toWriteIntoFile = new Blogger(Feat_Vect, nonradical);
        return toWriteIntoFile;
    }

    @Override
    public void handleOutput(Blogger output) {
        try {
            IOReadWrite.writeToFile(output);
        } catch (IOException ex) {
            Logger.getLogger(CreateFeatureVector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onWorkerThreadComplete() {
//        System.out.println("Thread Complete");
    }

    @Override
    public void onOutputHandleThreadComplete() {
//        System.out.println("Processing Complete");
    }

}
