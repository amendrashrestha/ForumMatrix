package com.proj.forummatrix.main;

import com.proj.forummatrix.model.Database;
import com.proj.forummatrix.utilities.AbstractThreadProcessor;
import com.proj.forummatrix.utilities.BlogInfo;
import com.proj.forummatrix.utilities.Blogger;
import com.proj.forummatrix.utilities.IOReadWrite;
import com.proj.forummatrix.utilities.Table;

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
        

        Database.getPosts(user, tableName, (String post) -> {
            List<Float> postFeatVector = new ArrayList();

            String filteredPost = IOReadWrite.filterPost(post);
            String removePunc = IOReadWrite.removePunct(filteredPost);
            List<String> filteredWordInPost = IOReadWrite.extractWords(filteredPost);
            List<String> filteredPuncInPost = IOReadWrite.extractWords(removePunc);
            int wordInPostSize = filteredWordInPost.size();

            List<Float> wordLength = PostAnalysis.countWordLengths(filteredPuncInPost, wordInPostSize);
            //English text feature vector
            List<Float> characterLength = PostAnalysis.countCharactersAZ(removePunc, removePunc);
            List<Float> specialCharacter = PostAnalysis.countEnglishSpecialCharacters(filteredPost, post);
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

        Blogger toWriteIntoFile = new Blogger(Feat_Vect, radical);
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
