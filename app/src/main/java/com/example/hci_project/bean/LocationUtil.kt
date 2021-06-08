package com.example.hci_project.bean

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class LocationUtil private constructor() {
    companion object {
        private var listenLocationOnce: ((Location) -> Unit)? = null
        var location: Location? = null
        private val mLocationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(_location: Location?) {
                if (_location != null) {
                    location = _location

                    if (listenLocationOnce != null) {
                        listenLocationOnce!!(location!!)
                        listenLocationOnce = null
                    }
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
        fun requestUserLocation(
            context: Context,
            listenLocationOnce: ((Location) -> Unit)? = null
        ) {
            //require Location Permission
            val mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
            this.listenLocationOnce = listenLocationOnce

            this.location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000 * 60,
                100f, mLocationListener
            )
        }

        //distance, velocity unit is km, km/h
        //return is min time
        fun getGoingTime(distance: Double, velocity: Double): Double {
            val velocityAsMin = (velocity * 1000) / 60
            val distanceAsMeter = distance * 1000

            return distanceAsMeter / velocityAsMin
        }

        fun distance(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double,
            unit: Char = 'K'
        ): Double {
            val theta = lon1 - lon2
            var dist =
                sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * cos(
                    deg2rad(theta)
                )
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

        fun isPermissionGranted(context: Context): Boolean {
            val denied = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            return !denied
        }

        fun requirePermission(
            activity: Activity,
            msg: String = "위치 필터 적용을 사용하려면 위치 권한이 필요합니다"
        ): Boolean {
            val denied = isPermissionGranted(activity)
            if (denied) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Toast.makeText(activity, msg, Toast.LENGTH_LONG)
                        .show()
                }
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
            }
            return !denied
        }
    }
}