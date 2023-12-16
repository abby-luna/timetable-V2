package com.tunafish.timetablev2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.time.LocalTime
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var json : JSONObject
    
    private fun generateJson()
    {

        val dataGrab = GetData(this)
        runBlocking {
            val jsonStr = dataGrab.get()
            json = JSONObject(jsonStr)
        }
    }


    private fun generateTimetable(day : Int)
    {

        val scrollWindow = findViewById<LinearLayout>(R.id.scrollableWindow)
        scrollWindow.removeAllViews()

        if(json.has("Error"))
        {
            val v  = layoutInflater.inflate(R.layout.timetable_error, null) as LinearLayout
            scrollWindow.addView(v)
            return
        }

        try {
            val jObj = json.getJSONArray("timetable")
            val ttable : Timetable = Timetable()
            val calendar: Calendar = Calendar.getInstance()
            val today = calendar.get(Calendar.DAY_OF_WEEK)

            ttable.genTimeTable(jObj)
            ttable.traverse(scrollWindow, this, day, today-2)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
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


        // we need handler
        val handler = Handler(Looper.getMainLooper())

        val r: Runnable = object : Runnable {
            override fun run() {

                var lt = LocalTime.now()
                generateJson()


                var waitTime : Long = ((1800 - (((lt.minute % 30) * 60) + lt.second))  * 1000).toLong()
                handler.postDelayed(this, waitTime )
                generateSpinnerObject()
                Log.d("Handler", "${LocalTime.now()}");
            }
        }
        r.run()


    }
}