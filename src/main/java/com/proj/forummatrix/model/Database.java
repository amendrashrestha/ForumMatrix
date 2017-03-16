package com.proj.forummatrix.model;

import com.proj.forummatrix.utilities.BlogInfo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amendrashrestha
 */
public class Database {

    public static List<BlogInfo> getUsers(String tableName) {
        List<BlogInfo> blogInfo = new ArrayList();
        try (Connection connection = Connect.getConn()) {
            String selectQuery = "SELECT user from " + tableName +
                    " where Id <= 144";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);
            while (result.next()) {
                String blogger = result.getString(1);
                blogInfo.add(new BlogInfo(blogger));
            }
            return blogInfo;
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public interface postFound {
        void onPostFound(String post);
    }

    public static ArrayList<String> getPosts(String User) {
        ArrayList<String> posts = new ArrayList();

        try (Connection connection = Connect.getConn()) {
            String sizeQuery = "SET group_concat_max_len = 100000000000000000;";
            String tableName = "tbl_vnnforum_posts_user_day";

            String selectQuery = "SELECT Text from " + tableName
                    + " WHERE User = " + "\'" + User + "\'";

            Statement statement = connection.createStatement();
            statement.execute(sizeQuery);
            ResultSet result = statement.executeQuery(selectQuery);

            while (result.next()) {
                String post = result.getString(1);
                posts.add(post);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return posts;
    }
    
    public static void getPosts(String User, String tableName, postFound postCallback) {

        try (Connection connection = Connect.getConn()) {
            String sizeQuery = "SET group_concat_max_len = 100000000000000000;";

            String selectQuery = "SELECT Text from " + tableName
                    + " WHERE User = " + "\'" + User + "\'";

            Statement statement = connection.createStatement();
            statement.execute(sizeQuery);
            ResultSet result = statement.executeQuery(selectQuery);

            while (result.next()) {
                String post = result.getString(1);
                postCallback.onPostFound(post);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static List<BlogInfo> getOtherUsers(String validUsersTable) {
        
        List<BlogInfo> blogInfo = new ArrayList();
        try (Connection connection = Connect.getConn()) {
            
            String selectQuery = "SELECT user from " + validUsersTable +
                    " where Id > 144";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);
            while (result.next()) {
                String blogger = result.getString(1);
                blogInfo.add(new BlogInfo(blogger));
            }            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return blogInfo;
    }
}
