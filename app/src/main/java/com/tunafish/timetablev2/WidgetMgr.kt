package com.tunafish.timetablev2

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class WidgetMgr : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {

        val remoteViews = RemoteViews(context.packageName, R.layout.timetable_cell_widget)
        val tt = Timetable()
        tt.genFromFile(context)
        tt.setWidgetNext(remoteViews)



        // Update your widget's content

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

    }

}