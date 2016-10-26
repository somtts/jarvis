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

package org.jarvis.core.resources.api.connectors;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jarvis.core.exception.TechnicalException;
import org.jarvis.core.exception.TechnicalHttpException;
import org.jarvis.core.exception.TechnicalNotFoundException;
import org.jarvis.core.model.bean.connector.ConnectorBean;
import org.jarvis.core.model.rest.connector.ConnectorRest;
import org.jarvis.core.resources.api.ApiResources;
import org.jarvis.core.resources.api.GenericValue;
import org.jarvis.core.type.GenericMap;
import org.jarvis.core.type.TaskType;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * View resource
 */
@Component
public class ApiConnectorResources extends ApiResources<ConnectorRest,ConnectorBean> {

	protected Client client;

	/**
	 * constructor
	 */
	public ApiConnectorResources() {
		setRestClass(ConnectorRest.class);
		setBeanClass(ConnectorBean.class);

		// create HTTP Client for all call
		this.client = ClientBuilder.newClient();
	}

	@Override
	public void mount() {
		/**
		 * connectors
		 */
		declare(CONNECTOR_RESOURCE);
	}

	@Override
	public GenericValue doRealTask(GenericMap args, TaskType taskType) throws TechnicalException {
		GenericMap result;
		switch(taskType) {
			case REGISTER:
				result = register(args);
				break;
			default:
				result = new GenericMap();
		}
		return new GenericValue(result);
	}

	/**
	 * connectors registration is based on its name
	 * if exist update it, else create it
	 * @param args
	 * @return
	 */
	private GenericMap register(GenericMap args) {
		/**
		 * name is not mandatory
		 */
		String value = (String) args.get("name");
		if(value == null) {
			throw new TechnicalException("No name");
		}
		/**
		 * iterate on elements
		 */
		List<ConnectorBean> beans = doFindByAttributeBean("name", (String) args.get("name"));
		ConnectorBean bean = new ConnectorBean();
		if(beans.size() > 0) {
			/**
			 * found
			 */
			bean.name = (String) copy(args.get("name"), beans.get(0).name);
			bean.adress = (String) copy(args.get("adress"), beans.get(0).adress);
			bean.icon = (String) copy(args.get("icon"), beans.get(0).icon);
			bean.canAnswer = (boolean) copy(args.get("canAnswer"), beans.get(0).canAnswer);
			bean.isRenderer = (boolean) copy(args.get("isRenderer"), beans.get(0).isRenderer);
			bean.isSensor = (boolean) copy(args.get("isSensor"), beans.get(0).isSensor);
			bean.lastAdvertise = DateTime.now();
			try {
				doUpdate(beans.get(0).id, bean);
			} catch (TechnicalNotFoundException e) {
				logger.warn("No such entity {}", beans.get(0));
			}
		} else {
			/**
			 * not found
			 */
			bean.name = (String) copy(args.get("name"), "default");
			bean.adress = (String) copy(args.get("adress"), "http://localhost:80");
			bean.icon = (String) copy(args.get("icon"), "settings_input_antenna");
			bean.canAnswer = (boolean) copy(args.get("canAnswer"), false);
			bean.isRenderer = (boolean) copy(args.get("isRenderer"), false);
			bean.isSensor = (boolean) copy(args.get("isSensor"), false);
			doCreate(bean);
		}
		return args;
	}

	/**
	 * copy value
	 * @param value
	 * @param defaut
	 * @return
	 */
	private Object copy(Object value, Object defaut) {
		if(value != null) {
			return value;
		} else {
			return defaut;
		}
	}

	@Override
	public GenericValue doRealTask(ConnectorBean bean, GenericMap args, TaskType taskType) throws TechnicalException {
		GenericMap result;
		switch(taskType) {
			case PING:
			try {
				result = ping(bean, args, new GenericMap());
			} catch (TechnicalNotFoundException | TechnicalHttpException e) {
				logger.error("Error {}", e);
				throw new TechnicalException(e);
			}
				break;
			default:
				result = new GenericMap();
		}
		return new GenericValue(result);
	}

	private GenericMap ping(ConnectorBean bean, GenericMap args, GenericMap properties) throws TechnicalNotFoundException, TechnicalHttpException {
		/**
		 * build call
		 */
		Response entity = client.target(bean.adress)
		        .path("/api/config")
	            .request(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .acceptEncoding("charset=UTF-8")
	            .get();

		if(entity.getStatus() == 200) {
			GenericMap body = null;
			try {
				body = mapper.readValue(entity.readEntity(String.class), GenericMap.class);
			} catch (IOException e) {
				throw new TechnicalException(e);
			}
			return body;
		} else {
			throw new TechnicalHttpException(entity.getStatus(), bean.adress);
		}
	}
}
