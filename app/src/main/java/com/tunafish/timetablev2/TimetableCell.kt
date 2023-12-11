package com.tunafish.timetablev2

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.TextView
import org.json.JSONObject
import java.time.LocalTime

class TimetableCell(obj : JSONObject) {

    private val daysToInt= mapOf(
        "Mon" to 0,
        "Tue" to 1,
        "Wed" to 2,
        "Thu" to 3,
        "Fri" to 4
    )

    private val day : Int? = daysToInt[obj.getString("day")]
    val startTime : LocalTime = makeTime(obj.getString("startTime"))
    private val endTime : LocalTime = makeTime(obj.getString("endTime"))
    private val subject : String = obj.getString("subject")
    private val type : String = obj.getString("type")
    private val room : String = obj.getString("room")
    private val teacher : String = obj.getString("teacher")



    //@RequiresApi(Build.VERSION_CODES.O)
    private fun makeTime(timeString : String) : LocalTime
    {
        val timeParts = timeString.split(":")

        val hours = timeParts[0].toInt()
        val minutes = timeParts[1].toInt()

        return LocalTime.of(hours, minutes, 0)
    }

    fun makeLayout(context: Context) : LinearLayout
    {

        val inflater = LayoutInflater.from(context)
        val view  = inflater.inflate(R.layout.timetable_cell, null) as LinearLayout

        view.setOnClickListener {
            val i = Intent(context, LoginPage::class.java)
            context.startActivity(i)
        }

        val classView = view.findViewById<TextView>(R.id.className)
        val timeView = view.findViewById<TextView>(R.id.timeFull)
        val roomView = view.findViewById<TextView>(R.id.roomNo)

        classView.text = subject
        timeView.text = "$startTime - $endTime"
        roomView.text = room


        return view
    }

    fun generateRemoteViews(rv: RemoteViews)
    {
        rv.setTextViewText(R.id.widget_title, subject)
        rv.setTextViewText(R.id.room, room)
        rv.setTextViewText(R.id.sTime, startTime.toString())

    }

    fun checkDay(d : String) : Boolean
    {
        return (daysToInt[d] == day)

    }
    fun checkDay(d : Int) : Boolean
    {
        return (d == day)

    }
}


