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

package org.jarvis.core.model.bean.plugin;

import org.jarvis.core.model.bean.GenericBean;

/**
 * plugin
 */
public class PluginBean extends GenericBean {
	/**
	 * name
	 */
	public String name;
	/**
	 * type
	 */
	public String type;
	/**
	 * icon
	 */
	public String icon;
	/**
	 * owner
	 */
	public String owner;
	/**
	 * active
	 */
	public boolean active;
	/**
	 * visible
	 */
	public boolean visible;
	@Override
	public String toString() {
		return "PluginBean [name=" + name + ", type=" + type + ", icon=" + icon + ", owner=" + owner + ", active="
				+ active + ", visible=" + visible + "]";
	}
}
