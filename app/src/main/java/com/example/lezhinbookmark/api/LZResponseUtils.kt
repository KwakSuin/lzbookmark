package com.example.lezhinbookmark.api

import com.example.lezhinbookmark.common.LZErrorMessage
import org.json.JSONObject
import retrofit2.Response

sealed class ApiResult<out T: Any> {
    data class OnSuccess<out T: Any>(val responseDTO: T) : ApiResult<T>()
    data class OnFailure(val errorObject: LZErrorMessage?): ApiResult<Nothing>()
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
                        ApiResult.OnFailure(LZErrorMessage(id = -1, messageString = errorMessage))
                    } else {
                        ApiResult.OnFailure(LZErrorMessage(id = -1, messageString = "No Data"))
                    }
                } else {
                    ApiResult.OnFailure(LZErrorMessage(id = -1, messageString = "No Data"))
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
                ApiResult.OnFailure(LZErrorMessage(id = -1, messageString = errorMessage))
            }

            ApiResult.OnFailure(LZErrorMessage(id = -2, messageString = "Network Error"))
        }

    } catch (e: Exception) {
        ApiResult.OnFailure(LZErrorMessage(id = -2, messageString = "Network Error"))
    }
}