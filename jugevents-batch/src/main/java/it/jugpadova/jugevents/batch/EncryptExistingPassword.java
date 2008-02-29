/**
 * 
 */
package it.jugpadova.jugevents.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
// import org.acegisecurity.providers.encoding.Md5PasswordEncoder;

/**
 * Batch to encrypt(md5) plain text password existing in jugevents 
 * database. It has been implemented using plain jdbc.
 * @author Enrico Giurin
 *
 */
public class EncryptExistingPassword {

    private static Properties properties = null;
    private static MessageDigest md = null;

    static {
        // Read properties file.
        properties = new Properties();
        try {
            properties.load(new FileInputStream("batch.properties"));
            md = MessageDigest.getInstance("MD5");
        } catch (IOException e) {
            System.err.println("Error while loading batch.properties");
            e.printStackTrace();
            System.exit(1);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error while instantiating the message digest");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void updatePasswords(Connection connection)
            throws Exception {
        String selectUsers = "select id, username, password from psec_user";
        String updateUser = "update psec_user set password=?, oldpassword=? where id=?";
        Statement selectUsersStmt = null;
        PreparedStatement updateUserStmt = null;
        ResultSet selectUsersRs = null;
        try {
            selectUsersStmt = connection.createStatement();
            updateUserStmt = connection.prepareStatement(updateUser);
            selectUsersRs = selectUsersStmt.executeQuery(selectUsers);
            int rowUpdated = 0;
            while (selectUsersRs.next()) {
                String username = selectUsersRs.getString("username");
                String password = selectUsersRs.getString("password");
                long id = selectUsersRs.getLong("id");
                updateUserStmt.setString(1, encodePassword(password, username));
                updateUserStmt.setString(2, password);
                updateUserStmt.setLong(3, id);
                rowUpdated += updateUserStmt.executeUpdate();
            }
            System.out.println("Updated " + rowUpdated + " rows in table PSEC_USER");
        } finally {
            try {
                selectUsersRs.close();
            } catch (Exception e) {
            }
            try {
                selectUsersStmt.close();
            } catch (Exception e) {
            }
            try {
                updateUserStmt.close();
            } catch (Exception e) {
            }
        }
    }

    private static String encodePassword(String rawPass, String username) throws UnsupportedEncodingException {
        byte[] digest = md.digest((rawPass+"{" + username + "}").getBytes("UTF-8"));
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            result.append(Integer.toHexString(((char)digest[i]) & 0xFF));
        }
        return result.toString();
    }

    /*
    private static String encodePasswordWithAcegi(String rawPass, String username) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.encodePassword(rawPass, username);
    }
    */
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        Connection connection = null;
        System.out.println("Starting updating batch...");
        try {
            Class.forName(properties.getProperty("driver"));
            connection = DriverManager.getConnection((String) properties.get("url"),
                    (String) properties.get("username"),
                    (String) properties.get("password"));
            connection.setAutoCommit(false);
            updatePasswords(connection);
            connection.commit();
        } catch (Exception e) {
            System.err.println("Error while executing the batch");
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (Exception e2) {
            }
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
            }
        }
    }
}//end of class

