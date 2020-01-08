package com.antnzr.words

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import kotlin.random.Random


class WordWidget : AppWidgetProvider() {
    private val TAG = WordWidget::class.java.simpleName

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate: started...")

        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: started")
        super.onReceive(context, intent)

        if (intent?.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE
            && intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)
        ) {
            context?.let {
                updateAppWidget(it, AppWidgetManager.getInstance(it), getIntExtra(intent))
            }
        }
    }
}

private fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val views: RemoteViews = RemoteViews(context.packageName, R.layout.word_widget)
        .apply {
            setOnClickPendingIntent(R.id.word, changeWordPendingIntent(appWidgetId, context))
            setTextViewText(R.id.word, randomWordsPair(context))
        }

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun changeWordPendingIntent(appWidgetId: Int, context: Context): PendingIntent {
    val intent = Intent(context, WordWidget::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

private fun randomWordsPair(context: Context): String {
    val service = LocalTsvWords()
    val wordPairs = service.getWords(context)

    val wordPair = wordPairs[Random.nextInt(0, wordPairs.size)]
    return "${wordPair.from} : ${wordPair.to}"
}

private fun getIntExtra(intent: Intent): Int {
    return intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
}
