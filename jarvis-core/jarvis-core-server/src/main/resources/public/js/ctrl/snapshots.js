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

angular.module('JarvisApp.ctrl.snapshots', ['JarvisApp.services'])
.controller('snapshotsCtrl', 
	function($scope, $log, genericScopeService, snapshotResourceService){
	/**
	 * declare generic scope resource (and inject it in scope)
	 */
	genericScopeService.scope.resources(
			function(entities) {
				$scope.snapshots = entities;
			},
			function() {
				return $scope.snapshots;
			},
			$scope, 
			'snapshots', 
			snapshotResourceService.snapshot,
			{
    			name: "snapshot name",
    			icon: "list"
    		}
	);
})
.controller('snapshotCtrl',
	function($scope, $log, $stateParams, $filter, $http, genericResourceService, genericScopeService, snapshotResourceService, iotResourceService, toastService){
	/**
	 * declare generic scope resource (and inject it in scope)
	 */
	genericScopeService.scope.resource(
			$scope, 
			'snapshot', 
			'snapshots', 
			snapshotResourceService.snapshot);
    /**
     * download current snapshot
	 * @param snapshot, the snapshot to be downloaded
     */
    $scope.download = function(snapshot) {
    	if(snapshot != undefined && snapshot.id != undefined && snapshot.id != '') {
    		snapshotResourceService.snapshot.task(snapshot.id, 'download', {}, function(data) {
    			var fileName = 'export-'+$filter('date')(new Date(), 'yyyyMMdd-HHmmss') + '.json';
                var a = document.createElement("a");
                document.body.appendChild(a);
                a.style = "display: none";
       	    	$log.debug('snapshot', snapshot, data);
       	    	var file = new Blob([angular.toJson(data, true)], {type: 'application/text'});
                var fileURL = window.URL.createObjectURL(file);
                a.href = fileURL;
                a.download = fileName;
                a.click();
    	    }, toastService.failure);
    	}
    }
    /**
     * uploaded current snapshot
	 * @param snapshot, the snapshot to be uploaded
     */
    $scope.loaded = function(snapshot, file) {
    	snapshot.json = atob(file.data.substr(13));
    	$log.debug('loaded', snapshot, file);
    }
    /**
     * uploaded current snapshot
	 * @param snapshot, the snapshot to be uploaded
     */
    $scope.upload = function(id) {
    	$('#'+id).trigger('click');
    }
    /**
     * load this controller
     */
    $scope.load = function() {
		/**
		 * get current snapshot
		 */
    	$scope.snapshots = [];
    	genericResourceService.scope.entity.get($stateParams.id, function(update) {$scope.snapshot=update}, snapshotResourceService.snapshot);

		$log.info('snapshot-ctrl', $scope.snapshots);
    }
})
