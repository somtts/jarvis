/**
 *  Copyright 2016 Yannick Roffin
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
package org.jarvis.core.services.cache;

import javax.annotation.PostConstruct;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.CacheManagerBuilder;
import org.ehcache.config.CacheConfigurationBuilder;
import org.jarvis.core.resources.ResourceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * main daemon
 */
@Component
@PropertySource("classpath:server.properties")
public class CoreEhcacheManager {

	protected Logger logger = LoggerFactory.getLogger(CoreEhcacheManager.class);

	protected CacheManager cacheManager;
	protected Cache<String, Object> preConfigured;
	protected Cache<String, Object> cache;
	
	/**
	 * start component
	 */
	@PostConstruct
	public void init() {
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("preConfigured",
						CacheConfigurationBuilder.newCacheConfigurationBuilder().buildConfig(String.class, Object.class))
				.build(true);

		preConfigured = cacheManager.getCache("preConfigured", String.class, Object.class);

		cache = cacheManager.createCache("myCache",
				CacheConfigurationBuilder.newCacheConfigurationBuilder().buildConfig(String.class, Object.class));
	}

	/**
	 * @param key
	 * @param value
	 * @return ResourceData
	 */
	public ResourceData put(String key, ResourceData value) {
		cache.put(key, value);
		return value;
	}

	/**
	 * @param key
	 * @return boolean
	 */
	public boolean contain(String key) {
		return cache.containsKey(key);
	}

	/**
	 * @param key
	 * @return ResourceData
	 */
	public ResourceData get(String key) {
		return (ResourceData) cache.get(key);
	}
}
