package com.tunafish.timetablev2


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SubjectInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subject_info)

        findViewById<TextView>(R.id.subject).text =  "${intent.getStringExtra("subject")}"
        findViewById<TextView>(R.id.typeRoom).text =  "${intent.getStringExtra("type")} : ${intent.getStringExtra("room")}"
        findViewById<TextView>(R.id.teacher).text =  "${intent.getStringExtra("teacher")}"
        findViewById<TextView>(R.id.time).text =  "${intent.getStringExtra("sTime")} - ${intent.getStringExtra("eTime")}"
//        i.putExtra("sTime", startTime.toString())
//        i.putExtra("eTime", endTime.toString())
//        i.putExtra("subject", subject)
//        i.putExtra("type", type)
//        i.putExtra("room", room)
//        i.putExtra("teacher", teacher)
    }
}
