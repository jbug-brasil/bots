#!/bin/env node

//OpenShift sample Node application
var express = require('express');
var fs      = require('fs');
var irc = require('irc');

var search_url = 'http://search.jboss.org/#!q=';
var community_url = 'https://community.jboss.org/search.jspa?q=';

// IRC bot

var bot = new irc.Client('chat.freenode.net', process.env.OPENSHIFT_APP_NAME || 'jbugbrbot', {
    channels: ['#jbug-brasil'],
    port: 8001,
    debug: true
});

bot.addListener('message', function(from, to, message) {
	if(message.indexOf('jbugbrasilbot') > -1) { // Mandaram msg pra ele
		if(message == 'Olá jbugbrasilbot, tudo bem?') {
			bot.say(to, 'Eu estou ótimo ' + from + '! E você? Como vai?');
		} else if(message.indexOf('olá') > -1) { // msg de reconhecimento
			bot.say(to, 'Olá! Eu sou o bot do JBUG:Brasil. Muito prazer!');
		} else if(message.indexOf('pesquise por:') > -1) {
			var query = message.split(':')[2].split(' ');
			var url1 = search_url;
			var url2 = community_url;
			for(i = 0; i < query.length; i++) {
				url1 += query[i] + '+';
				url2 += query[i] + '+';
			}
			bot.say(to, 'Será que esse link ajuda? ' + url1);
			bot.say(to, 'Ou esse? ' + url2);
		} else if(message.indexOf('Obrigado!') || message.indexOf('Valeu')) {
			bot.say(to, 'Sem problemas! Sempre que precisar! =D');
		} else {
			bot.say(to, 'Desculpe, mas não entendi... =(');
		}
	}
	
	if(message.indexOf(/[A-Z]-[0-9]{3,4}/)) {
		bot.say(to, '');
	}
	
});

bot.addListener('error', function(message) {
    console.log('error: ', message);
});




/**
 * Define the sample application.
 */
var SampleApp = function() {

    // Scope.
    var self = this;


    /* ================================================================ */
    /* Helper functions. */
    /* ================================================================ */

    /**
	 * Set up server IP address and port # using env variables/defaults.
	 */
    self.setupVariables = function() {
        // Set the environment variables we need.
        self.ipaddress = process.env.OPENSHIFT_NODEJS_IP;
        self.port      = process.env.OPENSHIFT_NODEJS_PORT || 8080;

        if (typeof self.ipaddress === "undefined") {
            // Log errors on OpenShift but continue w/ 127.0.0.1 - this
            // allows us to run/test the app locally.
            console.warn('No OPENSHIFT_INTERNAL_IP var, using 127.0.0.1');
            self.ipaddress = "127.0.0.1";
        };
    };


    /**
	 * Populate the cache.
	 */
    self.populateCache = function() {
        if (typeof self.zcache === "undefined") {
            self.zcache = { 'index.html': '' };
        }

        // Local cache for static content.
        self.zcache['index.html'] = fs.readFileSync('./index.html');
    };


    /**
	 * Retrieve entry (content) from cache.
	 * 
	 * @param {string}
	 *            key Key identifying content to retrieve from cache.
	 */
    self.cache_get = function(key) { return self.zcache[key]; };


    /**
	 * terminator === the termination handler Terminate server on receipt of the
	 * specified signal.
	 * 
	 * @param {string}
	 *            sig Signal to terminate on.
	 */
    self.terminator = function(sig){
        if (typeof sig === "string") {
           console.log('%s: Received %s - terminating sample app ...',
                       Date(Date.now()), sig);
           process.exit(1);
        }
        console.log('%s: Node server stopped.', Date(Date.now()) );
    };


    /**
	 * Setup termination handlers (for exit and a list of signals).
	 */
    self.setupTerminationHandlers = function(){
        // Process on exit and signals.
        process.on('exit', function() { self.terminator(); });

        // Removed 'SIGPIPE' from the list - bugz 852598.
        ['SIGHUP', 'SIGINT', 'SIGQUIT', 'SIGILL', 'SIGTRAP', 'SIGABRT',
         'SIGBUS', 'SIGFPE', 'SIGUSR1', 'SIGSEGV', 'SIGUSR2', 'SIGTERM'
        ].forEach(function(element, index, array) {
            process.on(element, function() { self.terminator(element); });
        });
    };


    /* ================================================================ */
    /* App server functions (main app logic here). */
    /* ================================================================ */

    /**
	 * Create the routing table entries + handlers for the application.
	 */
    self.createRoutes = function() {
        self.routes = { };

        // Routes for /health, /asciimo and /
        self.routes['/health'] = function(req, res) {
            res.send('1');
        };

        self.routes['/asciimo'] = function(req, res) {
            var link = "http://i.imgur.com/kmbjB.png";
            res.send("<html><body><img src='" + link + "'></body></html>");
        };

        self.routes['/'] = function(req, res) {
            res.setHeader('Content-Type', 'text/html');
            res.send(self.cache_get('index.html') );
        };
    };


    /**
	 * Initialize the server (express) and create the routes and register the
	 * handlers.
	 */
    self.initializeServer = function() {
        self.createRoutes();
        self.app = express.createServer();

        // Add handlers for the app (from the routes).
        for (var r in self.routes) {
            self.app.get(r, self.routes[r]);
        }
    };


    /**
	 * Initializes the sample application.
	 */
    self.initialize = function() {
        self.setupVariables();
        self.populateCache();
        self.setupTerminationHandlers();

        // Create the express server and routes.
        self.initializeServer();
    };


    /**
	 * Start the server (starts up the sample application).
	 */
    self.start = function() {
        // Start the app on the specific interface (and port).
        self.app.listen(self.port, self.ipaddress, function() {
            console.log('%s: Node server started on %s:%d ...',
                        Date(Date.now() ), self.ipaddress, self.port);
        });
    };

};   /* Sample Application. */



/**
 * main(): Main code.
 */
var zapp = new SampleApp();
zapp.initialize();
zapp.start();

