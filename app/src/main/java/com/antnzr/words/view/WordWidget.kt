package com.antnzr.words.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.widget.RemoteViews
import com.antnzr.words.R
import com.antnzr.words.data.LocalTsvWordsRepository
import com.antnzr.words.data.WordPair
import com.antnzr.words.utils.*
import com.antnzr.words.view.wordDetails.WordDetailsActivity
import com.antnzr.words.view.wordList.WordListActivity


class WordWidget : AppWidgetProvider() {

    private var repository = LocalTsvWordsRepository()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetId, repository.getCurrentWord(context))
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        intent?.let {
            if (!intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) {
                return
            }
        }

        when (intent?.action) {
            GOOGLE_TRANSLATE_ACTION -> {
                showWordDetailsWith(
                    GOOGLE_TRANSLATE,
                    intent.getParcelableExtra<Parcelable>(WORD_NEED_DETAILS) as WordPair,
                    context
                )
            }
            CONTEXT_REVERSO_ACTION -> {
                showWordDetailsWith(
                    CONTEXT_REVERSO,
                    intent.getParcelableExtra<Parcelable>(WORD_NEED_DETAILS) as WordPair,
                    context
                )
            }
            NEXT_WORD_ACTION,
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                context?.let {
                    updateAppWidget(
                        it,
                        getIntExtra(intent),
                        repository.getNextWord(context)
                    )
                }
            }
            PREVIOUS_WORD_ACTION -> {
                context?.let {
                    updateAppWidget(
                        it,
                        getIntExtra(intent),
                        repository.getPreviousWord(it)
                    )
                }
            }
            LIST_ACTION -> {
                val listIntent = Intent(context, WordListActivity::class.java)
                listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getIntExtra(intent))
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
            ?: repository.getRandomWord(context)

        repository.saveCurrentWord(context, currentWordPair?.from)

        val views: RemoteViews = RemoteViews(
            context.packageName,
            R.layout.word_widget
        )
            .apply {
                setOnClickPendingIntent(
                    R.id.google_translate_btn,
                    pendingIntent(
                        appWidgetId, context,
                        GOOGLE_TRANSLATE_ACTION, currentWordPair
                    )
                )
                setOnClickPendingIntent(
                    R.id.context_reverso_btn,
                    pendingIntent(
                        appWidgetId, context,
                        CONTEXT_REVERSO_ACTION, currentWordPair
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
                    R.id.word,
                    pendingIntent(
                        appWidgetId, context,
                        AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    )
                )
                setTextViewText(R.id.word, formatWordPair(currentWordPair))
            }

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
    }
}

private fun pendingIntent(
    appWidgetId: Int,
    context: Context,
    action: String,
    wordPair: WordPair? = null
): PendingIntent {
    val intent = Intent(context, WordWidget::class.java)
    intent.action = action
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    wordPair.let { intent.putExtra(WORD_NEED_DETAILS, it) }

    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

private fun getIntExtra(intent: Intent): Int {
    return intent.getIntExtra(
        AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID
    )
}

private fun formatWordPair(wordPair: WordPair?): String {
    return "${wordPair?.from} | ${wordPair?.to}"
}


fun updateWordWidget(context: Context?) {
    context?.let {
        val widgetManager = AppWidgetManager.getInstance(it)
        val ids: IntArray = widgetManager.getAppWidgetIds(
            ComponentName(it, WordWidget::class.java)
        )

        val widget = WordWidget()
        widget.onUpdate(it, widgetManager, ids)
    }
}

fun showWordDetailsWith(resource: String, wordPair: WordPair?, context: Context?) {
    val intent = Intent(context, WordDetailsActivity::class.java)
    intent.putExtra(WEB_RESOURCE, resource)
    intent.putExtra(WORD_NEED_DETAILS, wordPair)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    context?.startActivity(intent)
}
