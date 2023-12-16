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

    fun makeLayout(context: Context, layDay: Int) : LinearLayout
    {

        val inflater = LayoutInflater.from(context)
        val view  = inflater.inflate(R.layout.timetable_cell, null) as LinearLayout


        view.setOnClickListener {
            val i = Intent(context, SubjectInformation::class.java)
            i.putExtra("sTime", startTime.toString())
            i.putExtra("eTime", endTime.toString())
            i.putExtra("subject", subject)
            i.putExtra("type", type)
            i.putExtra("room", room)
            i.putExtra("teacher", teacher)

            context.startActivity(i)
        }

        val classView = view.findViewById<TextView>(R.id.className)
        val timeView = view.findViewById<TextView>(R.id.timeFull)
        val roomView = view.findViewById<TextView>(R.id.roomNo)
        val innerTimetableCell = view.findViewById<LinearLayout>(R.id.innerTimetableCell)

        classView.text = subject
        timeView.text = "$startTime - $endTime"
        roomView.text = room
        if(day != layDay)
        {
            innerTimetableCell.background = context.getDrawable(R.drawable.border_not_today)

        }
        else {
            val lt: LocalTime = LocalTime.now()
            if (endTime < lt) {
                innerTimetableCell.background = context.getDrawable(R.drawable.border_past)
            }
            if (startTime < lt && endTime > lt) {
                innerTimetableCell.background = context.getDrawable(R.drawable.border_current)
            }
        }
        return view
    }

    fun generateRemoteViews(rv: RemoteViews)
    {
        rv.setTextViewText(R.id.widget_title, subject)
        rv.setTextViewText(R.id.room, room)
        rv.setTextViewText(R.id.sTime, startTime.toString())

    }

//    fun checkDay(d : String) : Boolean
//    {
//        return (daysToInt[d] == day)
//
//    }
    fun checkDay(d : Int) : Boolean
    {
        return (d == day)
    }
}


