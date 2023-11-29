Robot Framework is a generic open source automation framework. It can be used for test automation and robotic process automation (RPA).

# Local Setup

## Prerequisites

In order to run RobotFramework tests, Docker needs to be installed: [https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/)


## Writing Tests
When writing new tests:

1. Create new client folder under the **robot-scripts** directory
2. Create a **Resources** and **Tests** folder
	* 	Under **Resources** add:
		*  Files for common variables
		*  common.robot for shared keywords, setup method, and teardown method
	*  Under **Tests** add:
		*  Test case files

		
## Executing Tests	
1. Start Docker
2. Create your docker image
	`docker build --network=host -t my-robotframework-dockerimage .`
3. Run tests locally
	`docker run -it --rm -v /tmp/.X11-unix:/tmp/.X11-unix \
		-v {path to folder}/qa-automation:/qa-automation \
		--env SAUCE_USERNAME={SauceLabs Username} \
		--env SAUCE_ACCESS_KEY={SauceLabs Access Key} \
		my-robotframework-dockerimage \
		bash -c "robot --variable local:True --outputdir /qa-automation/results/{client folder} /qa-automation/robot-scripts/{client folder}"`	
4. Run tests on SauceLabs
	`docker run -it --rm -v /tmp/.X11-unix:/tmp/.X11-unix \
		-v {path to folder}/qa-automation:/qa-automation \
		--env SAUCE_USERNAME={SauceLabs Username} \
		--env SAUCE_ACCESS_KEY={SauceLabs Access Key} \
		my-robotframework-dockerimage \
		bash -c "robot --variable local:False --outputdir /qa-automation/results/{client folder} /qa-automation/robot-scripts/{client folder}"`	
	
## Test Results
Test results can be viewed in the **qa-automation/results** folder.