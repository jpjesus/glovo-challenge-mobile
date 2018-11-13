package com.adamszewera.glovochallenge.util;

import com.google.android.gms.maps.model.LatLng;

import java.util.*;

/*
 * Convex hull algorithm - Library (Java)
 *
 * Copyright (c) 2017 Project Nayuki
 * https://www.nayuki.io/page/convex-hull-algorithm
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (see COPYING.txt and COPYING.LESSER.txt).
 * If not, see <http://www.gnu.org/licenses/>.
 */


public final class ConvexHull {

    // Returns a new list of points representing the convex hull of
    // the given set of points. The convex hull excludes collinear points.
    // This algorithm runs in O(n log n) time.
    public static List<LatLng> makeHull(List<LatLng> points) {
        List<LatLng> newPoints = new ArrayList<>(points);
        Collections.sort(newPoints, new LatLngComparator());
        return makeHullPresorted(newPoints);
    }


    // Returns the convex hull, assuming that each points[i] <= points[i + 1]. Runs in O(n) time.
    public static List<LatLng> makeHullPresorted(List<LatLng> points) {
        if (points.size() <= 1)
            return new ArrayList<>(points);

        // Andrew's monotone chain algorithm. Positive y coordinates correspond to "up"
        // as per the mathematical convention, instead of "down" as per the computer
        // graphics convention. This doesn't affect the correctness of the result.

        List<LatLng> upperHull = new ArrayList<>();
        for (LatLng p : points) {
            while (upperHull.size() >= 2) {
                LatLng q = upperHull.get(upperHull.size() - 1);
                LatLng r = upperHull.get(upperHull.size() - 2);
                if ((q.latitude - r.latitude) * (p.longitude - r.longitude) >= (q.longitude - r.longitude) * (p.latitude - r.latitude))
                    upperHull.remove(upperHull.size() - 1);
                else
                    break;
            }
            upperHull.add(p);
        }
        upperHull.remove(upperHull.size() - 1);

        List<LatLng> lowerHull = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            LatLng p = points.get(i);
            while (lowerHull.size() >= 2) {
                LatLng q = lowerHull.get(lowerHull.size() - 1);
                LatLng r = lowerHull.get(lowerHull.size() - 2);
                if ((q.latitude - r.latitude) * (p.longitude - r.longitude) >= (q.longitude - r.longitude) * (p.latitude - r.latitude))
                    lowerHull.remove(lowerHull.size() - 1);
                else
                    break;
            }
            lowerHull.add(p);
        }
        lowerHull.remove(lowerHull.size() - 1);

        if (!(upperHull.size() == 1 && upperHull.equals(lowerHull)))
            upperHull.addAll(lowerHull);
        return upperHull;
    }

}



final class LatLngComparator implements Comparator<LatLng> {

    @Override
    public int compare(LatLng p1, LatLng p2) {
        if (p1.latitude != p2.latitude)
            return Double.compare(p1.latitude, p2.latitude);
        else
            return Double.compare(p2.latitude, p2.latitude);
    }

    @Override
    public boolean equals(Object obj) {
        // used to compare comparators
        return false;
    }
}









