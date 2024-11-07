# CS310_TeamAssignment2_BeachApp

Running the MainActivity file on a Pixel 2 with API 35 should be appropriate to support the app runtime.

We currently have a test user with the username and password of 'ss' and 'ss' respectfully, which can be inputted to grant access into the app. Additionally, Santa Monica is the only beach to have signifncant data (user reviews, star ratings) to be implemented at the moment, which is currently our benchmark for app functionality. Clicking on the corresponding Santa Monica button should open up a new page displaying all the appropriate information for that beach. It may take a second for the beach weather info to update due to API fetching occuring on a separate thread.

If any issues persist when trying to run the app, they may be related to: firebase authorization, currently slow overall runtime, or poor internet connection (preventing proper fetching of API data).
