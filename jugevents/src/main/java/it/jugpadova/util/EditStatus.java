/**
 * 
 */
package it.jugpadova.util;

/**
 * @author Enrico Giurin
 *
 */
public enum EditStatus {
	UNMODIFIED(0, "NOT MODIFIED"),
	MODIFIED(1, "MODIFIED"),
	NEW(2, "NEW"),
	DELETED(3, "DELETED");
	
	EditStatus(int value, String description)
	{
		this.value = value;
		this.description = description;			
	}
	public int value = 0;
	public String description = null;

}
