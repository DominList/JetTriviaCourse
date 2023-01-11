package com.example.jettriviacourse.component

import android.graphics.PathEffect
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviacourse.screens.QuestionsViewModel
import com.example.jettriviacourse.util.AppColors
import java.util.Locale

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    Log.d("Loading data", "List size: ${questions?.size}")

    if (viewModel.isDataLoading == true) {
        CircularProgressIndicator(modifier = Modifier.size(100.dp))
        Log.d("Loading data", "Questions are loading!")
    } else {
        questions?.forEachIndexed { index, questionsItem ->
            val formattedIndex = String.format(Locale.getDefault(), "%4d.", index + 1)
            Log.d("Loading data", "Questions: $formattedIndex ${questionsItem.question}")
        }

        //LazyColumn(content = )
    }
}

@Preview
@Composable
fun QuestionDisplay() {
    val pathEffect =
        androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start

        ) {
            QuestionTracker()
            DrawDottedLine(pathEffect = pathEffect)
            Column {
                Text(
                    modifier = Modifier.padding(6.dp).align(alignment = Alignment.Start).fillMaxHeight(0.3f).fillMaxWidth(),
                    text = "Text of the question?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite
                )
            }
        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: androidx.compose.ui.graphics.PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset.Zero,
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = AppColors.mLightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 27.sp
                )
            ) {

                append("Question $counter/")
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                ) {
                    append("$outOf ")
                }
            }
        },
        modifier = Modifier.padding(20.dp)
    )
}
