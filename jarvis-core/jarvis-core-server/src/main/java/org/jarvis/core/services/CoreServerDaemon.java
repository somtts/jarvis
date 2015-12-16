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

package org.jarvis.core.services;

import org.jarvis.core.resources.CoreResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.jarvis.core.resources.api.ApiJobResources;
import org.jarvis.core.resources.api.ApiClientResources;
import org.jarvis.core.resources.api.ApiLabelResources;
import org.jarvis.core.resources.api.ApiScenarioResources;
import org.jarvis.core.resources.api.ApiConnectorResources;
import org.jarvis.core.resources.api.ApiCrontabResources;

@Component
@PropertySource("classpath:server.properties")
public class CoreServerDaemon {

	@Autowired
	Environment env;

	@Autowired
	CoreResources coreResources;

	@Autowired
	ApiJobResources apiJobResources;

	@Autowired
	ApiClientResources apiClientResources;

	@Autowired
	ApiConnectorResources apiConnectorResources;

	@Autowired
	ApiLabelResources apiLabelResources;

	@Autowired
	ApiCrontabResources apiCrontabResources;

	@Autowired
	ApiScenarioResources apiScenarioResources;

	/**
	 * start component
	 */
	public void server() {
		String iface = env.getProperty("jarvis.server.interface");
		int port = Integer.parseInt(env.getProperty("jarvis.server.port"));
		spark.Spark.ipAddress(iface);

		/**
		 * port
		 */
		spark.Spark.port(port);

		/**
		 * mount resources
		 */
		coreResources.mount();
		apiJobResources.mount();
		apiClientResources.mount();
		apiConnectorResources.mount();
		apiCrontabResources.mount();
		apiLabelResources.mount();
		apiScenarioResources.mount();
	}
}
