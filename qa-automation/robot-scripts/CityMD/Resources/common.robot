*** Settings ***
Library     SeleniumLibrary  plugins=SauceLabs
Library     AppiumLibrary
Library     BuiltIn
Library     Process

Variables  ../Resources/variables.py
Variables  ../Resources/mobile.py
Variables  ../Resources/locators.py   # centralized locators variable file

Resource  ../Resources/LandingPage.robot
Resource  ../Resources/TopNav.robot
Resource  ../Resources/SearchResults.robot

*** Variables ***


*** Keywords ***
Start Appium
    Log to Console      Start Appium Server
    Start Process                   
    ...     appium -a localhost -p 4723
    ...     shell=True
    ...     alias=appiumserver 
    ...     stdout=/qa-automation/appium/stdout.log
    ...     stderr=/qa-automation/appium/stderr.log
    Process Should Be Running       appiumserver
    Sleep                           10s


Stop Appium
    Log to Console      Stop Appium Server
    Terminate Process   appiumserver kill=True


Begin Web Test
    Run Keyword If      ${local}    Begin Local Web Test
    ...     ELSE        Begin Remote Web Test
    


Begin Mobile Test
    [Arguments]         ${applicatonType}
    Run Keyword If      ${local} and '${applicatonType}' == 'android'   Begin Local Android Mobile Test
    ...     ELSE IF     ${local} and '${applicatonType}' == 'iOS'       Begin Local iOS Mobile Test
    ...     ELSE IF     '${applicatonType}' == 'android'               Begin Remote Android Mobile Test
    ...     ELSE        Begin Remote iOS Mobile Test


End Session
    Run Keyword If      ${local}    End Local Session
    ...     ELSE        End Remote Session


End Mobile Session
    Run Keyword If      ${local}    End Local Mobile Session
    ...     ELSE        End Remote Mobile Session


Begin Local Web Test
    IF          '${environment}' == 'dev'
        ${START_URL}    Set Variable    ${dev_url}
    ELSE IF     '${environment}' == 'qa'
        ${START_URL}    Set Variable    ${qa_url}
    ELSE IF     '${environment}' == 'stage'
        ${START_URL}    Set Variable    ${stage_url}
    ELSE
        ${START_URL}    Set Variable    ${prod_url}
    END
    Run Keyword If      '${browser}' == 'chrome'    Create WebDriver With Chrome Options
    ...     ELSE IF     '${browser}' == 'firefox'   Create WebDriver With Firefox Options
    Go To    ${START_URL}


Begin Local Android Mobile Test
    IF          '${environment}' == 'dev'
        ${START_URL}    Set Variable    ${dev_url}
    ELSE IF     '${environment}' == 'qa'
        ${START_URL}    Set Variable    ${qa_url}
    ELSE IF     '${environment}' == 'stage'
        ${START_URL}    Set Variable    ${stage_url}
    ELSE
        ${START_URL}    Set Variable    ${prod_url}
    END
    Start Appium
    Open Application 
    ...     http://localhost:4723
    ...     appActivity=${EMPTY}
    ...     automationName=${ANDROID_AUTOMATION_NAME}
    ...     platformName=${ANDROID_PLATFORM_NAME}   
    ...     platformVersion=${ANDROID_PLATFORM_VERSION}
    ...     app=${ANDROID_APP}
    ...     browserName=Chrome
    Go To Url      ${START_URL}


Begin Local iOS Mobile Test
    IF          '${environment}' == 'dev'
        ${START_URL}    Set Variable    ${dev_url}
    ELSE IF     '${environment}' == 'qa'
        ${START_URL}    Set Variable    ${qa_url}
    ELSE IF     '${environment}' == 'stage'
        ${START_URL}    Set Variable    ${stage_url}
    ELSE
        ${START_URL}    Set Variable    ${prod_url}
    END
    Start Appium
    Open Application  
    ...     http://localhost:4723
    ...     automationName=${IOS_AUTOMATION_NAME}
    ...     platformName=${IOS_PLATFORM_NAME}   
    ...     platformVersion=${IOS_PLATFORM_VERSION}
    ...     deviceName=${IOS_DEVICE_NAME}
    ...     browserName=Safari
    Go To Url     ${START_URL} 


End Local Session
    Close Browser

End Local Mobile Session
    Stop Appium
    Close Application


Begin Remote Web Test
    IF          '${environment}' == 'dev'
        ${START_URL}    Set Variable    ${dev_url}
    ELSE IF     '${environment}' == 'qa'
        ${START_URL}    Set Variable    ${qa_url}
    ELSE IF     '${environment}' == 'stage'
        ${START_URL}    Set Variable    ${stage_url}
    ELSE
        ${START_URL}    Set Variable    ${prod_url}
    END
    IF          '${browser}' == 'firefox'   
        ${desired_capabilities}     Set Variable     ${firefox_desired_capabilities}
    ELSE IF     '${browser}' == 'safari'    
        ${desired_capabilities}     Set Variable     ${safari_desired_capabilities}  
    ELSE        
        ${desired_capabilities}     Set Variable     ${chrome_desired_capabilities} 
    END 
    Open Browser    ${START_URL}    ${browser}      remote_url=${remote_url}       desired_capabilities=${desired_capabilities}


Begin Remote Android Mobile Test
    IF          '${environment}' == 'dev'
        ${START_URL}    Set Variable    ${dev_url}
    ELSE IF     '${environment}' == 'qa'
        ${START_URL}    Set Variable    ${qa_url}
    ELSE IF     '${environment}' == 'stage'
        ${START_URL}    Set Variable    ${stage_url}
    ELSE
        ${START_URL}    Set Variable    ${prod_url}
    END
    Open Browser    ${START_URL}    chrome          remote_url=${remote_url}       desired_capabilities=${ANDROID_DESIRED_CAPS}


Begin Remote iOS Mobile Test
    IF          '${environment}' == 'dev'
        ${START_URL}    Set Variable    ${dev_url}
    ELSE IF     '${environment}' == 'qa'
        ${START_URL}    Set Variable    ${qa_url}
    ELSE IF     '${environment}' == 'stage'
        ${START_URL}    Set Variable    ${stage_url}
    ELSE
        ${START_URL}    Set Variable    ${prod_url}
    END
    Open Browser    ${START_URL}    safari          remote_url=${remote_url}       desired_capabilities=${IOS_DESIRED_CAPS}


End Remote Session
    Run Keyword If  '${TEST_STATUS}'== 'PASS'  Execute Javascript  sauce:job-result=passed
    ...  ELSE  Execute Javascript  sauce:job-result=failed
    Close Browser

End Remote Mobile Session
    Run Keyword If  '${TEST_STATUS}'== 'PASS'  Execute Javascript  sauce:job-result=passed
    ...  ELSE  Execute Javascript  sauce:job-result=failed
    Close Browser


Create WebDriver With Chrome Options
    ${chrome_options}=    Evaluate    selenium.webdriver.ChromeOptions()
    Call Method    ${chrome_options}    add_argument    --no-sandbox
    Call Method    ${chrome_options}    add_argument    --headless
    Call Method    ${chrome_options}    add_argument    --disable-dev-shm-usage
    Call Method    ${chrome_options}    add_argument    --single-process
    Call Method    ${chrome_options}    add_argument    --log-level\=3
    Call Method    ${chrome_options}    add_argument    --start-maximized
    Call Method    ${chrome_options}    add_argument    --window-size\=1200,900
    Call Method    ${chrome_options}    add_argument    --disable-extensions
    ${chrome_options.binary_location}   Set Variable   /usr/bin/chromium
    @{service_args}=    Create List    --verbose    --log-path=/qa-automation/chromedriver.log
    Create WebDriver    Chrome    chrome_options=${chrome_options}    service_args=@{service_args}


Create WebDriver With Firefox Options
    ${firefox_options}=    Evaluate    selenium.webdriver.FirefoxOptions()
    Call Method    ${firefox_options}    add_argument   --headless
    Call Method    ${firefox_options}    add_argument   --single-process
    Call Method    ${firefox_options}    add_argument   --log-level\=3
    Call Method    ${firefox_options}    add_argument   --start-maximized
    Call Method    ${firefox_options}    add_argument   --window-size\=1200,900
    Call Method    ${firefox_options}    add_argument   --disable-extensions
    ${firefox_options.binary_location}   Set Variable   /usr/bin/firefox
    @{service_args}=    Create List    --verbose    --log-path=/qa-automation/geckodriver.log
    Create Webdriver    Firefox    options=${firefox_options}   executable_path=/usr/local/bin/geckodriver   service_args=@{service_args}


user is not logged in
    Log  Check to see whether user is logged in

user searches for content
    [Arguments]     ${search_term}
    LandingPage.Verify Page Loaded
    TopNav.Search for Content   ${search_term}

user searches for content mobile
    [Arguments]     ${search_term}
    LandingPage.Verify Page Loaded Mobile
    TopNav.Search for Content Mobile   ${search_term}

search results contains relevant products
    Log  Clicking on the first content link
    SearchResults.Verify Search Completed
    SearchResults.Click Content Link


search results contains relevant products mobile
    Log  Clicking on the first content link
    SearchResults.Verify Search Completed Mobile
    SearchResults.Click Content Link Mobile