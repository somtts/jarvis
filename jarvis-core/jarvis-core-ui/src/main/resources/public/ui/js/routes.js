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

/* Controllers */

angular.module('JarvisApp.routes',['JarvisApp.config'])
    .config( ['$urlRouterProvider', function($urlRouterProvider) {
        /**
         * default state
         */
        $urlRouterProvider.otherwise('/home');
    }])
    .config(['$stateProvider', function($stateProvider) {
        /**
         * now set up the state
         */
        $stateProvider
        .state('token_access', {
            url: '/access_token=:accesToken',
            controller: 'extractTokenCtrl',
            template: ''
        })
        .state('home', {
            url: '/home',
            controller: 'homeCtrl',
            templateUrl: '/ui/js/partials/home/page.html'
        })
        .state('helper-iots', {
            url: '/helper-iots',
            controller: 'helperCtrl',
            templateUrl: '/ui/js/partials/helper/jarvis-commands.svg'
        })
        .state('helper-scenarii', {
            url: '/helper-scenarii',
            controller: 'helperCtrl',
            templateUrl: '/ui/js/partials/helper/jarvis-scenarii.svg'
        })
        .state('events', {
            url: '/events',
            controller: 'eventsCtrl',
            templateUrl: '/ui/js/partials/events/page.html'
        })
        .state('triggers', {
            url: '/triggers',
            controller: 'triggersCtrl',
            templateUrl: '/ui/js/partials/triggers/page.html'
        })
        .state('triggers-by-id', {
            url: '/triggers/:id?tab',
            controller: 'triggerCtrl',
            templateUrl: '/ui/js/partials/triggers/trigger/page.html'
        })
        .state('iots', {
            url: '/iots',
            controller: 'iotsCtrl',
            templateUrl: '/ui/js/partials/iots/page.html'
        })
        .state('iots-by-id', {
            url: '/iots/:id?tab',
            controller: 'iotCtrl',
            templateUrl: '/ui/js/partials/iots/iot/page.html'
        })
        .state('plugins', {
            url: '/plugins',
            controller: 'pluginsCtrl',
            templateUrl: '/ui/js/partials/plugins/page.html'
        })
        .state('plugins-by-id-script', {
            url: '/plugins/scripts/:id?tab',
            controller: 'pluginScriptCtrl',
            templateUrl: '/ui/js/partials/plugins/script/page.html'
        })
        .state('commands', {
            url: '/commands',
            controller: 'commandsCtrl',
            templateUrl: '/ui/js/partials/commands/page.html'
        })
        .state('commands-by-id', {
            url: '/commands/:id',
            controller: 'commandCtrl',
            templateUrl: '/ui/js/partials/commands/command/page.html'
        })
        .state('views', {
            url: '/views',
            controller: 'viewsCtrl',
            templateUrl: '/ui/js/partials/views/page.html'
        })
        .state('views-by-id', {
            url: '/views/:id',
            controller: 'viewCtrl',
            templateUrl: '/ui/js/partials/views/view/page.html'
        })
        .state('configurations', {
            url: '/configurations',
            controller: 'configurationsCtrl',
            templateUrl: '/ui/js/partials/configurations/page.html'
        })
        .state('configurations-by-id', {
            url: '/configurations/:id',
            controller: 'configurationCtrl',
            templateUrl: '/ui/js/partials/configurations/configuration/page.html'
        })
        .state('properties', {
            url: '/properties',
            controller: 'propertiesCtrl',
            templateUrl: '/ui/js/partials/properties/page.html'
        })
        .state('properties-by-id', {
            url: '/properties/:id',
            controller: 'propertyCtrl',
            templateUrl: '/ui/js/partials/properties/property/page.html'
        })
        .state('connectors', {
            url: '/connectors',
            controller: 'connectorsCtrl',
            templateUrl: '/ui/js/partials/connectors/page.html'
        })
        .state('connectors-by-id', {
            url: '/connectors/:id',
            controller: 'connectorCtrl',
            templateUrl: '/ui/js/partials/connectors/connector/page.html'
        })
        .state('snapshots', {
            url: '/snapshots',
            controller: 'snapshotsCtrl',
            templateUrl: '/ui/js/partials/snapshots/page.html'
        })
        .state('snapshots-by-id', {
            url: '/snapshots/:id',
            controller: 'snapshotCtrl',
            templateUrl: '/ui/js/partials/snapshots/snapshot/page.html'
        })
        .state('crons', {
            url: '/crons',
            controller: 'cronsCtrl',
            templateUrl: '/ui/js/partials/crons/page.html'
        })
        .state('crons-by-id', {
            url: '/crons/:id',
            controller: 'cronCtrl',
            templateUrl: '/ui/js/partials/crons/cron/page.html'
        })
        .state('scenarios', {
            url: '/scenarios',
            controller: 'scenariosCtrl',
            templateUrl: '/ui/js/partials/scenarios/page.html'
        })
        .state('scenarios-by-id', {
            url: '/scenarios/:id?tab',
            controller: 'scenarioCtrl',
            templateUrl: '/ui/js/partials/scenarios/scenario/page.html'
        })
        .state('blocks', {
            url: '/blocks',
            controller: 'blocksCtrl',
            templateUrl: '/ui/js/partials/blocks/page.html'
        })
        .state('blocks-by-id', {
            url: '/blocks/:id?tab',
            controller: 'blockCtrl',
            templateUrl: '/ui/js/partials/blocks/block/page.html'
        })
        ;
    }])