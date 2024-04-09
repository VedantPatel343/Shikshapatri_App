package com.swaminarayan.shikshapatriApp.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

fun launchFlow(block: suspend FlowCollector<Unit>.() -> Unit): Flow<Unit> = flow {
    emit(Unit)
    block()
}