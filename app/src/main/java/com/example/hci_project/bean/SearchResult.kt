package com.example.hci_project.bean

import java.io.Serializable

class SearchResult(
    val type: Int,
    val title: String,
    val subtitle: String? = null,
    var obj: Serializable? = null,
    val timeStamp: Long = System.currentTimeMillis()
) : Serializable {
    companion object {
        val TYPE_SEARCH: Int = 1234
        val TYPE_SCHOOL: Int = 4321

        fun convert(schoolList: ArrayList<School>): ArrayList<SearchResult> {
            val list = ArrayList<SearchResult>()
            schoolList.map {
                list.add(convert(it))
            }

            return list
        }

        fun convert(school: School): SearchResult {
            return SearchResult(TYPE_SCHOOL, school.name, school.addr)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is SearchResult) {
            return other.timeStamp == this.timeStamp ||
                    (other.type == this.type && other.title == this.title)
        }
        return false
    }
}