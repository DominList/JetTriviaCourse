package com.example.jettriviacourse.repository

import android.util.Log
import com.example.jettriviacourse.data.DataOrException
import com.example.jettriviacourse.model.QuestionItem
import com.example.jettriviacourse.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    //ArrayList<QuestionItem>(emptyList())

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {

            dataOrException.exception = e
            Log.d(TAG, "getAllQuestions: ${dataOrException.exception!!.localizedMessage}")

        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }

    companion object {
        const val TAG = "Exception"
    }
}