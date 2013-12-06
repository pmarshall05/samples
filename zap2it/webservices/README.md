Zap2it Web Services
===================
Developer Goal: Centralize all of our data services functionality
URL:  http://api.zap2it.com/tvlistings/webservices/[Web Service Name]

All APIs return JSON by default.  However, they also return XML and HTML just set the return type variable (rty) to JSON, XML, or HTML.

Examples are listed below and can also be viewed on Zap2it.com's home page.

What's On
============================
This API returns station-level data.  Returns what is airing for one or more stations for a discreet block of time.  The block of time cannot exceed 24 hours.
* http://api.zap2it.com/tvlistings/webservices/whatson?stnlt=11164


Upcoming Airings
============================
This API returns show-level schedule information.  Returns the upcoming airings for given series on a given set of stations for the next 14 days.
* http://api.zap2it.com/tvlistings/webservices/upcomingAirings?sid=EP01158760,EP00596702&stnlt=51306&zip=60611


Prime Time Grid
============================
This API returns the prime time grid for the given stations and zip code.  If parameters are not provided, affiliate defaults are used instead.
* http://api.zap2it.com/tvlistings/webservices/primetimeGrid?zip=60611


Celebs On TV
============================
This API returns a list of celebs appearing on a talk show within the next 24 hours
* http://api.zap2it.com/tvlistings/webservices/celebsontv


Popular Shows
============================
This API returns “new” or “live” shows ordered by popularity (check-in counter descending) for the given criteria.  However, a sponsored episode can be injected at any position in the result set. 
* http://api.zap2it.com/tvlistings/webservices/popularShows