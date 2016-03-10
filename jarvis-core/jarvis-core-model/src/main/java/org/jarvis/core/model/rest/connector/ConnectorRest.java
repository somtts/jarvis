/**
 *  Copyright 2015 Yannick Roffin
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.jarvis.core.model.rest.connector;

import org.jarvis.core.model.rest.GenericEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * rest connector
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnectorRest extends GenericEntity {
	/**
	 * name
	 */
	@JsonProperty("name")
	public String name;
	/**
	 * icon
	 */
	@JsonProperty("icon")
	public String icon;
	/**
	 * href
	 */
	@JsonProperty("adress")
	public String adress;
	/**
	 * isRenderer
	 */
	@JsonProperty("isRenderer")
	public boolean isRenderer;
	/**
	 * isSensor
	 */
	@JsonProperty("isSensor")
	public boolean isSensor;
	/**
	 * canAnswer
	 */
	@JsonProperty("canAnswer")
	public boolean canAnswer;
}
