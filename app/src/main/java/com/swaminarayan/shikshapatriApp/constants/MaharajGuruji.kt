package com.swaminarayan.shikshapatriApp.constants

import com.swaminarayan.shikshapatriApp.R

data class MaharajGuruji(
    val image: Int,
    val isLeftSideFace: Boolean
)

val gurujiList = listOf(
    MaharajGuruji(R.drawable.guruji_1, false),
    MaharajGuruji(R.drawable.guruji_2, false),
    MaharajGuruji(R.drawable.guruji_6, false),
    MaharajGuruji(R.drawable.guruji_3, true),
    MaharajGuruji(R.drawable.guruji_4, true),
    MaharajGuruji(R.drawable.guruji_5, true)
)
val gurujiFrontFaceList = listOf(
    MaharajGuruji(R.drawable.guruji_1, false),
    MaharajGuruji(R.drawable.guruji_2, false),
    MaharajGuruji(R.drawable.guruji_6, false)
)

val maharajList = listOf(
    MaharajGuruji(R.drawable.maharaj_1, false),
    MaharajGuruji(R.drawable.maharaj_2, false),
    MaharajGuruji(R.drawable.maharaj_3, false),
    MaharajGuruji(R.drawable.maharaj_4, false),
    MaharajGuruji(R.drawable.maharaj_5, false),
    MaharajGuruji(R.drawable.maharaj_6, true),
    MaharajGuruji(R.drawable.maharaj_7, false),
    MaharajGuruji(R.drawable.maharaj_8, false),
    MaharajGuruji(R.drawable.maharaj_9, false),
    MaharajGuruji(R.drawable.maharaj_10, false),
    MaharajGuruji(R.drawable.maharaj_11, false),
    MaharajGuruji(R.drawable.maharaj_12, false),
    MaharajGuruji(R.drawable.maharaj_13, false),
    MaharajGuruji(R.drawable.maharaj_14, false),
    MaharajGuruji(R.drawable.maharaj_15, false),
    MaharajGuruji(R.drawable.maharaj_16, false),
    MaharajGuruji(R.drawable.maharaj_17, true),
    MaharajGuruji(R.drawable.maharaj_18, false),
    MaharajGuruji(R.drawable.maharaj_19, false),
)