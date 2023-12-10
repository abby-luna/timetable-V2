package com.tunafish.timetablev2

import android.content.Context
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class GetData(c: Context)
{

    private val context : Context = c
    private val error = "{\"Error\":1}"
    private fun writeToFile(data: String)
    {
        val file = "timetable.json"
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun readFile() : String{
        try {
            var fileInputStream: FileInputStream? = context.openFileInput("timetable.json")
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String?
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }

            if (stringBuilder.toString().isEmpty()) {
                return error
            }
            return stringBuilder.toString()
        }
        catch (e: Exception)
        {
            return error;
        }
    }
    suspend fun get() : String
    {
        // TODO: Implement rest api
        val endpoint = "https://raw.githubusercontent.com/abby-luna/abby-luna/main/ttable.json"
        try {
            val client = HttpClient(CIO)
            val response: HttpResponse = client.get(endpoint)

            Log.d("Network", response.status.toString())
            Log.d("Network", response.bodyAsText())

            client.close()
            writeToFile(response.bodyAsText())
            return response.bodyAsText()

        }
        catch (e: Exception)
        {
            Log.d("error", "Error Hit $e")
        }

        return readFile()

    }

}

