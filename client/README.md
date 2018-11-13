# Glovo Andorid Challenge

### Notes:
* the dependencies are under: buildSrc/src/main/java/Config.kt

* uses the convex hull algorithm implemented in java from Project Nayuki.
  This work is released under LGPL-3.0 licenes and it's compatible with commercial use.
  The modified version that uses LatLng objects assumes that in an area of a city the difference 
  between the given points can is small and that we can think of working with points in a 2D plane,
  since this algorithm was not made to work with 3D data.