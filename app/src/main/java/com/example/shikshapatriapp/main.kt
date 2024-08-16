package com.example.shikshapatriapp
import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.math.floor

@RequiresApi(Build.VERSION_CODES.O)
fun main() {


    val per = String.format("%.1f", (1180) / (1460).toFloat() * 100).toFloat()
    val bal = String.format("%.1f", per - per.toInt()).toFloat() * 10
    val ceil = floor(per)

    println(bal.toInt())
    println(per)
    println(ceil)


}