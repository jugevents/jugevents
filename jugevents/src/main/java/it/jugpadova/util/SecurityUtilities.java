/**
 * 
 */
package it.jugpadova.util;

import org.acegisecurity.Authentication;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;

/**
 * Defines methods for security porpouse
 * 
 * @author Enrico Giurin
 * 
 */
public class SecurityUtilities {
	/**
	 * Returns username of authenticated user.
	 * 
	 * @return
	 */
	public static String getUserAuthentichated() {
		Authentication authentication = org.acegisecurity.context.SecurityContextHolder
				.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName();
		}
		return null;
	}// end of method
	
	/**
	 * Encrypt password (MD5) according to security configuration file defined
	 * in the parancoe plugin security.
	 * It works only if the userPropertyToUse in the <code>ReflectionSaltSource</code> is set
	 * to <code>getUsername</code>.
	 * @param rawPass
	 * @param username
	 * @return
	 */
	public static String encodePassword(String rawPass, String username)
	{
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.encodePassword(rawPass, username);
        
	}

}
