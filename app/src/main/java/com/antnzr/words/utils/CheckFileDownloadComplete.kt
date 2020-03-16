package com.antnzr.words.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.util.Log
import java.io.File


class CheckFileDownloadComplete : BroadcastReceiver() {
    private val TAG = this.javaClass.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
            Log.d(TAG, "download file complete")

            val query = DownloadManager.Query()
            query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0))
            val manager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val cursor: Cursor = manager.query(query)
            if (cursor.moveToFirst()) {
                if (cursor.count > 0) {
                    val status: Int =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        handleSuccess(context, cursor)
                    }
                }
            }
            cursor.close()
        }
    }

    private fun handleSuccess(context: Context, cursor: Cursor) {
        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            .let {
                val file = getFile(it)
                val unzipTo = unzipToDirName(context, file)

                FileUtils.unzip(file, unzipTo.absolutePath).let { it ->
                    if (it) {
                        unzipTo.listFiles().iterator().forEach { file ->
                            if (!file.name.endsWith(SUBTITLE_EXTENSION)) {
                                file.delete()
                            }
                        }
                    }
                }
            }
    }

    private fun getFile(str: String): File {
        return File(Uri.parse(str).path)
    }

    private fun unzipToDirName(context: Context, file: File): File {
        val subDir: File? = context.getExternalFilesDir(SUB_LOCAL_DIR)
        if (!subDir?.exists()!!) {
            subDir.mkdir()
        }

        return subDir.resolve(file.name.replace(".zip", ""))
    }
}
