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

import org.jarvis.core.model.rest.CrontabRest;
import org.jarvis.core.services.ApiCrontabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spark.Request;
import spark.Response;
import spark.Route;

@Component
public class ApiCrontabResources extends ApiResources<CrontabRest> {

	@Autowired
	ApiCrontabService apiCrontabService;

	@Override
	public List<CrontabRest> doFindAll() {
    	return apiCrontabService.findAll();
	}

	@Override
	public CrontabRest doGetById(String id) {
    	return apiCrontabService.getById(id);
	}

	@Override
	public CrontabRest doCreate(CrontabRest CrontabRest) {
		return apiCrontabService.create(CrontabRest);
	}

	@Override
	public CrontabRest doUpdate(String id, CrontabRest CrontabRest) {
    	return apiCrontabService.update(id, CrontabRest);
	}

	@Override
	public void mount() {
		/**
		 * mount resources
		 */
		get("/api/crontabs", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doFindAll(request, response);
		    }
		});
		get("/api/crontabs/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doGetById(request, ":id", response);
		    }
		});
		post("/api/crontabs", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doCreate(request, response, CrontabRest.class);
		    }
		});
		put("/api/crontabs/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doUpdate(request, ":id", response, CrontabRest.class);
		    }
		});
	}
}
