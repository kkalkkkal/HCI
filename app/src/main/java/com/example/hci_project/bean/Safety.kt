package com.example.hci_project.bean

class Safety(
        //대피훈련여부
        val escapeCheck: Boolean,
        //가스안전검사여부
        val gasCheck: Boolean,
        //소방안전검사여부
        val fireCheck: Boolean,
        //전기안전검사여부
        val electricCheck: Boolean,
        //놀이시설검사여부
        val playgroundCheck: Boolean,
        //총 cctv 갯수
        val cctvCnt: Int
)