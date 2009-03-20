/**
 * 
 */
package it.jugpadova.bean;

import java.io.Serializable;

import it.jugpadova.po.Speaker;

/**
 * @author Enrico Giurin
 *
 */
public class SpeakerBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Speaker speaker;
	public SpeakerBean(Speaker speaker) 
	{
		if(speaker == null)
		{
			throw new IllegalArgumentException("Speaker cannot be null!");
		}
		this.speaker = speaker;
	}
	/**
	 * Two instances of SpeakerBean are equals if they have equals (ignorecase) firstName
	 * and LastName.
	 */
	@Override
	public boolean equals(Object obj) {
		if((obj!=null)&&(obj instanceof SpeakerBean)) {
			SpeakerBean sb = (SpeakerBean)obj;
			if(sb.speaker.getFirstName().equalsIgnoreCase(this.speaker.getFirstName())
				&&(sb.speaker.getLastName().equalsIgnoreCase(this.speaker.getLastName())))
				return true;
			
		}
			
		return false;
	}
	@Override
	public int hashCode() {
		int result = 17;
		result = 37*result + speaker.getFirstName().hashCode();
		result = 37*result + speaker.getLastName().hashCode();
		return result;

	}
	
	
	

}
