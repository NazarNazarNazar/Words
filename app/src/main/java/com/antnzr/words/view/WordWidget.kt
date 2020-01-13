package com.antnzr.words.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.antnzr.words.data.LocalTsvWords
import com.antnzr.words.R
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.*


class WordWidget : AppWidgetProvider() {
    private val TAG = WordWidget::class.java.simpleName

    var service = LocalTsvWords()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate: started...")

        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetId, service.getCurrentWord(context))
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started")
        super.onReceive(context, intent)

        intent?.let {
            if (!intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                return
            }
        }

        when (intent?.action) {
            WORD_DETAILS_ACTION -> {
                val detailsIntent = Intent(context, WordDetailsActivity::class.java)
                detailsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getIntExtra(intent))
                detailsIntent.putExtra(CURRENT_WORD, intent.getStringExtra(CURRENT_WORD))
                detailsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                context?.startActivity(detailsIntent)
            }
            NEXT_WORD_ACTION,
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                context?.let {
                    updateAppWidget(
                        it,
                        getIntExtra(intent),
                        service.getNextWord(context)
                    )
                }
            }
            PREVIOUS_WORD_ACTION -> {
                context?.let {
                    updateAppWidget(
                        it,
                        getIntExtra(intent),
                        service.getPreviousWord(it)
                    )
                }
            }
            LIST_ACTION -> {
                val listIntent = Intent(context, HolderActivity::class.java)
                listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getIntExtra(intent))
                listIntent.putExtra(CURRENT_WORD, listIntent.getStringExtra(CURRENT_WORD))
                listIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                context?.startActivity(listIntent)
            }
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetId: Int,
        wordPair: WordPair? = null
    ) {
        val currentWordPair: WordPair? = wordPair
            .let { it }
            ?: service.getRandomWord(context)

        service.saveCurrentWord(context, currentWordPair?.from)

        val views: RemoteViews = RemoteViews(
            context.packageName,
            R.layout.word_widget
        )
            .apply {
                setOnClickPendingIntent(
                    R.id.details_btn,
                    pendingIntent(
                        appWidgetId, context,
                        WORD_DETAILS_ACTION, currentWordPair?.from
                    )
                )
                setOnClickPendingIntent(
                    R.id.left_btn,
                    pendingIntent(
                        appWidgetId, context,
                        PREVIOUS_WORD_ACTION
                    )
                )
                setOnClickPendingIntent(
                    R.id.right_btn,
                    pendingIntent(
                        appWidgetId, context,
                        NEXT_WORD_ACTION
                    )
                )
                setOnClickPendingIntent(
                    R.id.list_btn,
                    pendingIntent(
                        appWidgetId, context,
                        LIST_ACTION
                    )
                )
                setOnClickPendingIntent(
                    R.id.do_not_display_btn,
                    pendingIntent(
                        appWidgetId, context,
                        DO_NOT_DISPLAY_WORD_ACTION
                    )
                )
                setOnClickPendingIntent(
                    R.id.word,
                    pendingIntent(
                        appWidgetId,
                        context,
                        AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    )
                )
                setTextViewText(R.id.word, currentWordPair?.toString())
            }

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
    }
}

private fun pendingIntent(
    appWidgetId: Int,
    context: Context,
    action: String,
    word: String? = null
): PendingIntent {
    val intent = Intent(context, WordWidget::class.java)
    intent.action = action
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    word.let { intent.putExtra(CURRENT_WORD, it) }

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

private fun getIntExtra(intent: Intent): Int {
    return intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
}
