package com.swaminarayan.shikshapatriApp.domain.usecases

import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna

class EditAgna(
    private val agnaRepo: AgnaRepo,
    private val dailyFormRepo: DailyFormRepo
) {

    suspend operator fun invoke(agna: Agna) {
        agnaRepo.upsertAgna(agna)
        editAgnaInList(dailyFormRepo, agna)
    }

    private suspend fun editAgnaInList(dailyFormRepo: DailyFormRepo, agna: Agna) {
//        dailyFormRepo.dailyFormList().forEach { form ->
//            val dailyAgnasList: MutableList<DailyAgna> = form.dailyAgnas.toMutableList()
//
////            val contains = dailyAgnasList.contains()
//
//            val dailyForm = DailyForm(
//                id = form.id,
//                dailyAgnas = dailyAgnasList.toList(),
//                date = form.date,
//            )
//
//            dailyFormRepo.upsertDailyForm(dailyForm)
//        }
    }


}