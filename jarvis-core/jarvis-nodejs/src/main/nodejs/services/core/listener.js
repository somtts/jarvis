/** 
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

/**
 * Load the TCP Library
 */
var net = require('net');

var kernel = require(__dirname + '/kernel');

/**
 * main listener
 */
exports.start = function(host, port) {
	/**
	 * Keep track of the chat clients
	 */
	kernel.clearClients();

	/**
	 * Start a TCP Server
	 */
	var listener = net.createServer(exports.handler);
	/**
	 * error handler
	 */
	listener.on('error', function(e) {
		if (e.code == 'EADDRINUSE') {
			console.log('Address ' + port + ' in use, retrying...');
			setTimeout(function() {
				listener.listen(port);
			}, port);
		}
	});
	listener.listen(port, host);
	kernel.notify("Listener started done on " + host + ':' + port);
}

/**
 * handler services
 */
exports.handler = function(socket) {
	/**
	 * Error handler
	 */
	socket.on('error', function(e) {
		/**
		 * unable to build exchange protocol with this client
		 */
		console.error(e);
	});

	/**
	 * Identify this client (create) during socket connect phase
	 */
	var descriptor = {
		'id' : socket.remoteAddress + ":" + socket.remotePort,
		'socket' : socket
	};

	/**
	 * Send a nice welcome message and announce
	 */
	kernel.sendMessage({
		'code' : 'welcome',
		'welcome' : {
			'data' : descriptor.id + ' connexion'
		},
		'session' : {
			'client' : {
				'id' : descriptor.id
			}
		}
	}, descriptor, socket);

	/**
	 * store descriptor/socket in context put this new client in the list
	 */
	kernel.addClient(descriptor);

	/**
	 * Handle incoming messages from clients.
	 */
	socket.on('data', function(data) {
		/**
		 * handle this new message
		 */
		console.info("message to parse[%s]", data);
		handle(socket, JSON.parse(data));
	});

	/**
	 * Remove the client from the list when it leaves
	 */
	socket.on('error', function() {
		kernel.removeClient(socket);
	});

	/**
	 * Remove the client from the list when it leaves
	 */
	socket.on('end', function() {
		kernel.removeClient(socket);
	});

	/**
	 * Send a message to all clients
	 */
	function handle(sender, message) {
		/**
		 * handle welcome reply
		 */
		if (message.code == 'welcome') {
			var descriptor = kernel.findDescriptorBySocket(sender).descriptor;
			/**
			 * store element from client in session onto descriptor element
			 */
			descriptor.name = message.session.client.name;
			descriptor.canAnswer = message.session.client.canAnswer;
			descriptor.isRenderer = message.session.client.isRenderer;
			descriptor.isSensor = message.session.client.isSensor;
			kernel.register(descriptor, {
				'id' : -1,
				'name' : 'kernel'
			}, message);
			/**
			 * declare xmppclient
			 */
			kernel.xmppcli({
				fn : kernel.xmppcliAiml,
				jid : descriptor.id + '@jarvis.org',
				descriptorId : descriptor.id
			});
			return;
		}
		/**
		 * handle event
		 */
		if (message.code == 'event') {
			var descriptor = kernel.findDescriptorBySocket(sender).descriptor;
			kernel.register(descriptor, {
				'id' : -1,
				'name' : 'kernel'
			}, message);
			return;
		}
		if (message.code == 'evt') {
			var descriptor = kernel.findDescriptorBySocket(sender).descriptor;
			kernel.register(descriptor, {
				'id' : -1,
				'name' : 'kernel'
			}, message);
			return;
		}
	}
}
