# Glovo Andorid Challenge



## How to use it:
1. Change the ip address of the API endpoint in app/main/res/values/strings.xml 
    The name of the resource is **api_endpoint**
2. Compile the application and run it



### Notes:
* the dependencies are under: buildSrc/src/main/java/Config.kt

* uses the convex hull algorithm implemented in java from Project Nayuki.
  This algorithm and its original implementation are released under LGPL-3.0 license and it's compatible with commercial use.
  The modified version created and used here uses LatLng objects and assumes that in an area of a city the difference 
  between the given points can be small enough to be useful in this scenario (if we imagine to work with points in a 2D plane,
  since this algorithm was not made to work with 3D data).
  For more see **util** package in the project.
  
* The working area of a city shown on the map is the approximation of the real polygon. This polygon 
  is computed each time for all the points that make the original polygon. This of course is not the 
  most efficient solution to do it, but it's implemented this way so it's easy to see the difference between the two.
  
  



### Not (yet) implemented features:
* The information panel should display additional information found in the city object for a city that is currently in the middle of the map
* the app should ask the user to select a city manually (cities should be grouped by country)



### Next steps:
* add error messages for the user to notify that a network error happened or that the position is not available
