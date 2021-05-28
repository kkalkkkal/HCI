package com.example.hci_project.bean

import android.content.Context
import android.util.Log
import com.naver.maps.geometry.LatLng
import jxl.Cell
import jxl.Workbook
import java.io.InputStream

class SchoolManager private constructor() {
    companion object {
        private var inst: SchoolManager? = null
        fun getInstance(): SchoolManager {
            if (inst == null)
                inst = SchoolManager()
            return inst!!
        }
    }

    var list: ArrayList<School> = ArrayList()

    @Synchronized
    fun use(context: Context, callback: (SchoolManager?) -> Unit) {
        if (list.isNotEmpty()) {
            callback(this)
            return
        }
        //load
        Thread {
            try {
                val list = loadKindergarden(context)
                list.addAll(loadChildHome(context))

                this.list = list
                //success to load
                callback(this)
            } catch (e: Exception) {
                e.printStackTrace()
                //fail to load
                callback(null)
            }
        }.start()
    }

    fun sortByLocation(userLocation: LatLng): ArrayList<School> {
        val sorted = ArrayList<School>(list)
        return ArrayList(sorted.sortedBy {
            LocationUtil.distance(userLocation.latitude, userLocation.longitude, it.lat, it.lng)
        })
    }

    fun search(keyword: String, filterSetting: FilterSetting?): ArrayList<School> {
        val resultList: ArrayList<School> = ArrayList()
        list.map {
            if (keyword == "" || it.name.contains(keyword))
                resultList.add(it)
        }
        return if (filterSetting == null) resultList else applyFilter(resultList, filterSetting)
    }

    private fun applyFilter(schoolList: ArrayList<School>, filterSetting: FilterSetting): ArrayList<School> {
        val resultList: ArrayList<School> = ArrayList(schoolList)
        //BUS
        if (filterSetting.facilitates.contains(FilterSetting.Facilitate.BUS_AVAILABLE)) {
            val iterator = resultList.iterator()
            while (iterator.hasNext()) {
                val school = iterator.next()
                if (!school.isAvailableBus) {
                    iterator.remove()
                }
            }
        }
        //school size
        if (filterSetting.minSchoolSize != 0) {
            val iterator = resultList.iterator()
            while (iterator.hasNext()) {
                val school = iterator.next()
                if (school.size < filterSetting.minSchoolSize) {
                    iterator.remove()
                }
            }
        }
        //kids per teacher
        if (filterSetting.maxKidsPerTeacher != 0) {
            val iterator = resultList.iterator()
            while (iterator.hasNext()) {
                val school = iterator.next()
                if (school.getKidsPerTeacher() < filterSetting.minSchoolSize) {
                    iterator.remove()
                }
            }
        }
        if (filterSetting.maxDistanceKmFromHere != 5 && LocationUtil.location != null) {
            val iterator = resultList.iterator()
            while (iterator.hasNext()) {
                val school = iterator.next()
                if (school.getDistanceFromUserLocation() > filterSetting.maxDistanceKmFromHere) {
                    iterator.remove()
                }
            }
        }
        return resultList
    }

    private fun loadKindergarden(context: Context): ArrayList<School> {
        // DB 불러오기
        val schoolList = ArrayList<School>()

        val is2: InputStream =
                context.resources.assets.open("kindergardenDB.xls") // 유치원 현황
        val wb = Workbook.getWorkbook(is2)
        if (wb != null) {
            val sheet = wb.getSheet("유치원+기본현황") // 시트 불러오기
            val sheet_room = wb.getSheet("유치원+교실면적현황") // 시트 불러오기
            val sheet_bus = wb.getSheet("유치원+통학차량현황") // 시트 불러오기
            val sheet_teacher = wb.getSheet("유치원+직위·자격별+교직원현황") // 시트 불러오기
            if (sheet != null && sheet_room != null && sheet_bus != null) {
                var getColIndex = fun(char: Char): Int {
                    return char.toInt() - 'a'.toInt()
                }
                var getSum = fun(row: Array<Cell>, start: Char, end: Char): Int {
                    var sum = 0
                    for (idx in getColIndex(start) until getColIndex(end)) {
                        try {
                            sum += row[idx].contents!!.toInt()
                        } catch (e: Exception) {
                            //cast error
//                            e.printStackTrace()
                        }
                    }
                    return sum
                }
                //iterate about row
                for (row in 3 until sheet.rows - 1) {
                    try {
                        val currentRow = sheet.getRow(row)
                        val currentRow_room = sheet_room.getRow(row)
                        val currentRow_bus = sheet_bus.getRow(row)
                        val currentRow_teacher = sheet_bus.getRow(row)
                        val school = School(
                                currentRow[getColIndex('h')].contents!!,
                                currentRow[getColIndex('d')].contents!!,
                                "${School.TYPE_KINDER} " + currentRow[getColIndex('e')].contents!!,
                                "",
                                currentRow[getColIndex('l')].contents!!,
                                currentRow_room[getColIndex('f')].contents!!.replace("개", "").toInt(),
                                (currentRow_room[getColIndex('g')].contents!!.replace("㎡", "").toInt() / 3.3).toInt(),
                                if (currentRow_room[getColIndex('h')].contents!!.replace("㎡", "") == "") 0 else 1,
                                getSum(currentRow_teacher, 'g', 't'),
                                getSum(currentRow, 'l', 'u'),
                                getSum(currentRow, 'q', 'u'),
                                currentRow[getColIndex('w')].contents!!.toDouble(),
                                currentRow[getColIndex('x')].contents!!.toDouble(),
                                currentRow_bus[getColIndex('f')].contents!! == "Y",
                                currentRow[getColIndex('j')].contents!!,
                                currentRow[getColIndex('g')].contents!!,
                        )
                        schoolList.add(school)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("excel translate err", e.toString())
                    }
                }
            }
        }
        wb?.close()
        return schoolList
    }

    private fun loadChildHome(context: Context): ArrayList<School> {
        // DB 불러오기
        val schoolList = ArrayList<School>()

        val is2: InputStream =
                context.resources.assets.open("childhomeDB.xls") // 어린이집 현황
        val wb2 = Workbook.getWorkbook(is2)
        if (wb2 != null) {
            val sheet = wb2.getSheet(0) // 시트 불러오기
            if (sheet != null) {
                var getColIndex = fun(char: Char): Int {
                    return char.toInt() - 'a'.toInt()
                }
                //iterate about row
                for (row in 1 until sheet.rows - 1) {
                    try {
                        val currentRow = sheet.getRow(row)
                        if (currentRow[getColIndex('e')].contents!! == "폐지")
                            continue

                        val school = School(
                                currentRow[getColIndex('g')].contents!!,
                                currentRow[getColIndex('c')].contents!!,
                                "${School.TYPE_CHILD} " + currentRow[getColIndex('d')].contents!!,
                                currentRow[getColIndex('f')].contents!!,
                                currentRow[getColIndex('h')].contents!!,
                                currentRow[getColIndex('j')].contents!!.toInt(),
                                currentRow[getColIndex('k')].contents!!.toInt(),
                                currentRow[getColIndex('l')].contents!!.toInt(),
                                currentRow[getColIndex('m')].contents!!.toInt(),
                                currentRow[getColIndex('n')].contents!!.toInt(),
                                currentRow[getColIndex('o')].contents!!.toInt(),
                                currentRow[getColIndex('p')].contents!!.toDouble(),
                                currentRow[getColIndex('q')].contents!!.toDouble(),
                                currentRow[getColIndex('r')].contents!! == "운영",
                                currentRow[getColIndex('s')].contents!!,
                                currentRow[getColIndex('t')].contents!!,
                        )
                        schoolList.add(school)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.d("excel translate err", e.toString())
                    }
                }
            }
        }
        wb2?.close()
        return schoolList
    }
}