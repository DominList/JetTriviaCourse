package com.example.jettriviacourse.screens

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettriviacourse.data.DataOrException
import com.example.jettriviacourse.model.QuestionItem
import com.example.jettriviacourse.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(private val repository: QuestionRepository) : ViewModel() {

    val data: MutableState<DataOrException<ArrayList<QuestionItem>, Boolean, Exception>> =
        mutableStateOf(
            DataOrException(null, true, Exception(""))
        )

    val isDataLoading get() = data.value.loading

    init {
        getAllQuestions()
    }

    private fun getAllQuestions() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllQuestions()
            if (data.value.data.toString().isNotEmpty()) {
                data.value.loading = false
            }
        }
    }

    fun getAllQuestionsNumber(): Int = data.value.data?.size ?: 0

}