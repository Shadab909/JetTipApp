package com.android.jettipapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.jettipapp.components.InputField
import com.android.jettipapp.ui.theme.JetTipAppTheme
import com.android.jettipapp.utils.calculateTotalPerPerson
import com.android.jettipapp.utils.calculateTotalTip
import com.android.jettipapp.widgets.RoundIconButton

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()

            }
        }
    }
}

@Composable
fun MyApp(content : @Composable () -> Unit) {
    JetTipAppTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}
@ExperimentalComposeUiApi
@Composable
fun MainContent() {

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPerson = remember{
        mutableStateOf(0.0)
    }
    val splitByState = remember {
        mutableStateOf(1)
    }
    val totalBillState = remember{
        mutableStateOf("")
    }
    Column(modifier = Modifier.padding(12.dp)) {
        BillForm(
            totalBillState = totalBillState,
            splitByState = splitByState,
            tipAmountState = tipAmountState,
            totalPerPerson = totalPerPerson
        ){
        }
    }

}

@Composable
fun TopHeader(totalPerPerson : Double = 134.0) {
    val total = "%.2f".format(totalPerPerson)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp),
        shape = RoundedCornerShape(corner = CornerSize(12.dp)),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Total Per Person", style = TextStyle(fontSize = 24.sp))
            Text(text = "$$total", style = MaterialTheme.typography.h4, fontWeight = FontWeight.Bold)
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    totalBillState : MutableState<String>,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPerson : MutableState<Double>,
    onValueChanged : (String ) -> Unit
) {



    val keyboardController = LocalSoftwareKeyboardController.current

    val validState = remember(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.value * 100).toInt()


        TopHeader(totalPerPerson = totalPerPerson.value)

        Surface(
            modifier = modifier
                .padding(2.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(8.dp)),
            border = BorderStroke(width = 1.dp, color = Color.LightGray)
        ) {
            Column(
                modifier = modifier.padding(6.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) {
                            return@KeyboardActions
                        }
                        onValueChanged(totalBillState.value.trim())
                        keyboardController?.hide()
                    }
                )

                if (validState) {
                    Row(
                        modifier = modifier.padding(3.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "Split", modifier = Modifier.align(CenterVertically)
                        )
                        Spacer(modifier = modifier.width(120.dp))
                        Row(
                            modifier = modifier.padding(3.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            RoundIconButton(
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    splitByState.value =
                                        if (splitByState.value > 1) splitByState.value - 1
                                        else 1
                                    totalPerPerson.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        tipPercentage = tipPercentage,
                                        splitBy = splitByState.value
                                    )
                                }
                            )

                            Text(
                                text = splitByState.value.toString(),
                                modifier = modifier
                                    .align(CenterVertically)
                                    .padding(start = 9.dp, end = 9.dp)
                            )

                            RoundIconButton(
                                imageVector = Icons.Default.Add,
                                onClick = { splitByState.value = splitByState.value + 1

                                    totalPerPerson.value = calculateTotalPerPerson(
                                        totalBill = totalBillState.value.toDouble(),
                                        tipPercentage = tipPercentage,
                                        splitBy = splitByState.value
                                    )
                                }
                            )
                        }
                    }

                    Row(
                        modifier = modifier
                            .padding(horizontal = 3.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Tip", modifier = modifier
                                .align(CenterVertically)
                        )
                        Spacer(modifier = modifier.width(220.dp))
                        Text(
                            text = "$${tipAmountState.value}", modifier = modifier
                                .align(CenterVertically)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$tipPercentage %")
                        Slider(
                            value = sliderPositionState.value,
                            onValueChange = { newValue ->
                                sliderPositionState.value = newValue
                                tipAmountState.value =
                                    calculateTotalTip(
                                        totalBillState.value.toDouble(),
                                        tipPercentage
                                    )
                                totalPerPerson.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    tipPercentage = tipPercentage,
                                    splitBy = splitByState.value
                                )

                            },
                            modifier = modifier.padding(start = 10.dp, end = 10.dp),
                            steps = 5
                        )
                    }

                } else {
                    Box { }
                }
            }
        }
}


@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {
            MainContent()
        }
    }
}