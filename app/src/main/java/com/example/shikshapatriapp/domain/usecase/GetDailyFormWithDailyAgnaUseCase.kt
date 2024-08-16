package com.example.shikshapatriapp.domain.usecase

import com.example.shikshapatriapp.data.repository.AgnaRepo
import com.example.shikshapatriapp.data.repository.DailyFormRepo
import com.example.shikshapatriapp.domain.models.Agna
import com.example.shikshapatriapp.domain.models.DailyAgna
import com.example.shikshapatriapp.domain.models.DailyAgnaHelperClass
import com.example.shikshapatriapp.domain.models.DailyForm

class GetDailyFormWithDailyAgnaUseCase(
    private val dailyFormRepo: DailyFormRepo,
    private val agnaRepo: AgnaRepo
) {

    suspend operator fun invoke(id: Long): Pair<DailyForm, List<DailyAgnaHelperClass>> {
        val dailyAgnas = mutableListOf<DailyAgnaHelperClass?>()
        dailyFormRepo.getDailyFormWithDailyAgnaByDailyFormId(id).forEach { data ->
            data.dailyAgnas.map { dailyAgna ->
                val agna = agnaRepo.getAgnaById(dailyAgna.agnaId)
                dailyAgnas.add(agna?.toHelperClass(dailyAgna))
            }
        }
        return Pair(dailyFormRepo.getDailyFormById(id), dailyAgnas.filterNotNull())
    }

}

fun Agna?.toHelperClass(dailyAgna: DailyAgna): DailyAgnaHelperClass {
    return DailyAgnaHelperClass(
        agnaId = dailyAgna.agnaId,
        palai = dailyAgna.palai,
        agna = this?.agna.orEmpty(),
        description = this?.description.orEmpty(),
        author = this?.author.orEmpty(),
        rajipoPoints = this?.rajipoPoints ?: 0,
        alwaysPalayChe = this?.alwaysPalayChe ?: false,
        isCounter = this?.isCounter ?: false,
        count = dailyAgna.count,
        note = dailyAgna.note
    )
}