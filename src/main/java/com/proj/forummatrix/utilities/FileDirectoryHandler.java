package com.proj.forummatrix.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Amendra Shrestha
 */
public class FileDirectoryHandler {

    public static List<String> list = new ArrayList<>();

    /**
     * checks directory name and finds lists of files
     *
     * @param fname
     */
    // so from other class you can call, Directory.getList() method which will return all the list of file path
    public void rootlist(String fname) {
        File filenames = new File(fname);

        try {
            if (!filenames.isDirectory()) {
                getList().add(fname); // add all the file path to array list
                return;
            }
            String filelists[] = filenames.list();
            for (int i = 0; i < filelists.length; i++) {
                rootlist(fname + File.separator + filelists[i]);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    /**
     * @return the list
     */
    public static List<String> getList() {
        return list;
    }

    /**
     * @param aList the list to set
     */
    public static void setList(List<String> aList) {
        list = aList;
    }

}
