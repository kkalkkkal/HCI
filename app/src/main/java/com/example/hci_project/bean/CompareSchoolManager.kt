package com.example.hci_project.bean

class CompareSchoolManager {
    companion object {
        val list: ArrayList<School> = ArrayList()
        fun add(school: School): ArrayList<School> {
            //not allow duplicated!
            if (!contains(school))
                list.add(school)
            return list
        }

        fun remove(school: School): ArrayList<School> {
            val iterator = list.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().equals(school)) {
                    iterator.remove()
                    break
                }
            }
            return list
        }

        fun contains(school: School): Boolean {
            var result = false
            list.map {
                if (it.equals(school))
                    result = true
            }
            return result
        }
    }
}