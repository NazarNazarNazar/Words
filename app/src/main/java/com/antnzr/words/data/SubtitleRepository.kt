package com.antnzr.words.data

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.antnzr.words.domain.Subtitle
import com.antnzr.words.gateway.ApiClient
import com.antnzr.words.utils.SUB_DOWNLOAD_DIR
import com.antnzr.words.utils.SUB_LOCAL_DIR
import retrofit2.Call
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

interface SubtitleRepository {
    fun getSubtitles(context: Context, subName: String): ArrayList<Subtitle>
    fun downloadSubtitle(context: Context, url: String)
}

class SubtitleRepositoryImpl : SubtitleRepository {
    private val TAG = this.javaClass.simpleName

    override fun getSubtitles(context: Context, subName: String): ArrayList<Subtitle> {
        return getSubtitles(subName)!!
    }

    private fun getSubtitles(subName: String, lang: String = "eng"): ArrayList<Subtitle>? {
        val call: Call<ArrayList<Subtitle>> = ApiClient.getClient.getSubtitles(subName, lang)

        return try {
            ArrayList(filterSubtitles(call.execute().body()!!, subName))
        } catch (exception: Exception) {
            Log.d(TAG, "Couldn't get subtitles. Error: ${exception.message}")
            ArrayList()
        }
    }

    override fun downloadSubtitle(context: Context, url: String) {
        downloadFile(context, url)
    }

    private fun filterSubtitles(subtitles: ArrayList<Subtitle>, subName: String): List<Subtitle> {
        return subtitles
//            .filter { sub ->
//                sub.subFileName.toLowerCase(Locale.getDefault())
//                    .contains(subName.toLowerCase(Locale.getDefault()))
//            }
            .sortedWith(compareByDescending { it.subRating.toFloat() })
            .reversed()
    }

    private fun downloadFile(context: Context, url: String) {
        if (!isPermissionGranted(context)) {
            requestPermissions(context)
        } else {
            Log.d(TAG, "Start downloading file with url $url")
            try {
                val uri: Uri = Uri.parse(url)
                val fileName = "${uri.lastPathSegment}.zip"

                val localSubDir =
                    context.getExternalFilesDir(SUB_LOCAL_DIR).resolve(uri.lastPathSegment)
                if (localSubDir.exists()) {
                    Toast.makeText(context, "Already Exists...", Toast.LENGTH_LONG).show()
                    return
                }

                val downloadManager = getSystemService(context, DownloadManager::class.java)
                val request = DownloadManager.Request(uri)

                request.setTitle(fileName)
                request.setVisibleInDownloadsUi(true)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalFilesDir(
                    context,
                    SUB_DOWNLOAD_DIR,
                    fileName
                )

                downloadManager?.enqueue(request)
            } catch (exception: Exception) {
                Log.e(TAG, "Something went wrong: ${exception.message}")
            }
        }
    }

    private fun isPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            1
        )
    }

}

/*  private fun getSubtitles(subName: String, lang: String = "eng") {
        val call: Call<ArrayList<Subtitle>> = ApiClient.getClient.getSubtitles(subName, lang)

        call.enqueue(object : Callback<ArrayList<Subtitle>> {
            override fun onResponse(
                call: Call<ArrayList<Subtitle>>,
                response: Response<ArrayList<Subtitle>>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "successfully fetch data: ${response.code()}")

                    if (response.body().isNullOrEmpty()) {
                        emptyView.visibility = View.VISIBLE
                    } else {
                        subtitles = filterSubtitles(response.body()!!, subName)
                        recyclerView(subtitles!!)
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Subtitle>>, t: Throwable) {
                Log.d(TAG, "Unsuccessfully fetch data because: ${t.message}")
            }
        })
    }
    */