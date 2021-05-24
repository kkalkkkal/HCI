package com.example.hci_project.bean

import android.content.Context
import android.util.Log
import jxl.Workbook
import java.io.InputStream
import java.lang.Exception

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
    fun use(context: Context, callback: (SchoolManager?) -> Unit) {
        if (list.isNotEmpty()) {
            callback(this)
            return
        }
        //load
        try {
            list = load(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        callback(if (list.isEmpty()) null else this)
    }

    fun search(keyword: String): ArrayList<School> {
        val resultList: ArrayList<School> = ArrayList()
        list.map {
            if (it.name.contains(keyword))
                resultList.add(it)
        }
        return resultList
    }

    private fun load(context: Context, filterSetting: FilterSetting?= null): ArrayList<School> {
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
                            currentRow[getColIndex('d')].contents!!,
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
        return schoolList
    }
}