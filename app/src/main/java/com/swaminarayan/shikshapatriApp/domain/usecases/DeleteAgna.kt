package com.swaminarayan.shikshapatriApp.domain.usecases

import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm

class DeleteAgna(
    private val agnaRepo: AgnaRepo,
    private val dailyFormRepo: DailyFormRepo
) {

    suspend operator fun invoke(agna: Agna) {
        agnaRepo.deleteAgna(agna)
        removeAgnaFromList(dailyFormRepo, agna)
    }

    private suspend fun removeAgnaFromList(dailyFormRepo: DailyFormRepo, agna: Agna) {
        dailyFormRepo.dailyFormList().forEach { form ->
            val dailyAgnasList: MutableList<DailyAgna> = form.dailyAgnas.toMutableList()
            dailyAgnasList.removeIf {
                it.id == agna.id
            }

            val dailyForm = DailyForm(
                id = form.id,
                dailyAgnas = dailyAgnasList.toList(),
                date = form.date
            )

            dailyFormRepo.upsertDailyForm(dailyForm)
        }
    }

}