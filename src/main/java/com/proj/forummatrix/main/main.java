package com.proj.forummatrix.main;

import com.proj.forummatrix.model.Database;
import com.proj.forummatrix.utilities.Table;

/**
 *
 * @author amendrashrestha
 */
public class main {

    public static void init() {
        String forum = "vnnforum";
        boolean flag = false;
        String tableName = "tbl_" + forum + "_posts_user_day";
        String validUsers = "tbl_" + forum + "_valid_user";

        Table table = new Table();
        table.setTable(tableName);
        
        CreateFeatureVector threadStart = new CreateFeatureVector();

        PostAnalysis.init(flag, tableName, validUsers);

        //for trump users
//        threadStart.startProcessing(Database.getUsers(validUsers), table);
        //for other users
        threadStart.startProcessing(Database.getOtherUsers(validUsers), table);
    }

    public static void main(String args[]) {
        init();
    }

}
