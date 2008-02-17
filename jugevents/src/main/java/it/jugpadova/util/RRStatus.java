/**
 * 
 */
package it.jugpadova.util;

/**
 * @author Enrico Giurin
 *
 */
public enum RRStatus {
	NOT_REQUIRED(0, "NOT REQUIRED"),
	RELIABILITY_REQUIRED(1, "REQUIRED"),
	RELIABILITY_GRANTED(2, "GRANTED"),
	RELIABILITY_NOT_GRANTED(3, "NOT GRANTED"),
	RELIABILITY_REVOKED(4, "REVOKED");
	RRStatus(int value, String description)
	{
		this.value = value;
		this.description = description;			
	}
	public int value = 0;
	public String description = null;

}
