/**
 * 
 */
package it.jugpadova.bean;

import java.io.Serializable;

/**
 * Bean for the Jugger search form
 * @author Enrico Giurin
 *
 */
public class JuggerSearch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9049455196122589946L;
	public static final int INVALID_STATUS = -1;
	private String username = null;
	private String JUGName = null;
	private int RRStatus = INVALID_STATUS;
	public int getRRStatus() {
		return RRStatus;
	}
	public void setRRStatus(int status) {
		RRStatus = status;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getJUGName() {
		return JUGName;
	}
	public void setJUGName(String name) {
		JUGName = name;
	}
	 

}
