package com.example.hci_project.bean

import android.content.Context
import androidx.annotation.WorkerThread
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FavoriteSchoolManager private constructor() {
    companion object {
        private var inst: FavoriteSchoolManager? = null
        fun getInstance(): FavoriteSchoolManager {
            if (inst == null)
                inst = FavoriteSchoolManager()
            return inst!!
        }
    }

    private val fileName = "favorite2.txt"

    var list: ArrayList<School> = ArrayList()
    fun getListAsSearchResult(): ArrayList<SearchResult> {
        val searchResultList = ArrayList<SearchResult>()
        list.map {
            val searchResult = SearchResult.convert(it)
            searchResult.obj = it
            searchResultList.add(searchResult)
        }
        return searchResultList
    }

    fun isFavorite(school: School): Boolean {
        var target = list.find {
            it.name == school.name
        }
        return target != null
    }

    @Synchronized
    fun use(context: Context, @WorkerThread callback: (FavoriteSchoolManager?) -> Unit) {
        if (list.isNotEmpty()) {
            callback(this)
            return
        }
        Thread {
            try {
                val file = context.getFileStreamPath(fileName)
                if (file.exists()) {
                    val inputStream = ObjectInputStream(file.inputStream())
                    val list = inputStream.readObject()
                    this.list = list as ArrayList<School>
                } else {
                    save(context)
                }
                callback(this)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }.start()
    }


    fun save(context: Context) {
        Thread {
            val pureList: ArrayList<School> = ArrayList()
            list.map {
                if (!pureList.contains(it))
                    pureList.add(it)
            }
            list = pureList

            try {
                val file = context.getFileStreamPath(fileName)
                if (!file.exists())
                    file.createNewFile()
                val outputStream = ObjectOutputStream(file.outputStream())
                outputStream.writeObject(pureList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}