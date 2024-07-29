package com.example.lezhinbookmark.api

import android.content.Context
import com.example.lezhinbookmark.common.LZConstants
import org.json.JSONObject
import retrofit2.Response

sealed class ApiResult<out T: Any> {
    data class OnSuccess<out T: Any>(val responseDTO: T) : ApiResult<T>()
    data class OnFailure(val errorCode: Int?, val errorMessage: String?, val errorBody: Any? = null): ApiResult<Nothing>()
}

suspend fun <T: Any> checkApiResponse(call: suspend() -> Response<T?>): ApiResult<T> {
    return try {
        val response = call.invoke()
        val body: T? = response.body()
        val errorBody = response.errorBody()

        var errorMessage: String? = null

        if(response.isSuccessful) {
            if(body != null) {
                ApiResult.OnSuccess(body)
            } else {
                if (errorBody != null) {
                    val errorObject = JSONObject(errorBody.string())
                    if(errorObject.has("message")) {
                        errorMessage = errorObject.get("message").toString()
                        ApiResult.OnFailure(-1, errorMessage)
                    } else {
                        ApiResult.OnFailure(-1, "No Data")
                    }
                } else {
                    ApiResult.OnFailure(-1, "No Data")
                }
            }
        } else {
            if(errorBody != null) {
                val error = JSONObject(errorBody.string())

                if (error.has("error")) {
                    val errorObject = JSONObject(error.get("error").toString())

                    if(errorObject.has("message")) {
                        errorMessage = errorObject.get("message").toString()
                    }
                } else {
                    if(error.has("message")) {
                        errorMessage = error.get("message").toString()
                    }
                }
                ApiResult.OnFailure(-1, errorMessage)
            }

            ApiResult.OnFailure(-2, "Network Error")
        }

    } catch (e: Exception) {
        ApiResult.OnFailure(-2, "Network Error")
    }
}