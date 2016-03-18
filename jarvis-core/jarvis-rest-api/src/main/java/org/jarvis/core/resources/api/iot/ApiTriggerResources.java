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

package org.jarvis.core.resources.api.iot;

import org.jarvis.core.model.bean.scenario.TriggerBean;
import org.jarvis.core.model.rest.scenario.TriggerRest;
import org.jarvis.core.resources.api.ApiResources;
import org.jarvis.core.resources.api.ResourcePair;
import org.jarvis.core.resources.api.href.ApiHrefIotResources;
import org.jarvis.core.resources.api.href.ApiHrefIotScriptPluginResources;
import org.jarvis.core.resources.api.href.ApiHrefPluginCommandResources;
import org.jarvis.core.resources.api.plugins.ApiCommandResources;
import org.jarvis.core.resources.api.plugins.ApiScriptPluginResources;
import org.jarvis.core.type.GenericMap;
import org.jarvis.core.type.TaskType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Trigger resource
 */
@Component
public class ApiTriggerResources extends ApiResources<TriggerRest,TriggerBean> {

	@Autowired
	ApiIotResources apiIotResources;

	@Autowired
	ApiHrefIotResources apiHrefIotResources;

	@Autowired
	ApiHrefIotScriptPluginResources apiHrefIotScriptPluginResources;

	@Autowired
	ApiScriptPluginResources apiScriptPluginResources;

	@Autowired
	ApiCommandResources apiCommandResources;

	@Autowired
	ApiHrefPluginCommandResources apiHrefPluginCommandResources;

	/**
	 * constructor
	 */
	public ApiTriggerResources() {
		setRestClass(TriggerRest.class);
		setBeanClass(TriggerBean.class);
	}

	@Override
	public void mount() {
		/**
		 * scripts
		 */
		declare(TRIGGER_RESOURCE);
	}

	@Override
	public ResourcePair doRealTask(TriggerBean trigger, GenericMap args, TaskType taskType) throws Exception {
		return null;
	}
}