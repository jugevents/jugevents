/**
 * 
 */
package it.jugpadova.util;

/**
 * @author eg
 *
 */
public enum NumOfDaysReminder {
	NOT_ACTIVE(-1, "NOT_ACTIVE"),
	TODAY(0, "TODAY"),
	ONE_DAY(1, "ONE_DAY_BEFORE"),
	TWO_DAYS(2, "TWO_DAYS_BEFORE"),
	TREE_DAYS(3, "TREE_DAYS_BEFORE"),	
	FIVE_DAYS(5, "FIVE_DAYS_BEFORE"),
	ONE_WEEK(7, "ONE_WEEK_BEFORE");	
	
	
	NumOfDaysReminder(int value, String description)
	{
		this.value = value;
		this.description = description;			
	}
	public int value = 0;
	public String description = null;

}
