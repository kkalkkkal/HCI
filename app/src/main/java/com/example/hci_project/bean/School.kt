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

    fun getKidsPerTeacher(): Int {
        return currentStudentCnt / currentStudentCnt
    }
}