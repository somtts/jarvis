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

package org.jarvis.core.resources.api;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import org.jarvis.core.model.rest.ScenarioRest;
import org.jarvis.core.services.ApiScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spark.Request;
import spark.Response;
import spark.Route;

@Component
public class ApiScenarioResources extends ApiResources<ScenarioRest> {

	@Autowired
	ApiScenarioService apiScenarioService;

	@Override
	public List<ScenarioRest> doFindAll() {
    	return apiScenarioService.findAll();
	}

	@Override
	public ScenarioRest doGetById(String id) {
    	return apiScenarioService.getById(id);
	}

	@Override
	public ScenarioRest doCreate(ScenarioRest ScenarioRest) {
		return apiScenarioService.create(ScenarioRest);
	}

	@Override
	public ScenarioRest doUpdate(String id, ScenarioRest ScenarioRest) {
    	return apiScenarioService.update(id, ScenarioRest);
	}

	@Override
	public void mount() {
		/**
		 * mount resources
		 */
		get("/api/Scenarios", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doFindAll(request, response);
		    }
		});
		get("/api/Scenarios/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doGetById(request, ":id", response);
		    }
		});
		post("/api/Scenarios", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doCreate(request, response, ScenarioRest.class);
		    }
		});
		put("/api/Scenarios/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doUpdate(request, ":id", response, ScenarioRest.class);
		    }
		});
	}
}
