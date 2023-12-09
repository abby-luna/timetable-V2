package com.tunafish.timetablev2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private fun viewGen(timeBegin : String, timeEnd : String, classTitle : String, roomNumber : String ) : LinearLayout
    {
        val view  = layoutInflater.inflate(R.layout.timetable_cell, null) as LinearLayout

        view.setOnClickListener {
            val i = Intent(this, LoginPage::class.java)
            startActivity(i)
        }

        val classView = view.findViewById<TextView>(R.id.className)
        val timeView = view.findViewById<TextView>(R.id.timeFull)
        val roomView = view.findViewById<TextView>(R.id.roomNo)

        classView.text = classTitle
        timeView.text = "$timeBegin - $timeEnd"
        roomView.text = roomNumber


        return view;
    }

    private fun generateTimetable()
    {
        val scrollWindow = findViewById<LinearLayout>(R.id.scrollableWindow)
        val dataGrab = GetData()

        runBlocking {
            val jsonStr = dataGrab.get()
            var obj = JSONObject(jsonStr).getJSONArray("timetable")

            for (i in 0..<obj.length()) {
                try {
                    val objI = JSONObject(obj[i].toString())
                    val timeButton: LinearLayout = viewGen(
                        objI.getString("startTime"),
                        objI.getString("endTime"),
                        objI.getString("subject"),
                        objI.getString("room")
                    )
                    scrollWindow.addView(timeButton)
                } catch (e: Exception) {
                    Log.d("JSON E", e.toString())
                }
            }
        }
    }
    private fun generateSpinnerObject()
    {
        // get our word list
        val strArray = resources.getStringArray(R.array.spinnerItems)
        val adapter = ArrayAdapter(this, R.layout.custom_dropdown_item, strArray)

        adapter.setDropDownViewResource(R.layout.custom_dropdown_item)
        val spinner : Spinner = findViewById(R.id.spinner)
        spinner.adapter = adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable)

        generateSpinnerObject()
        generateTimetable()

    }
}