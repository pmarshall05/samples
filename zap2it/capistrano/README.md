Overview
=================

Capistrano project for Zap2it TV Listings, Movies, and People webapp deployments on EC2 Servers.

This project uses the capistrano-multistage and capistrano-multiconfig gems.  The multistage gem allows you to customize deployment for multiple environments. The multiconfig gem allows you to customize deployment for multiple projects/applications.

Localhost Configuration
============================
* Create an environment variable named AWS_ACCESS_KEY_ID and set to AWS access key
* Create an environment variable named AWS_SECRET_ACCESS_KEY and set to AWS secret access key
* Create an environment variable named AWS_PEM and set to the path of your aws_hosts.pem file
* Generate a Github SSH key


Environments
=================
* dev
* qa
* qa2
* staging (This represents the stage environment. Stage is a keyword in Capistrano)
* production 


Applications
=================
* core_lib
* tvlistings


Servers
=================
Any server that needs to be deployed to should be added to the appropriate environment recipe (i.e. config/develop/zcore/core_lib/dev.rb)


Setup Tasks
=================
* `cap zcore:core_lib:${environment} deploy:setup` - Prepare remote servers for core_lib deployment
* `cap zcore:tvlistings:${environment} deploy:setup` - Prepare remote servers for tvlistings deployment
* `cap zcore:all:${environment} setup_all` - Sets up both core_lib and tvlistings


Deployment Tasks
=================
Pass in the tag/branch to deploy via the command line.
* `cap zcore:core_lib:${environment} deploy -s git_tag=13.10_ZC3093` - Deploys core_lib
* `cap zcore:tvlistings:${environment} deploy  -s git_tag=13.10_ZC3093` - Deploys tvlistings
* `cap zcore:all:${environment} deploy_all -s git_tag=13.10_ZC3093` - Deploys both core_lib and tvlistings. This task restarts tomcat.


Rollback Tasks
=================
* `cap zcore:core_lib:${environment} deploy:rollback` - Rollback core_lib
* `cap zcore:tvlistings:${environment} deploy:rollback` - Rollback tvlistings
* `cap zcore:all:${environment} rollback_all` - Rollback both core_lib and tvlistings
