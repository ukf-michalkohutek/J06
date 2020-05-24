

1. On openweathermap.org, the daily forecast API (https://openweathermap.org/forecast16) is paid.
   At first, I wanted to use the free API (https://openweathermap.org/forecast5) for 5 day / 3 hour forecasts,
   but it returns information for every 3 hours, instead of summing the whole day up (which is what we want), so it wasn't the best option.

   The ideal API for our situation is the One Call API (https://openweathermap.org/api/one-call-api).
   You can filter out the information and only get current weather + daily forecast for 7 days.
   The only downside of this API is you must search by Lat and Lon instead of cityID.
   For this reason, I made a function that gets the Lat and Lon for a given cityID (also from openweathermap), and decided to use the One Call API.

   There might be a better solution, but this is the cleanest one I could figure out without paying for an API subscription,
   or parsing through a ton of useless 3 hour forecast data.

2. The date and time displayed at the top of the application is for the city you have selected, not your local time.

3. The background of the application changes based on the current time of day (night/day)
   and the current weather condition (Clear/Cloudy/Rain/Thunderstorm/Snow), of the city you selected.

4. Displaying information about 2 cities at a time is done by pressing a button to move between pages.
   To achieve this I had had to make a second copy of weather.fxml, weatherController.java and previousCities.json.
   There is probably a smarter way to do it without having two separate files but I didn't have enough time to figure it out.

5. The application remembers the last 3 cities you have selected, for both page 1 and page 2.
   The city that shows up after you launch the application is the last city you had selected for that page.