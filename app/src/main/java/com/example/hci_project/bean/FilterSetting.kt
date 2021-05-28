package com.example.hci_project.bean

import com.example.hci_project.R
import java.io.Serializable

class FilterSetting() : Serializable {

    class Facilitate(val name: String, val id: String, val icon: Int) : Serializable {
        companion object {
            val BUS_AVAILABLE =
                Facilitate("통학버스 운영", "bus_available", R.drawable.ic_baseline_directions_bus_24)
            val WATER_INSPECTION =
                Facilitate("수질 안전 검사", "water_inspection", R.drawable.ic_baseline_water_damage_24)
            val ELECTRIC_INSPECTION = Facilitate(
                "전기 안전 검사",
                "electric_inspection",
                R.drawable.ic_baseline_electrical_services_24
            )
            val GAS_INSPECTION =
                Facilitate("가스 안전 검사", "gas_inspection", R.drawable.ic_baseline_cloud_done_24)
            val FIRE_INSPECTION = Facilitate(
                "소방 안전 검사",
                "fire_inspection",
                R.drawable.ic_baseline_fire_extinguisher_24
            )
            val ESCAPE_TRAINING =
                Facilitate("소방 대피 훈련", "escape_training", R.drawable.ic_baseline_run_circle_24)

            fun getAll(): ArrayList<Facilitate> {
                return arrayListOf(
                    BUS_AVAILABLE,
                    WATER_INSPECTION,
                    GAS_INSPECTION,
                    ELECTRIC_INSPECTION,
                    FIRE_INSPECTION,
                    ESCAPE_TRAINING
                )
            }
        }


        override fun toString(): String {
            return "Facilitate(name='$name', id='$id', icon=$icon)"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Facilitate

            return other.id == this.id
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + id.hashCode()
            result = 31 * result + icon
            return result
        }
    }

    val facilitates: ArrayList<Facilitate> = arrayListOf(
        Facilitate.WATER_INSPECTION,
        Facilitate.GAS_INSPECTION,
        Facilitate.ELECTRIC_INSPECTION,
        Facilitate.FIRE_INSPECTION,
        Facilitate.ESCAPE_TRAINING
    )

    var minSchoolSize: Int = 0
    var maxKidsPerTeacher: Int = 0
    var maxDistanceKmFromHere: Int = 3

    var schoolStartHour: Int = 0
    var schoolEndTime: Int = 0

    var callback: Runnable? = null

    constructor(filterSetting: FilterSetting) : this() {
        minSchoolSize = filterSetting.minSchoolSize
        maxKidsPerTeacher = filterSetting.maxKidsPerTeacher
        maxDistanceKmFromHere = filterSetting.maxDistanceKmFromHere
        schoolStartHour = filterSetting.schoolStartHour
        schoolEndTime = filterSetting.schoolEndTime

        facilitates.clear()
        facilitates.addAll(filterSetting.facilitates)
    }

    @JvmName("setCallback1")
    fun setCallback(callback: Runnable?) {
        this.callback = callback
    }

    fun use(usable: (FilterSetting) -> Unit) {
        usable(this)
        callback?.run()
    }

    fun overwrite(filterSetting: FilterSetting): FilterSetting {
        minSchoolSize = filterSetting.minSchoolSize
        maxKidsPerTeacher = filterSetting.maxKidsPerTeacher
        maxDistanceKmFromHere = filterSetting.maxDistanceKmFromHere
        schoolStartHour = filterSetting.schoolStartHour
        schoolEndTime = filterSetting.schoolEndTime

        facilitates.clear()
        facilitates.addAll(filterSetting.facilitates)

        callback?.run()

        return this
    }

    override fun toString(): String {
        return "FilterSetting(facilitates=$facilitates, minSchoolSize=$minSchoolSize, maxKidsPerTeacher=$maxKidsPerTeacher, maxDistanceKmFromHere=$maxDistanceKmFromHere, schoolStartHour=$schoolStartHour, schoolEndTime=$schoolEndTime, callback=$callback)"
    }


}