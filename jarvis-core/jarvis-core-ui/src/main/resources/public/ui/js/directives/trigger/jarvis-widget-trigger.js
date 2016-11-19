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

/* Ctrls */

angular.module('jarvis.directives.trigger', ['JarvisApp.services'])
.controller('jarvisWidgetTriggersCtrl', 
		[ '$scope', '$log', 'genericScopeService', 'jarvisWidgetTriggerService',
		function($scope, $log, genericScopeService, componentService){
	/**
	 * declare generic scope resource (and inject it in scope)
	 */
	genericScopeService.scope.resources(
			function(entities) {
				$scope.triggers = entities;
			},
			function() {
				return $scope.triggers;
			},
			$scope, 
			'triggers', 
			componentService.trigger,
			{
    			name: "trigger name",
    			icon: "settings_remote"
    		}
	);
}])
.controller('jarvisWidgetTriggerCtrl',
		[ '$scope', '$log', '$stateParams', 'genericResourceService', 'genericScopeService', 'genericPickerService', 'jarvisWidgetTriggerService', 'toastService',
	function($scope, $log, $stateParams, genericResourceService, genericScopeService, genericPickerService, componentService, toastService){
	/**
	 * declare generic scope resource (and inject it in scope)
	 */
	genericScopeService.scope.resource(
			$scope, 
			'trigger', 
			'triggers', 
			componentService.trigger
	);
	/**
	 * declare links
	 */
	$scope.links = {
			crons: {}
	}
	/**
	 * declare action links
	 */
	genericScopeService.scope.resourceLink(
			function() {
				return $scope.crons;
			},
			$scope.links.crons,
			'cron',
			'crons',
			componentService.trigger, 
			componentService.crons, 
			{'order':'1'},
			$stateParams.id
	);
    /**
     * execute this trigger
     */
    $scope.execute = function(trigger) {
    	componentService.trigger.task(trigger.id, 'execute',  {}, function(data) {
   	    	toastService.info('trigger ' + trigger.name + '#' + trigger.id + ' executed');
	    }, toastService.failure);
    }
    /**
     * loading
     */
    $scope.load = function() {
  		$scope.activeTab = $stateParams.tab;

  		/**
		 * get current trigger
		 */
    	genericResourceService.scope.entity.get($stateParams.id, function(update) {$scope.trigger=update}, componentService.trigger);
	
		/**
		 * get all crontabs
		 */
    	$scope.crons = [];
    	genericResourceService.scope.collections.findAll('crons', $stateParams.id, $scope.crons, componentService.crons);

    	$log.info('trigger-ctrl', $scope.trigger);
    }
}])
.factory('jarvisWidgetTriggerService', [ 'genericResourceService', function(genericResourceService) {
	return {
		trigger: genericResourceService.crud(['triggers']),
		crons : genericResourceService.links(['triggers'], ['crons']),
	}
}])
.directive('jarvisWidgetTriggers', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/trigger/jarvis-widget-triggers.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisTriggers', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/trigger/partials/jarvis-triggers.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisWidgetTrigger', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/trigger/jarvis-widget-trigger.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisTriggerGeneral', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/trigger/partials/jarvis-trigger-general.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisTriggerCron', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/trigger/partials/jarvis-trigger-cron.html',
    link: function(scope, element, attrs) {
    }
  }
}])
.directive('jarvisTriggerLink', [ '$log', '$stateParams', function ($log, $stateParams) {
  return {
    restrict: 'E',
    templateUrl: '/ui/js/directives/trigger/partials/jarvis-trigger-link.html',
    link: function(scope, element, attrs) {
    }
  }
}])
;
