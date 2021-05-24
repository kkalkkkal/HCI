package com.example.hci_project.bean

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class LocationUtil private constructor() {
    companion object {
        var location: Location? = null
        private val mLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(_location: Location?) {
                if (_location != null) {
                    location = _location
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
        }

        @SuppressLint("MissingPermission")
        fun requestUserLocation(context: Context) {
            //require Location Permission
            val mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60,
                    100f, mLocationListener)
        }

        fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, unit: Char = 'K'): Double {
            val theta = lon1 - lon2
            var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(deg2rad(theta))
            dist = acos(dist)
            dist = rad2deg(dist)
            dist *= 60 * 1.1515
            if (unit == 'K') {
                dist *= 1.609344
            } else if (unit == 'N') {
                dist *= 0.8684
            }
            return dist
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/ /*::  This function converts decimal degrees to radians             :*/ /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/ /*::  This function converts radians to decimal degrees             :*/ /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }
    }
}