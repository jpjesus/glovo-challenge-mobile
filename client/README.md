# Glovo Andorid Challenge



## How to use it:
1. Change the ip address of the API endpoint in app/main/res/values/strings.xml 
    The name of the resource is **api_endpoint**
2. Compile the application and run it



### Notes:
* the dependencies are under: buildSrc/src/main/java/Config.kt

* uses the convex hull algorithm implemented in java from Project Nayuki.
  This work is released under LGPL-3.0 licenes and it's compatible with commercial use.
  The modified version that uses LatLng objects assumes that in an area of a city the difference 
  between the given points can is small and that we can think of working with points in a 2D plane,
  since this algorithm was not made to work with 3D data.
  
* The working area of a city shown on the map is the approximation of the real polygon. This polygon in the code
  is computed each time for all the set of points that make the original polygon. This of course is not the 
  most efficient solution possible, but it's implemented this way so it's easy to see the difference between the two.
  
  

