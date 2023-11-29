*** Keywords ***
Verify Search Completed
    SeleniumLibrary.Wait Until Page Contains  results for "${SEARCH_TERM}"

Verify Search Completed Mobile
    AppiumLibrary.Wait Until Page Contains  results for "${SEARCH_TERM}"

Click Content Link
    [Documentation]  Clicks on the first product in the search results list
    SeleniumLibrary.Click Link  ${FirstLink}

Click Content Link Mobile
    [Documentation]  Clicks on the first product in the search results list
    AppiumLibrary.Click Link  ${FirstLink}