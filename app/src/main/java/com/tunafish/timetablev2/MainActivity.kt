package com.tunafish.timetablev2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    fun viewGen(timeBegin : String, timeEnd : String, classTitle : String, roomNumber : String ) : LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable)

        val scrollWindow = findViewById<LinearLayout>(R.id.scrollableWindow)
        val dataGrab = GetData()

        runBlocking {
            val jsonStr = dataGrab.get()
            var obj = JSONObject(jsonStr).getJSONArray("timetable")

            for (i in 0..<obj.length()){
                try {
                    Log.d("JSON PART", obj[i].toString())
                    val obji = JSONObject(obj[i].toString())

                    Log.d("JSON STRING", obji.getString("startTime"))


                    val timeButton: LinearLayout = viewGen(
                        obji.getString("startTime"),
                        obji.getString("endTime"),
                        obji.getString("subject"),
                        obji.getString("room")
                    )
                    scrollWindow.addView(timeButton)
                }
                catch (e : Exception){
                    Log.d("JSON E", e.toString())
                }
            }

        }






//        val navigateToLogin: Button = findViewById(R.id.button)
//        navigateToLogin.setOnClickListener {
//            val i = Intent(this, LoginPage::class.java)
//            startActivity(i)
//            //finish()
//        }



    }
}