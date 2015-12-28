/* 
 * Copyright 2014 Yannick Roffin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

/* Services */

var myAppServices = angular.module('JarvisApp.services.filter', []);

/**
 * filterService
 */
myAppServices.factory('filterService', function(Restangular) {
  var base = {
		  iot: function(element) {
			  return {
	          	'id':element.id,
	        	'name':element.name,
	        	'owner':element.owner,
	        	'visible':element.visible,
	        	'icon':element.icon,
	        	'tagColor':element.tagColor,
	        	'tagTextColor':element.tagTextColor
			  }
		  },
		  plugin: function(element) {
			  return {
	          	'id':element.id,
	        	'name':element.name,
	        	'owner':element.owner,
	        	'type':element.type,
	        	'icon':element.icon,
	        	'active':element.active,
	        	'visible':element.visible
			  }
		  },
		  script: function(element) {
			  return {
	          	'id':element.id,
	        	'name':element.name,
	        	'owner':element.owner,
	        	'type':element.type,
	        	'icon':element.icon,
	        	'active':element.active,
	        	'visible':element.visible
			  }
		  }
  }
  return base;
});
