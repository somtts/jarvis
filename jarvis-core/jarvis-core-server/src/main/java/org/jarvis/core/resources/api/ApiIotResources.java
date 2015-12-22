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
import static spark.Spark.delete;

import org.jarvis.core.exception.TechnicalNotFoundException;
import org.jarvis.core.model.bean.IotBean;
import org.jarvis.core.model.rest.GenericEntity;
import org.jarvis.core.model.rest.IotRest;
import org.jarvis.core.resources.api.href.ApiHrefIotResources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * IOT resource
 */
@Component
public class ApiIotResources extends ApiResources<IotRest,IotBean> {

	@Autowired
	ApiHrefIotResources apiHrefResources;

	/**
	 * constructor
	 */
	public ApiIotResources() {
		setRestClass(IotRest.class);
		setBeanClass(IotBean.class);
	}

	@Override
	public void mount() {
		/**
		 * mount resources
		 */
		get("/api/iots", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doFindAll(request, response);
		    }
		});
		get("/api/iots/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doGetById(request, ID, response);
		    }
		});
		post("/api/iots", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doCreate(request, response, IotRest.class);
		    }
		});
		put("/api/iots/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doUpdate(request, ID, response, IotRest.class);
		    }
		});
		delete("/api/iots/:id", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	return doDelete(request, ID, response, IotRest.class);
		    }
		});
		/**
		 * params
		 */
		get("/api/iots/:id/iots", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	try {
		    		IotRest iot = doGetById(request.params(ID));
			    	return mapper.writeValueAsString(apiHrefResources.findAll(iot, IotRest.class));
		    	} catch(TechnicalNotFoundException e) {
		    		response.status(404);
		    		return "";
		    	}
		    }
		});
		put("/api/iots/:id/iots/:iot", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	try {
		    		IotRest iot = doGetById(request.params(ID));
			    	try {
				    	IotRest child = doGetById(request.params(IOT));
				    	GenericEntity instance = apiHrefResources.add(iot, child);
				    	return mapper.writeValueAsString(instance);
			    	} catch(TechnicalNotFoundException e) {
			    		response.status(404);
			    		return "";
			    	}
		    	} catch(TechnicalNotFoundException e) {
		    		response.status(404);
		    		return "";
		    	}
		    }
		});
		delete("/api/jobs/:id/params/:param", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	try {
		    		IotRest job = doGetById(request.params(ID));
			    	try {
			    		IotRest param = doGetById(request.params(IOT));
				    	apiHrefResources.remove(job, param, request.queryParams(INSTANCE));
			    	} catch(TechnicalNotFoundException e) {
			    		response.status(404);
			    		return "";
			    	}
		    	} catch(TechnicalNotFoundException e) {
		    		response.status(404);
		    		return "";
		    	}
		    	return doGetById(request, ID, response);
		    }
		});
	}
}