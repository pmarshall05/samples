<?php

// override config for logging and asset minification
$config['system.logging']['error_level'] = 'verbose';
$config['system.performance']['css']['preprocess'] = FALSE;
$config['system.performance']['js']['preprocess'] = FALSE;
$config['system.performance']['cache']['page']['max_age'] = 0;

$settings['skip_permissions_hardening'] = TRUE;
$settings['container_yamls'][] = $app_root . '/sites/services.dev.yml';
$settings['file_private_path'] = '../private';
$settings['file_temp_path'] = '../tmp';

$landoInfo = json_decode(getenv('LANDO_INFO'));
// Database connection
$database = $landoInfo->database;
$databases['default']['default'] = [
	'database' => $database->creds->database,
	'username' => $database->creds->user,
	'password' => $database->creds->password,
	'host' => $database->internal_connection->host,
	'port' => $database->internal_connection->port,
	'driver' => 'mysql',
	'prefix' => '',
	'collation' => 'utf8mb4_general_ci',
];

// Solr connection details, if exists
// Search API server name to be configured 
$searchApiServer = 'default_solr_server';
if (isset($landoInfo->solr)) {
	$solr = $landoInfo->solr;
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['scheme'] = 'http'; 
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['host'] = $solr->internal_connection->host;
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['port'] = $solr->internal_connection->port;
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['path'] = '/';
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['core'] = $solr->core;
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['username'] = NULL;
	$config['search_api.server.' . $searchApiServer]['backend_config']['connector_config']['password'] = NULL;
}

// Use Mailhog for Lando Emails
$config['symfony_mailer.settings']['default_transport'] = 'mailhog';

// To be configured once site is named
$settings['trusted_host_patterns'] = [];
