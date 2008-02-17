//Copyright 2006-2007 The Parancoe Team
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
package it.jugpadova.util;

/**
 * Defines possibly values for the status of ReliabilityRequest po.
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
