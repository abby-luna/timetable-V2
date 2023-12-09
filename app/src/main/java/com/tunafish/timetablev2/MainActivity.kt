package com.tunafish.timetablev2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var json : JSONArray
    private var days = arrayOf("Mon", "Tue", "Wed","Thu","Fri")
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

    private fun generateJson()
    {
        runBlocking {
            val dataGrab = GetData()
            val jsonStr = dataGrab.get()
            json = JSONObject(jsonStr).getJSONArray("timetable")
        }
    }

    private fun clearScreen()
    {
        val parent : LinearLayout = findViewById(R.id.scrollableWindow)
        parent.removeAllViews()

    }
    private fun generateTimetable(day : Int)
    {

        // TODO : remove old views
        clearScreen()

        val scrollWindow = findViewById<LinearLayout>(R.id.scrollableWindow)
        for (i in 0..<json.length()) {
            try {
                val objI = JSONObject(json[i].toString())

                if(objI.getString("day") == days[day]) {
                    val timeButton: LinearLayout = viewGen(
                        objI.getString("startTime"),
                        objI.getString("endTime"),
                        objI.getString("subject"),
                        objI.getString("room")
                    )
                    scrollWindow.addView(timeButton)
                }
            } catch (e: Exception) {
                Log.d("JSON E", e.toString())
            }
        }

    }
    private fun generateSpinnerObject()
    {

        val calendar: Calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)



        // get our word list
        val strArray = resources.getStringArray(R.array.spinnerItems)
        val adapter = ArrayAdapter(this, R.layout.custom_dropdown_item, strArray)

        adapter.setDropDownViewResource(R.layout.custom_dropdown_item)
        val spinner : Spinner = findViewById(R.id.spinner)
        spinner.adapter = adapter

        if(day != Calendar.SUNDAY && day != Calendar.SATURDAY)
        {
            spinner.setSelection(day-2)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                generateTimetable(position)
                //Log.d("Selected", position.toString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable)

        generateJson()
        generateSpinnerObject()
        //generateTimetable()

    }
}