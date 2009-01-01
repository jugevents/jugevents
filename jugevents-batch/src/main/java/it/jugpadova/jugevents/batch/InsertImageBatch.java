/**
 * 
 */
package it.jugpadova.jugevents.batch;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * Basic class to insert images for the speaker functionality in jugevents.
 * @author Enrico Giurin
 *
 */
public class InsertImageBatch {
	 private static Properties properties = null;
	
	static {
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

	
	
	
	/**
	 * Update speaker table inserting lucio.jpg image in all the rows.
	 * @param pst
	 * @param index
	 * @throws Exception
	 */
	private static void fillPreparedStatement(PreparedStatement pst, int index) throws Exception {
	         ByteArrayOutputStream regStore = new ByteArrayOutputStream();
	         ObjectOutputStream regObjectStream = new ObjectOutputStream(regStore);
	         byte[] regBytes = resourceToBytes("/lucio.jpg");
	         regObjectStream.close();
	         regStore.close();
	         ByteArrayInputStream regArrayStream = new ByteArrayInputStream(regBytes);
	         pst.setBinaryStream(index, regArrayStream, regBytes.length);	      
	}// end of method
	
	
	private static void updatePicture(Connection connection)
    throws Exception {

		String updateUser = "update speaker set picture=?";
		PreparedStatement updateUserStmt = null;

		try {
			updateUserStmt = connection.prepareStatement(updateUser);
			fillPreparedStatement(updateUserStmt, 1);
			int rowUpdated = 0;
			rowUpdated += updateUserStmt.executeUpdate();
			System.out.println("Updated " + rowUpdated
					+ " rows in table speaker");
		} finally {

			try {
				updateUserStmt.close();
			} catch (Exception e) {
			}

		}
}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 Connection connection = null;
	        System.out.println("Starting updating batch...");
	        try {
	        	properties.load(new FileInputStream("batch.properties"));
	            Class.forName(properties.getProperty("driver"));
	            connection = DriverManager.getConnection((String) properties.get("url"),
	                    (String) properties.get("username"),
	                    (String) properties.get("password"));
	            connection.setAutoCommit(false);
	            updatePicture(connection);
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
	
	
	
	 /**
     * Reads a resource as stream and converts it into an array of bytes
     * @param path path of the resource.
     * @return
     * @throws IOException
     */
    public static byte[] resourceToBytes(String path) throws IOException {
    	InputStream is = null;
    	ByteArrayOutputStream out = null;
    	byte[] buffer = new byte[10240];
    	int len;
    	try {
    		is = InsertImageBatch.class.getResourceAsStream(path);        	
        	out = new ByteArrayOutputStream(buffer.length);   
        	while((len = is.read(buffer)) >= 0) {
        		out.write(buffer, 0, len);
        	}
        	return out.toByteArray();
            	
		}
		finally {
			is.close();
	    	out.close();
		}    	
    	} 

}
