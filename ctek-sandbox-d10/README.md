# Centretek Sandbox: Drupal 10
[ctek-sandbox-d10](https://bitbucket.org/centretek/ctek-sandbox-d10)

## Contents
This repository contains baseline content types and a default Storybook component library for the average healthcare website.

### Content Types
- Homepage
- Doctor
- Location
- Article
- Event
- Service Line
- Condition
- Treatment
- Basic Page


## Prerequisite
  1. Install Lando (https://github.com/lando/lando/releases) Version 3.6.4 is currently a stable release that will work with this site. 
  1. Docker Desktop - Do not install this independently as it will get installed by Lando with a stable version that will work with Lando. If installing independently, the version may not be 100% compatible and you may experience issues.

## Local Development
#### Clone Repo & Start Lando
- In Terminal
  - Clone the project from the repo
    - `git clone git@bitbucket.org:centretek/ctek-sandbox-d10.git`
  - `git checkout develop`
  - `lando start`

  
#### Enable CTEK Theme
- Run
	- `lando drush en components emulsify_twig`
	- `lando drush then ctek_theme -y`
	- `lando drush config-set system.theme default ctek_theme -y`
     

#### Start Storybook
- To start Storybook locally, run
      - `lando storybook-local`
  - To view Storybook, open your browser to: https://storybook.ctek.lndo.site:6006

#### Build Storybook Static Folder
- To create the storybook static folder required to run storybook on the server, run
    - `lando storybook-build`

### Import Starter DB
- To import the starter database locally, run
      - `lando import-db`