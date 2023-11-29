*** Keywords ***
Verify Page Loaded
    Set Window Size    1440    900
    SeleniumLibrary.Wait Until Page Contains  CityMD


Verify Page Loaded Mobile
    Set Window Size    1440    900
    AppiumLibrary.Wait Until Page Contains  CityMD