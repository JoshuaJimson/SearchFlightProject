Feature: Search Flights

Background: User is on Clear Trip home page
	Given I open browser with Clear Trip URL

Scenario: To Search for flights given From, To, number of passengers and date
	When I select "Trip" as <trip>
    And I select "From" as <from> and "To" as <to>
    And I select "Year&Month" as <yearmonth> and "Date" as <date>
    And I set "Adults" as <adult> and "Children" as <children> and "Infants" as <infant>
    And I click on "Search Flights" button
    Then I should see "Search Results" page
 
 Examples:
     | Trip | From | To | Yean&Month | Date | Adults| Children | Infants|
     | One way | Kochi, IN - Cochin International Airport (COK)  | New Delhi, IN - Indira Gandhi Airport (DEL)  | January 2022 | 22 | 3 | 1 | 1 |