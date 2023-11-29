

# example of using arguments to a script
echo "Starting batch run at Saucelabs.com"

robot -v  REMOTE_URL:http://macchu77:b365eb9a-f746-4ded-9189-bb1ce964f30d@ondemand.saucelabs.com:80/wd/hub -d Results/MacChrome  -v  DESIRED_CAPABILITIES:"build:mactest1,platform:macOS 12,browserName:chrome,version:latest"  Tests/ContentSearch.robot  &


robot -v  REMOTE_URL:http://macchu77:b365eb9a-f746-4ded-9189-bb1ce964f30d@ondemand.saucelabs.com:80/wd/hub -d Results/MacSafari -v  DESIRED_CAPABILITIES:"build:mactest2,platform:macOS 12,browserName:safari,version:latest"  Tests/ContentSearch.robot  &


robot -v  REMOTE_URL:http://macchu77:b365eb9a-f746-4ded-9189-bb1ce964f30d@ondemand.saucelabs.com:80/wd/hub -d Results/WinFirefox -v  DESIRED_CAPABILITIES:"build:mactest2,platform:Windows 11,browserName:firefox,version:latest"  Tests/ContentSearch.robot