package com.yjk.drawdiaryv2.view.main.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.yjk.drawdiaryv2.common.db.sql.Database
import com.yjk.drawdiaryv2.common.db.sql.SQLHelper
import com.yjk.drawdiaryv2.view.add.util.BitmapHelper
import com.yjk.drawdiaryv2.view.main.data.DiaryData
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainPresenter {

    fun getDiaryList(context: Context): ArrayList<DiaryData> {

        val diaryDataList = ArrayList<DiaryData>()

        val sqlHelper = SQLHelper(context)
        val cursor = sqlHelper.SELECTALL()

        val size = cursor.count

        if (size != 0) {

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {

                val diary = DiaryData()

                val index_id = cursor.getColumnIndex(Database.Entry._ID)
                if (index_id >= 0) {
                    val id = cursor.getInt(index_id)
                    diary.id = id
                }

                val index_time = cursor.getColumnIndex(Database.Entry.TIME)
                if (index_time >= 0) {
                    val time = cursor.getString(index_time)
                    diary.time = time
                }

                val index_weather = cursor.getColumnIndex(Database.Entry.WEATHER)
                if (index_weather >= 0) {
                    val weather = cursor.getInt(index_weather)
                    diary.weather = weather
                }

                val index_url = cursor.getColumnIndex(Database.Entry.URL)
                if (index_url >= 0) {
                    val url = cursor.getBlob(index_url)
                    diary.url = url
                }

                val index_allurl = cursor.getColumnIndex(Database.Entry.ALL_URL)
                if (index_allurl >= 0) {
                    val allurl = cursor.getBlob(index_allurl)
                    diary.all_url = allurl
                }

                val index_message = cursor.getColumnIndex(Database.Entry.WRITE)
                if (index_message >= 0) {
                    val message = cursor.getString(index_message)
                    diary.message = message
                }

                diaryDataList.add(diary)
                cursor.moveToNext()
            }
        }

        return diaryDataList
    }

    fun delete(context: Context, item: DiaryData) {
        val sqlHelper = SQLHelper(context)
        sqlHelper.DELETE("${item.id}")
    }

    fun share(activity: Activity, item: DiaryData) {

        try {
            val bitmapHelper = BitmapHelper()

            val bitmap = bitmapHelper.byteArrayToBitmap(item.all_url)

            val cachePath = File(activity.externalCacheDir, "diary_images")
            cachePath.mkdirs()

            val file = File(cachePath, "DrawDiary_${item.time}.png")

            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            val uri = FileProvider.getUriForFile(activity, activity.applicationContext.packageName +".provider", file)
            var intent = Intent(Intent.ACTION_SEND)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.setType("image/png")
            activity.startActivity(Intent.createChooser(intent, "share"))

        } catch (e: Exception) {
            // 저장 에러
            e.printStackTrace()
        }
    }
}