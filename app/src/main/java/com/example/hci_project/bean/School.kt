package com.example.hci_project.bean

import java.io.Serializable

class School(
        val addr: String,
        val name: String,
        val type: String,
        val postNum: String,
        val tel: String,
        val roomCnt: Int,
        val size: Int,
        val playgroundCnt: Int,
        val teacherCnt: Int,
        val maxStudentCnt: Int,
        val currentStudentCnt: Int,
        val lat: Double,
        val lng: Double,
        val isAvailableBus: Boolean,
        val homePage: String,
        val sinceDate: String,
) : Serializable {
    companion object {
        val TYPE_CHILD = "어린이집"
        val TYPE_KINDER = "유치원"
    }

    fun getOnlySchoolType(): String {
        return if (type.contains(TYPE_CHILD))
            TYPE_CHILD
        else
            TYPE_KINDER
    }

    fun getKidsPerTeacher(): Int {
        if (teacherCnt == 0)
            return 0
        return currentStudentCnt / teacherCnt
    }

    fun getDistanceFromUserLocation(): Float {
        if (LocationUtil.location == null) {
            return 0f
        }

        val userLocation = LocationUtil.location!!
        return LocationUtil.distance(userLocation.latitude, userLocation.longitude, lat, lng).toFloat()
    }

    override fun equals(other: Any?): Boolean {
        if (other is School) {
            return other.name == this.name
        }
        return super.equals(other)
    }
}