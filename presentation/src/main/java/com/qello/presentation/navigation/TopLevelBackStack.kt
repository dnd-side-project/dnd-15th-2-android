package com.qello.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

/**
 * 탭(하단바)마다 독립된 백스택을 유지하는 네비게이터
 */
class TopLevelBackStack<T : Any>(startKey: T) {

    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey),
    )

    var topLevelKey by mutableStateOf(startKey)
        private set

    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() = backStack.apply {
        clear()
        addAll(topLevelStacks.flatMap { it.value })
    }

    fun addTopLevel(key: T) {
        if (topLevelStacks[key] == null) {
            topLevelStacks[key] = mutableStateListOf(key)
        } else {
            topLevelStacks.apply { remove(key)?.let { put(key, it) } }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: T) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }

    fun popToTopLevelStart() {
        topLevelStacks[topLevelKey]?.let { stack ->
            if (stack.size > 1) {
                stack.subList(1, stack.size).clear()
                updateBackStack()
            }
        }
    }
}
