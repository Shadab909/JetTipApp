package com.android.jettipapp.utils

fun calculateTotalTip( totalBill : Double, tipPercentage: Int): Double {
    return if (
        totalBill > 1 && totalBill.toString().isNotEmpty()
    ) (totalBill * tipPercentage) / 100 else 0.0
}

fun calculateTotalPerPerson
            ( totalBill : Double, tipPercentage: Int,splitBy : Int) : Double

{
    val bill = calculateTotalTip(totalBill,tipPercentage) + totalBill
    return (bill/splitBy)
}

