// Copyright 2006-2007 The Parancoe Team
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
package it.jugpadova;

import it.jugpadova.blo.EventBo;
import it.jugpadova.blo.EventResourceBo;
import it.jugpadova.blo.FilterBo;
import it.jugpadova.blo.JugBo;
import it.jugpadova.blo.JuggerBo;
import it.jugpadova.blo.ParticipantBo;

import it.jugpadova.blo.SampleBlo;
import it.jugpadova.blo.ServicesBo;

public class Blos {

    public SampleBlo sample;
    public EventBo eventBo;
    public JuggerBo juggerBo;
    public FilterBo filterBo;
    public JugBo jugBo;
    public ServicesBo servicesBo;
    public ParticipantBo participantBo;
    public EventResourceBo eventResourceBo;

    public SampleBlo getSample() {
        return sample;
    }

    public void setSample(SampleBlo sample) {
        this.sample = sample;
    }

    public EventBo getEventBo() {
        return eventBo;
    }

    public void setEventBo(EventBo eventBo) {
        this.eventBo = eventBo;
    }

    public JuggerBo getJuggerBO() {
        return juggerBo;
    }

    public void setJuggerBO(JuggerBo juggerBo) {
        this.juggerBo = juggerBo;
    }

    public FilterBo getFilterBo() {
        return filterBo;
    }

    public void setFilterBo(FilterBo filterBo) {
        this.filterBo = filterBo;
    }

    public JugBo getJugBo() {
        return jugBo;
    }

    public void setJugBo(JugBo jugBo) {
        this.jugBo = jugBo;
    }

    public ServicesBo getServicesBo() {
        return servicesBo;
    }

    public void setServicesBo(ServicesBo servicesBo) {
        this.servicesBo = servicesBo;
    }

    public ParticipantBo getParticipantBo() {
        return participantBo;
    }

    public void setParticipantBo(ParticipantBo participantBo) {
        this.participantBo = participantBo;
    }

    public EventResourceBo getEventResourceBo() {
        return eventResourceBo;
    }

    public void setEventResourceBo(EventResourceBo eventResourceBo) {
        this.eventResourceBo = eventResourceBo;
    }
}
