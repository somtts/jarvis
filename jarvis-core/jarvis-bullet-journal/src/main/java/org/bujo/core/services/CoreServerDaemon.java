/**
 *  Copyright 2017 Yannick Roffin
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
package org.bujo.core.services;

import javax.annotation.PostConstruct;

import org.bujo.core.resources.CoreResources;
import org.jarvis.core.SwaggerParser;
import org.jarvis.core.model.bean.config.Oauth2Config;
import org.jarvis.core.security.JarvisAccessLogFilter;
import org.jarvis.core.security.JarvisAuthorizerUsers;
import org.jarvis.core.security.JarvisCoreClient;
import org.jarvis.core.security.JarvisTokenValidationFilter;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * main daemon
 */
@Component
@PropertySources({
	@PropertySource(value = "classpath:server.properties", ignoreResourceNotFound = true),
	@PropertySource(value = "file://${jarvis.user.dir}/config.properties", ignoreResourceNotFound = true)
})
@SwaggerDefinition(host = "192.168.1.12:8082",
info = @Info(
		description = "Jarvis",
		version = "v1.0", //
		title = "Jarvis bujo system",
		contact = @Contact(
				name = "Yannick Roffin", url = "https://yroffin.github.io"
				)
),
schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS },
consumes = { "application/json" },
produces = { "application/json" },
tags = { @Tag(name = "swagger") }
)
public class CoreServerDaemon {
	protected Logger logger = LoggerFactory.getLogger(CoreServerDaemon.class);

	@Autowired
	Environment env;

	@Autowired
	CoreResources coreResources;
	
	@Autowired
	ApiEntryResources apiEntryResources;

	protected ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * start component
	 */
	@PostConstruct
	public void server() {
		
		for(String key : ImmutableList.of(
				"jarvis.user.dir",
				"jarvis.log.dir",
				"jarvis.server.url",
				"jarvis.neo4j.url",
				"jarvis.elasticsearch.url",
				"jarvis.sunset.sunrise.url")) {
			logger.info("{} = {}", key, env.getProperty(key));
		}
		
		String iface = env.getProperty("jarvis.server.interface");
		int port = Integer.parseInt(env.getProperty("jarvis.server.port"));
		spark.Spark.ipAddress(iface);
		spark.Spark.threadPool(Integer.parseInt(env.getProperty("jarvis.server.pool.thread","32")));
		
		/**
		 * port
		 */
		spark.Spark.port(port);

		/**
		 * mount resources
		 */
		if(System.getProperty("profile") != null && System.getProperty("profile").equals("dev")) {
			coreResources.mountLocal();
			spark.Spark.staticFiles.expireTime(1);
		} else {
			coreResources.mountExternal();
			spark.Spark.staticFiles.expireTime(1);
		}
		
		/**
		 * build security config
		 */
		final Clients clients = new Clients(new JarvisCoreClient());
		final Config config = new Config(clients);
		final String[] users = env.getProperty("jarvis.oauth2.users","empty").split(",");
		config.addAuthorizer("usersCheck", new JarvisAuthorizerUsers(users));
		config.setHttpActionAdapter(new DefaultHttpActionAdapter());
		
		/**
		 * all api must be validated with token
		 */
		final String[] excludes = env.getProperty("jarvis.oauth2.excludes","").split(",");
		spark.Spark.before("/api/*", new JarvisTokenValidationFilter(config, "JarvisCoreClient", "securityHeaders,csrfToken,usersCheck", excludes));
		
		/**
		 * ident api
		 */
		spark.Spark.get("/api/profile/me", new Route() {
		    @Override
			public Object handle(Request request, Response response) throws Exception {
		    	/**
		    	 * in exclude mode return a fake profile
		    	 */
		        for(String exclude : excludes) {
		        	if(request.ip().matches(exclude)) {
		                return "{\"attributes\":{\"email\":\"-\"}}";
		        	}
		        }

		        UserProfile userProfile = request.session().attribute("pac4jUserProfile");
				return mapper.writeValueAsString(userProfile);
		    }
		});

		/**
		 * retrieve if credential are needed
		 */
		spark.Spark.get("/api/connect", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
		        /**
		         * no protection on excluded ips
		         */
		        for(String exclude : excludes) {
		        	if(request.ip().matches(exclude)) {
		                return false;
		        	}
		        }
		        return true;
			}			
		});

		/**
		 * retrieve oauth2 client and identity
		 */
		spark.Spark.get("/api/oauth2", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
		    	Oauth2Config oauth2Config = new Oauth2Config();
		    	if(request.queryParamsValues("client")[0].equals("google")) {
			    	oauth2Config.type = "google";
			    	oauth2Config.redirect = request.queryParamsValues("oauth2_redirect_uri")[0];
			    	oauth2Config.key = env.getProperty("jarvis.oauth2.google");
			    	oauth2Config.url = "https://accounts.google.com/o/oauth2/auth?scope=email&client_id="+oauth2Config.key+"&response_type=token&redirect_uri="+oauth2Config.redirect;
		    	}
		    	if(request.queryParamsValues("client")[0].equals("facebook")) {
			    	oauth2Config.type = "facebook";
			    	oauth2Config.redirect = request.queryParamsValues("oauth2_redirect_uri")[0];
			    	oauth2Config.key = env.getProperty("jarvis.oauth2.facebook");
			    	oauth2Config.url = "https://www.facebook.com/dialog/oauth?scope=email&client_id="+oauth2Config.key+"&response_type=token&redirect_uri="+oauth2Config.redirect;
		    	}
				return mapper.writeValueAsString(oauth2Config);
		    }
		});

		/**
		 * all resources
		 */
		apiEntryResources.mount();

		/**
		 * Build swagger json description
		 */
		final String swaggerJson;
		swaggerJson = SwaggerParser.getSwaggerJson("org.jarvis.core");
		spark.Spark.get("/api/swagger", (req, res) -> {
			return swaggerJson;
		});

		spark.Spark.after("/*", new JarvisAccessLogFilter());
	}
	
	protected final Logger accesslog = LoggerFactory.getLogger(JarvisAccessLogFilter.class);
}
