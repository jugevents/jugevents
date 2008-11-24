// Copyright 2006-2008 The JUG Events Team
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
package it.jugpadova.dao;

import java.util.List;

import it.jugpadova.po.SpeakerCoreAttributes;

import org.parancoe.persistence.dao.generic.Dao;
import org.parancoe.persistence.dao.generic.GenericDao;

/**
 * Speaker associated to SpeakerCoreAttributes po.
 * @author Enrico Giurin
 *
 */
@Dao(entity=SpeakerCoreAttributes.class)
public interface SpeakerCoreAttributesDao extends GenericDao<SpeakerCoreAttributes, Long> {
	List<SpeakerCoreAttributes> findByFirstNameAndLastName(String firstName, String lastName);

}