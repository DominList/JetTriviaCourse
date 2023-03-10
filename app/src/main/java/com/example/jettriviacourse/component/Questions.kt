package com.example.jettriviacourse.component

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettriviacourse.model.QuestionItem
import com.example.jettriviacourse.screens.QuestionsViewModel
import com.example.jettriviacourse.util.AppColors

@Composable
fun Questions(viewModel: QuestionsViewModel) {

    val questions = viewModel.data.value.data?.toMutableList()
    Log.d("Loading data", "List size: ${questions?.size}")

    val questionIndex = remember {
        mutableStateOf(0)
    }

    val questionsNumber = remember {
        mutableStateOf(0)
    }

    if (viewModel.isDataLoading == true) {
        LoadingPage()
        Log.d("Loading data", "Questions are loading!")
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (e: Exception) {
            null
        }
        questionsNumber.value = questions?.size ?: 0
        question?.let {
            QuestionDisplay(
                questionItem = question,
                questionIndex = questionIndex,
                questionsNumber = questionsNumber,
                onNextClicked = {
                    questionIndex.value = questionIndex.value + 1
                }
            )
        }
    }
}

@Preview
@Composable
fun LoadingPage() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.mDarkPurple
    )
    {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = AppColors.mOffWhite
            )
        }
    }
}


@Composable
fun QuestionDisplay(
    questionItem: QuestionItem,
    questionIndex: MutableState<Int> = mutableStateOf(0),
    questionsNumber: MutableState<Int> = mutableStateOf(0),
    onNextClicked: (Int) -> Unit = {}
) {
    val choicesState = remember(questionItem) {
        questionItem.choices.toMutableList()
    }

    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }

    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)
    }

    val updateAnswer: (Int) -> Unit = remember(questionItem) {
        { index ->
            answerState.value = index
            correctAnswerState.value = choicesState[index] == questionItem.answer
        }
    }

    val pathEffect =
        PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 10f)

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
            ShowProgress(score = questionIndex.value + 1, maxIndex = questionsNumber.value)
            QuestionTracker(counter = questionIndex.value + 1, outOf = questionsNumber.value)
            DrawDottedLine(pathEffect = pathEffect)
            Column {
                Text(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth(),
                    text = questionItem.question,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColors.mOffWhite
                )

                // choices
                choicesState.forEachIndexed { index, answer ->
                    val onAreaClicked: () -> Unit = { updateAnswer(index) }
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = AppColors.run {
                                        listOf(
                                            mOffDarkPurple,
                                            mBlue,
                                            mLightGray
                                        )
                                    },
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    bottomEndPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(color = Color.Transparent)
                            .clickable { onAreaClicked() },
                        verticalAlignment = CenterVertically,
                    ) {
                        RadioButton(
                            selected = answerState.value == index,
                            onClick = onAreaClicked,
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (correctAnswerState.value == true && index == answerState.value)
                                    Color.Green.copy(alpha = .7f)
                                else
                                    Color.Red.copy(alpha = .7f),
                                unselectedColor = AppColors.mOffWhite
                            )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true && index == answerState.value)
                                        Color.Green.copy(alpha = .7f)
                                    else if (correctAnswerState.value == false && index == answerState.value)
                                        Color.Red.copy(alpha = .7f)
                                    else AppColors.mOffWhite,
                                    fontSize = 17.sp
                                )
                            )
                            {
                                append(answer)
                            }
                        }
                        Text(text = annotatedString, modifier = Modifier
                            .padding(6.dp)
                            .clickable {
                                onAreaClicked()
                            })
                    }
                }
                Button(
                    onClick = { onNextClicked(questionIndex.value) },
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = buttonColors(containerColor = AppColors.mLightBlue),
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}


@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
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


@Preview
@Composable
fun ShowProgress(score: Int = 1, maxIndex: Int = 100) {

    val gradient = Brush.verticalGradient(
        listOf(
            Color(0xE6008EFF),
            Color(0xFF4CAF50)
        ), tileMode = TileMode.Repeated
    )

    val progressFactor by remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = AppColors.run {
                        listOf(
                            mOffDarkPurple,
                            mBlue,
                            mLightGray
                        )
                    },
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    bottomEndPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50
                )
            )
            .background(color = Color.Transparent),
        verticalAlignment = CenterVertically,
    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            onClick = {}
        ) {
            Text(
                text = (score * 10).toString(),
                modifier = Modifier.clip(RoundedCornerShape(50))
            )
        }
    }
}

