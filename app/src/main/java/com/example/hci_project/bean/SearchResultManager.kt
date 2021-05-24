package com.example.hci_project.bean

import android.content.Context
import androidx.annotation.WorkerThread
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.collections.ArrayList

class SearchResultManager private constructor() {
    companion object {
        private var inst: SearchResultManager? = null
        fun getInstance(): SearchResultManager {
            if (inst == null)
                inst = SearchResultManager()
            return inst!!
        }
    }

    private val fileName = "searchResults.txt"

    var searchResultList: ArrayList<SearchResult> = ArrayList()

    @Synchronized
    fun use(context: Context, @WorkerThread callback: (SearchResultManager?) -> Unit) {
        if (searchResultList.isNotEmpty()) {
            callback(this)
            return
        }
        Thread {
            try {
                val file = context.getFileStreamPath(fileName)
                if (file.exists()) {
                    val inputStream = ObjectInputStream(file.inputStream())
                    val list = inputStream.readObject()
                    searchResultList.clear()
                    searchResultList = list as ArrayList<SearchResult>
                    searchResultList.sortBy {
                        -it.timeStamp
                    }
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
            val pureList = ArrayList<SearchResult>()
            searchResultList.map {
                if (pureList.contains(it))
                    return@map

                pureList.add(it)
            }
            searchResultList = pureList
            try {
                val file = context.getFileStreamPath(fileName)
                if (!file.exists())
                    file.createNewFile()
                val outputStream = ObjectOutputStream(file.outputStream())
                outputStream.writeObject(searchResultList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}