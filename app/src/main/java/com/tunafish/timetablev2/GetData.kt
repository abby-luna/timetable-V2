package com.tunafish.timetablev2

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

class GetData
{
    suspend fun get() : String
    {
        // TODO: Implement rest api : FUTURE
        val endpoint = "https://raw.githubusercontent.com/abby-luna/abby-luna/main/ttable.json"
        try {
            val client = HttpClient(CIO)
            val response: HttpResponse = client.get(endpoint)

            Log.d("Network", response.status.toString())
            Log.d("Network", response.bodyAsText())

            client.close()

            return response.bodyAsText()

        }
        catch (e: Exception)
        {
            Log.d("error", "Error Hit $e")
        }

        return ""
    }

}