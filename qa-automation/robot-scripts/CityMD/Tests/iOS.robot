*** Settings ***
Documentation  Tests for iOS

Resource  ../Resources/common.robot  # necessary for Setup & Teardown

Test Setup  	Begin Mobile Test 		iOS
Test Teardown  	End Mobile Session


*** Test Cases ***
# TC:05:iOS - Logged out user search for content
#     [Tags]  citymd  smoke  search
#     # use control-b on each keyword to see lower level keywords
#     Given user is not logged in
#     When user searches for content mobile      ${search_term}
#     Then search results contains relevant products mobile


# TC:06:iOS - Logged out user search empty
#     [Tags]  citymd  smoke  search
#     [Documentation]   Logged out user when searches with empty value , error message should be displayed
#     Given user is not logged in
#     When user searches for content mobile   ${EMPTY}
#     Then Verify Error Message Mobile

