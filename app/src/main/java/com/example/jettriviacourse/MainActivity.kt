package com.example.jettriviacourse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jettriviacourse.screens.QuestionsViewModel
import com.example.jettriviacourse.ui.theme.JetTriviaCourseTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTriviaCourseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TriviaHome()
                }
            }
        }
    }
}

@Composable
fun TriviaHome(viewModel: QuestionsViewModel = hiltViewModel()) {
    Questions(viewModel = viewModel)
}

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    Log.d(MainActivity::class.java.name, "List size: ${questions?.size}")

    if (viewModel.isDataLoading == true) {
        Log.d("Loading data", "Questions are loading!")
    } else {
        questions?.forEach {
                questionsItem ->
            Log.d("Loading data", "Questions: ${questionsItem.question}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetTriviaCourseTheme {
        TriviaHome()
    }
}