/**
 * Copyright 2014 Yannick Roffin.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

var logger = require('blammo').LoggerFactory.getLogger('xmppcli');

'use strict';

var xmppcli = require('node-xmpp-client'), Element = require('node-xmpp-core').Stanza.Element, Message = require('node-xmpp-core').Stanza.Message;

/**
 * start xmppcli
 */
exports.start = function(host, port, args) {
	/**
	 * Internal client
	 */
	var client = new xmppcli.Client({
		jid : args.jid,
		resource : 'jarvis',
		password : 'alice',
		host : host,
		port : port,
		register : true,
		args : args
	})

	/**
	 * store for internal use
	 */
	client.args = args;

	setInterval(function() {
		/**
		 * send presence
		 */
		client.send(new Element('presence', {
			type : 'probe'
		}).c('show').t('chat').up().c('status').t('Hi'));
	}, 10000);

	/**
	 * online status
	 */
	client.on('online', function() {
		logger.info('online');
	})

	/**
	 * error handler
	 */
	client.on('error', function(e) {
		logger.error('error', e);
	})

	/**
	 * stanza handler
	 */
	client.on('stanza', function(stanza) {
		logger.trace('stanza', stanza.attrs.from, stanza.attrs.to, stanza.attrs.type);
		var emitType = null;
		if (stanza.getChild('query')) { // Info query get or set
			emitType = 'query:' + stanza.attrs.type + ':' + stanza.getChild('query').attrs.xmlns;
		} else if (stanza.getName() == "presence") { // Presence
			emitType = 'presence';
		} else if (stanza.getName() == "message") { // Message
			emitType = 'message';
		} else if (stanza.getChild('ping') != null) {
			emitType = 'ping';
		}
		if (emitType) {
			logger.debug(emitType + '!' + stanza);
			this.emit(emitType, stanza);
		} else {
			logger.trace("stanza not found " + stanza);
		}
	})

	/**
	 * presence handler
	 */
	client.on('presence', function(message) {
		logger.debug('presence', client.jid);
	})

	/**
	 * message handler
	 */
	client.on('message', function(message) {
		logger.debug('message');

		/**
		 * ignore everything that isn't a room message
		 */
		if (!message.attrs.type == 'chat') {
			return;
		}

		var body = message.getChild('body');
		/**
		 * message without body is probably a topic change
		 */
		if (!body) {
			return;
		}

		args.message = body.getText();
		var answer = args.fn(args);

		/**
		 * answer
		 */
		client.send(new Message({
			type : 'chat',
			to : message.attrs.from
		}).c('body').t(answer));
	})

	/**
	 * end handler
	 */
	client.on('end', function() {
		logger.info('end');
	})
}
