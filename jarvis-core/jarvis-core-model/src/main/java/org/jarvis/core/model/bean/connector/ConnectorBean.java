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

package org.jarvis.core.model.bean.connector;

import org.jarvis.core.model.bean.GenericBean;
import org.joda.time.DateTime;

/**
 * bean connector
 */
public class ConnectorBean extends GenericBean {
	/**
	 * name
	 */
	public String name;
	/**
	 * icon
	 */
	public String icon;
	/**
	 * adress
	 */
	public String adress;
	/**
	 * isRenderer
	 */
	public boolean isRenderer;
	/**
	 * isSensor
	 */
	public boolean isSensor;
	/**
	 * canAnswer
	 */
	public boolean canAnswer;
	/**
	 * lastAdvertise
	 */
	public DateTime lastAdvertise;
}
