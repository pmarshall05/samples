*** Settings ***
Documentation  CityMD top navigation

*** Keywords ***
Search for Content
    [Arguments]     ${search_term}
    Enter Search Term  ${search_term}
    Submit Search

Search for Content Mobile
    [Arguments]     ${search_term}
    Enter Search Term Mobile ${search_term}
    Submit Search Mobile

Enter Search Term
    [Arguments]    ${search_term}
    SeleniumLibrary.Wait Until Element Is Visible    ${SearchButton}
    SeleniumLibrary.Click Button  ${SearchButton}
    SeleniumLibrary.Input Text  ${SearchField}   ${search_term}

Enter Search Term Mobile
    [Arguments]    ${search_term}
    AppiumLibrary.Wait Until Element Is Visible    ${SearchButton}
    AppiumLibrary.Click Button  ${SearchButton}
    AppiumLibrary.Input Text  ${SearchField}   ${search_term}

Submit Search
     SeleniumLibrary.Click Button  ${SearchSubmit}

Submit Search Mobile
    AppiumLibrary.Click Button  ${SearchSubmit}

Verify Error Message
     SeleniumLibrary.Wait Until Element Is Visible    ${SearchErrorMessage}
     SeleniumLibrary.Element Text Should Be    ${SearchErrorMessage}    Please enter a valid input


Verify Error Message Mobile
     AppiumLibrary.Wait Until Element Is Visible    ${SearchErrorMessage}
     AppiumLibrary.Element Text Should Be    ${SearchErrorMessage}    Please enter a valid input
    
    