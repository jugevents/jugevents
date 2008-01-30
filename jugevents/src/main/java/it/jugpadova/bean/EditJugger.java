// Copyright 2006-2007 The JUG Events Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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

    private static final long serialVersionUID = -2868414967591782783L;
    @CascadeValidation
	private Jugger jugger;
    private RequireReliability requireReliability;
    private Boolean reliable = Boolean.FALSE;
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

    public Boolean getReliable() {
        return reliable;
    }

    public void setReliable(Boolean reliable) {
        this.reliable = reliable;
    }

    public RequireReliability getRequireReliability() {
        return requireReliability;
    }

    public void setRequireReliability(RequireReliability requireReliability) {
        this.requireReliability = requireReliability;
    }
}
