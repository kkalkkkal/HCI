package com.example.hci_project.bean

import java.io.Serializable

class SearchResult(
    val type: Int,
    val title: String,
    val subtitle: String?,
    val obj: Serializable?= null,
    val timeStamp: Long = System.currentTimeMillis()
) : Serializable {
    companion object {
        val TYPE_SEARCH: Int = 1234
        val TYPE_SCHOOL: Int = 4321
    }
}