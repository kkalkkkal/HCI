package com.example.hci_project.bean

import java.io.Serializable

class ServiceTime(
        val startHour: Int = 0,
        val startMin: Int = 0,
        val endHour: Int = 0,
        val endMin: Int = 0):Serializable {

    companion object {
        fun build(serviceTime: String): ServiceTime? {
            val serviceTimes: List<String> = serviceTime.split("~")

            var startHour: Int = 0
            var startMin: Int = 0
            var endHour: Int = 0
            var endMin: Int = 0
            try {
                for (idx in 0..1) {
                    val timeStr = serviceTimes[idx]

                    val hourStartIdx = 0
                    val minStartIdx = timeStr.indexOf('시', 0) + 1

                    if (idx == 0) {
                        startHour = timeStr.substring(hourStartIdx, timeStr.indexOf('시', 0)).toInt()
                        startMin = timeStr.substring(minStartIdx, timeStr.length -1).toInt()
                    } else if (idx == 1) {
                        endHour = timeStr.substring(hourStartIdx, timeStr.indexOf('시', 0)).toInt()
                        endMin = timeStr.substring(minStartIdx, timeStr.length - 1).toInt()
                    }
                }
                return ServiceTime(startHour, startMin, endHour, endMin)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }
}