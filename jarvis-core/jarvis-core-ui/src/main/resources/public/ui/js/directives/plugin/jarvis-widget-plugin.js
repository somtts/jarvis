/* 
 * Copyright 2016 Yannick Roffin.
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

/* Ctrls */

angular.module('jarvis.directives.plugin', ['JarvisApp.services'])
.controller('pluginsCtrl', 
		[ '$scope', '$log', 'genericScopeService', 'jarvisWidgetPluginService',
	function($scope, $log, genericScopeService, componentService){
	/**
	 * declare generic scope resource (and inject it in scope)
	 */
	genericScopeService.scope.resources(
			function(entities) {
				$scope.plugins = entities;
			},
			function() {
				return $scope.plugins;
			},
			$scope, 
			'plugins/scripts', 
			componentService.scripts,
			{
    			name: "script name",
    			icon: "list",
       			type: "script"
    		}
	);
}])
.controller('pluginScriptCtrl',
		[ '$scope',
		  '$log',
		  '$stateParams',
		  'genericResourceService',
		  'genericScopeService',
		  'genericPickerService',
		  'jarvisWidgetPluginService',
		  'jarvisWidgetDeviceService',
		  'toastService',
	function(
			$scope,
			$log,
			$stateParams,
			genericResourceService,
			genericScopeService,
			genericPickerService,
			componentService,
			deviceService,
			toastService){
	/**
	 * declare generic scope resource (and inject it in scope)
	 */
	genericScopeService.scope.resource(
			$scope, 
			'script', 
			'plugins', 
			componentService.scripts
	);
	/**
	 * declare links
	 */
	$scope.links = {
			commands: {}
	};
	/**
	 * declare generic scope resource link (and inject it in scope)
	 */
	genericScopeService.scope.resourceLink(
			function() {
				return $scope.commands;
			},
			$scope.links.commands, 
			'script', 
			'plugins', 
			componentService.scripts, 
			componentService.commands, 
			{
    			'order':'1',
    			'name':'noname',
       			'nature':'json',
       			'type':'data'
			},
			$stateParams.id
	);
    /**
     * execute these command on server side (only action command)
	 * @param command, the command to be executed
     */
    $scope.execute = function(command) {
    	if(command != undefined && command.id != undefined && command.id != '') {
    		componentService.scripts.task(command.id, 'execute', $scope.rawTestData, function(data) {
       	    	$log.debug('pluginScriptCtrl::execute', command, data);
       	    	$scope.rawoutput = data;
       	    	$scope.output = angular.toJson(data, true);
    	    }, toastService.failure);
    	}
    }
    /**
     * render these command on server side (only data command)
	 * @param command, the command to be executed
     */
    $scope.render = function(command) {
    	if(command != undefined && command.id != undefined && command.id != '') {
    		componentService.scripts.task(command.id, 'render', $scope.rawTestData, function(data) {
       	    	$log.debug('pluginScriptCtrl::render', command, data);
       	    	$scope.rawoutput = data;
       	    	$scope.output = angular.toJson(data, true);
    	    }, toastService.failure);
    	}
    }
    /**
     * fix params
     */
    $scope.submit = function() {
    	$scope.rawTestData = angular.fromJson($scope.editTestData);
    	$scope.jsonTestData = angular.toJson($scope.rawTestData, true);
    }
    /**
     * clear params
     */
    $scope.clear = function() {
    	$scope.rawoutput = {};
    	$scope.output = angular.toJson({}, true);
    	$scope.rawTestData = {};
    	$scope.editTestData = angular.toJson($scope.rawTestData, true);
    	$scope.jsonTestData = angular.toJson($scope.rawTestData, true);
    }
    /**
     * set owner
     */
    $scope.setOwner = function(owner) {
    	$scope.script.owner = owner.id;
    }
    /**
     * load controller
     */
    $scope.load = function() {
    	$scope.activeTab = $stateParams.tab;
    	
    	$scope.clear();
    	
	    /**
	     * init part
	     */
		$scope.combo = {
				booleans: [
		               	   {id: true, value:'True'},
		               	   {id: false,value:'False'}
		        ]
		};
	
		/**
		 * get current script
		 */
    	genericResourceService.scope.entity.get($stateParams.id, function(update) {$scope.script=update}, componentService.scripts);
	
		/**
		 * get all commands
		 */
    	$scope.commands = [];
    	genericResourceService.scope.collections.findAll('commands', $stateParams.id, $scope.commands, componentService.commands);

		/**
		 * find all owner
		 */
    	$scope.combo.owners = [{id: undefined, name: "device.empty"}];
    	genericResourceService.scope.combo.findAll('owner', $scope.combo.owners, deviceService.device);

		$log.info('script-ctrl', $scope.script);
    }
}])
.factory('jarvisWidgetPluginService', 
		[ 'Restangular', 'filterService', 'genericResourceService', 
		  function(Restangular, filterService, genericResourceService) {
	  var plugins = {
		        /**
				 * base services : findAll, delete, put and post
				 */
				findAll: function(callback, failure) {
	                var arr = [];
	                var plugins = ['scripts'];
					var done = _.after(plugins.length, function(loaded) {
						callback(loaded);
					});
					_.forEach(plugins, function(plugin) {
						Restangular.all('plugins').all(plugin).getList().then(function(elements) {
			            	_.forEach(elements, function(element) {
			                    arr.push(filterService.plain(element));
			                });
			            	done(arr);
						},function(errors){
							failure(errors);
						});
					});
				}
	  };
	  return {
		    plugins: plugins,
		    scripts: genericResourceService.crud(['plugins','scripts']),
		    commands : genericResourceService.links(['plugins','scripts'], ['commands'])
	  }
}])
/**
 * plugins
 */
.directive('jarvisWidgetPlugins', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/plugin/jarvis-widget-plugins.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisPlugins', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/plugin/partials/jarvis-plugins.html',
    link: function(scope, element, attrs) {
    }
  }
}])
/**
 * plugin
 */
.directive('jarvisWidgetPlugin', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/plugin/jarvis-widget-plugin.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisPluginGeneral', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/plugin/partials/jarvis-plugin-general.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisPluginResult', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/plugin/partials/jarvis-plugin-result.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisPluginScript', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/plugin/partials/jarvis-plugin-script.html',
    link: function(scope, element, attrs) {
    }
  }
}])
;

