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

function main() {
	// Middleware
	var express = require('express');
	var https = require('https');
	var http = require('http');
	var fs = require('fs');
	var blammo = require('blammo');

	// App part
	var routes = require(__dirname + '/routes/routes');
	var servicesConfig = require(__dirname + '/services/config');

	var root = blammo.LoggerFactory.getLogger(blammo.Logger.ROOT_LOGGER_NAME);
	var logger = blammo.LoggerFactory.getLogger('logger1');

	// Default options for this htts server
	var options = {
		key : fs.readFileSync('keys/agent2-key.pem'),
		cert : fs.readFileSync('keys/agent2-cert.pem')
	};

	// Create a service (the app object is just a callback).
	var app = express();
	
	// Activate cookies, sessions and forms
	app.use(express.logger())
	.use(express.static('public'))
	.use(express.favicon(__dirname + '/public/favicon.ico'))
	.use(express.cookieParser())
	.use(express.session({secret: 'secretkey'}))
	.use(express.bodyParser());

	/**
	 * build all routes
	 */
	routes.init(app);

	// Create an HTTP service.
	logger.info('Create an HTTP service');
	var httpServer = http.createServer(app);
	httpServer.listen(80);
	logger.info('Create an HTTP service done');
	// Create an HTTPS service identical to the HTTP service.
	logger.info("Create an HTTPS service identical to the HTTP service");
	var httpsServer = https.createServer(options, app);
	httpsServer.listen(443);
	logger.info("Create an HTTPS service identical to the HTTP service done");
}

main()
