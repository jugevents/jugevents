/**
 * 
 */
package it.jugpadova.bean;

import it.jugpadova.po.Jugger;

import java.io.Serializable;

import org.springmodules.validation.bean.conf.loader.annotation.handler.CascadeValidation;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Expression;

/**
 * Bean for editing jugger.
 * @author Enrico Giurin
 *
 */
public class EditJugger implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2868414967591782783L;

	@CascadeValidation
	private Jugger jugger;
	
	private RequireReliability requireReliability;
	
	private Boolean juggerIsReliable = false;
	
	private String password;
	@Expression("confirmPassword == password")
	private String confirmPassword;
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public Jugger getJugger() {
		return jugger;
	}
	public void setJugger(Jugger jugger) {
		this.jugger = jugger;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getJuggerIsReliable() {
		return juggerIsReliable;
	}
	public void setJuggerIsReliable(Boolean juggerIsReliable) {
		this.juggerIsReliable = juggerIsReliable;
	}
	public RequireReliability getRequireReliability() {
		return requireReliability;
	}
	public void setRequireReliability(RequireReliability requireReliability) {
		this.requireReliability = requireReliability;
	}

}
