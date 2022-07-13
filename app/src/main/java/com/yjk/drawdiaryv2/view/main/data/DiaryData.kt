package com.yjk.drawdiaryv2.view.main.data

class DiaryData {

    var id = 0
    var time = ""
    var weather = 0
    var message = ""
    var url: ByteArray? = null
    var all_url: ByteArray? = null
    override fun toString(): String {
        return "DiaryData(id=$id, time='$time', weather=$weather, message='$message')"
    }


}