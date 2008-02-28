/**
 * 
 */
package it.jugpadova.jugevents.batch;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.parancoe.plugins.security.User;

/**
 * Batch to encrypt(md5) plain text password existing in jugevents 
 * database. It has been implemented using plain jdbc.
 * @author Enrico Giurin
 *
 */
public class EncryptExistingPassword {
	private static Properties properties = null;
	static
	{
		// Read properties file.
	     properties = new Properties();
	    try {
	        properties.load(new FileInputStream("batch.properties"));
	    } catch (IOException e) {
	    	System.err.println("Error while loading batch.properties");
	    	e.printStackTrace();
	    	System.exit(1);
	    }
	}
	
   private static List<User> retriveAllUsers(Connection connection)
			throws Exception {

		User user = null;
		List<User> result = new ArrayList<User>();
		String sql = "select username, password from psec_user";
		Statement stm = null;
		ResultSet rSet = null;
		try {

			stm = connection.createStatement();
			rSet = stm.executeQuery(sql);
			while (rSet.next()) {
				user = new User();
				user.setPassword(rSet.getString("password"));
				user.setUsername(rSet.getString("username"));
				result.add(user);

			}
			return result;
		} finally {
			try {
				rSet.close();
			} catch (Exception e) {
			}
			try {
				stm.close();
			} catch (Exception e) {
			}
		}
	}
   
   
   private static void updatePasswords(Connection connection, List<User> users)
   throws Exception {
	   
   
	   String sql = "update psec_user set password=?, oldpassword=? where username=?";
	   PreparedStatement pst = null;
	   int rowUpdated = 0;

	   try {

		   pst = connection.prepareStatement(sql);
		   for(User user:users)
		   {
			   pst.setString(1, encodePassword(user.getPassword(), user.getUsername()));
			   pst.setString(2, user.getPassword());
			   pst.setString(3, user.getUsername());
			   
			   pst.executeUpdate();
			   rowUpdated++;
		   }
           System.out.println("Updated "+rowUpdated+" rows in table PSEC_USER");
	   } finally {
		   try {
			   pst.close();
		   } catch (Exception e) {
		   }

	   }
   }
   
   
   private static String encodePassword(String rawPass, String username)
	{
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
       return encoder.encodePassword(rawPass, username);
	}
       
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection 	connection = null;
		System.out.println("Starting updating batch...");
		try {
			Class.forName(properties.getProperty("driver"));
			connection = DriverManager.getConnection((String)properties.get("url"), 
													 (String)properties.get("username"), 
													 (String)properties.get("password"));
			connection.setAutoCommit(false);
			updatePasswords(connection, retriveAllUsers(connection));
			connection.commit();
		} catch (Exception e) {
			System.err.println("Error while executing the batch");
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (Exception e2) {}
			
		}
		finally
		{
			try {connection.close();
			} catch (Exception e) {}			
		}
	}

}//end of class
