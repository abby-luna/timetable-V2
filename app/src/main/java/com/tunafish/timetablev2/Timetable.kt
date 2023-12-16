package com.tunafish.timetablev2

import android.content.Context
import android.widget.LinearLayout
import android.widget.RemoteViews
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalTime
import java.util.Calendar


class Timetable
{

    private var cells : MutableList<TimetableCell> = mutableListOf()
    private var size = 0
    fun traverse(v: LinearLayout, c: Context, d: Int, today: Int) {

        val s = cells.sortedBy { it.startTime }

        for (i in 0..<size) {
            if(s[i].checkDay(d))
                v.addView(s[i].makeLayout(c, today))
        }
    }
    fun insert(t: TimetableCell)
    {
        cells.add(t)
        size++
    }

    fun setWidgetNext(r: RemoteViews)
    {
        val s = cells.sortedBy { it.startTime }
        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)


        val lt : LocalTime = LocalTime.now()
        for (i in 0..<size)
        {
            if(s[i].startTime > lt && s[i].checkDay(day-2))
            {
                s[i].generateRemoteViews(r)
                return
            }
        }

        r.setTextViewText(R.id.widget_title, "No more classes for today")
        r.setTextViewText(R.id.room, "")
        r.setTextViewText(R.id.sTime, "")
    }

    fun genTimeTable(ja: JSONArray) {
        for (i in 0..<ja.length()) {
            insert(TimetableCell( JSONObject(ja[i].toString())))
        }
    }

    fun genFromFile(c: Context)
    {
        val d = GetData(c)
        val j = JSONObject(d.readFile())
        if(j.has("Error"))
        {
            return
        }
        genTimeTable(j.getJSONArray("timetable"))
    }
}