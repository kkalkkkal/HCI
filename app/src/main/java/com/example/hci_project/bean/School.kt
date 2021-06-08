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
    val sinceDate: String
) : Serializable {
    companion object {
        val TYPE_CHILD = "어린이집"
        val TYPE_KINDER = "유치원"
    }

    //null일 경우 안전정보 없음
    //어린이집은 해당 정보가 없
    var safety: Safety? = null

    //영양사 직원 수
    //-1일 경우 정보 없음
    var mealManagerCnt: Int = -1

    //위탁 또는 직영
    var mealServiceType: String? = null

    var serviceTime: ServiceTime? = null


    fun getOnlySchoolType(): String {
        return if (type.contains(TYPE_CHILD))
            TYPE_CHILD
        else
            TYPE_KINDER
    }

    fun getServiceType(): String {
        val types = arrayListOf<String>("법인", "가정", "민간", "협동", "사립", "병설", "공립")
        var result = "공립"
        types.map {
            if (type.contains(it))
                result = it
        }
        return result
    }

    fun getKidsPerTeacher(): Float {
        if (teacherCnt == 0)
            return 0f
        return currentStudentCnt.toFloat() / teacherCnt.toFloat()
    }

    fun getDistanceFromUserLocation(): Float {
        if (LocationUtil.location == null) {
            return 0f
        }

        val userLocation = LocationUtil.location!!
        return LocationUtil.distance(userLocation.latitude, userLocation.longitude, lat, lng)
            .toFloat()
    }

    override fun equals(other: Any?): Boolean {
        if (other is School) {
            return other.name == this.name
        }
        return super.equals(other)
    }
}