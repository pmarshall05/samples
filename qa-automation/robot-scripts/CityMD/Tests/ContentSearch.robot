*** Settings ***
Documentation  Tests for Content Search

Resource  ../Resources/common.robot  # necessary for keywords and Setup & Teardown


Test Setup  Begin Web Test
Test Teardown  End Session


*** Test Cases ***
TC:03:Logged out user search for content
    [Tags]  citymd  smoke  search
    # use control-b on each keyword to see lower level keywords
    Given user is not logged in
    When user searches for content      ${search_term}
    Then search results contains relevant products


TC:04:Logged out user search empty
    [Tags]  citymd  smoke  search
    [Documentation]   Logged out user when searches with empty value , error message should be displayed
    Given user is not logged in
    When user searches for content   ${EMPTY}
    Then Verify Error Message

