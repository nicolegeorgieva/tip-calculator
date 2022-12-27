package com.example.tipcalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.components.InputField
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import com.example.tipcalculator.widgets.RoundIconButton
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Column() {
                    TopHeader()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
// A surface container using the 'background' color from the theme
    TipCalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double = 134.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = DecimalFormat("###,###,##0.00").format(totalPerPerson)

            Text(
                text = "Total Per Person", style = MaterialTheme.typography.h5
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    Column() {
        BillForm()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier, onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    var splittingNumberState by remember {
        mutableStateOf(1)
    }

    var sliderPositionState by remember {
        mutableStateOf(0f)
    }

    var tipPercentage = (sliderPositionState * 100).toInt()

    TopHeader()

    Surface(
        modifier = Modifier
            .padding(15.dp, 1.5.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions

                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                })

//            if (validState) {
            Row(
                modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Split",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(120.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                        if (splittingNumberState > 1) {
                            splittingNumberState -= 1
                        }
                    })

                    Text(
                        text = "$splittingNumberState",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )

                    RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                        if (splittingNumberState < 100) {
                            splittingNumberState += 1
                        }
                    })
                }
            }

            //Tip Row
            Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                Text(
                    text = "Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(200.dp))

                Text(
                    text = "$33.00",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$tipPercentage %")

                Spacer(modifier = Modifier.height(14.dp))

                //Slider
                Slider(value = sliderPositionState,
                    onValueChange = { newVal ->
                        sliderPositionState = newVal
                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    steps = 5,
                    onValueChangeFinished = {

                    })
            }

//            } else {
//                Box() {
//
//                }
//            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    TipCalculatorTheme {
        MyApp {
            Column() {
                TopHeader()
            }
        }
    }
}