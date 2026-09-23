package com.qello.presentation.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(val state: NavigationState) {

    /**
     * - 현재 top-level 키 → 그 스택의 첫 화면으로 돌아간다
     * - 다른 top-level 키 → 그 스택으로 전환한다 (탭 이동)
     * - 그 외 → 현재 스택 위에 쌓는다
     */
    fun navigate(key: NavKey) {
        when (key) {
            state.currentTopLevelKey -> popToTopLevelStart()
            in state.topLevelKeys -> goToTopLevel(key)
            else -> goToKey(key)
        }
    }

    /** 현재 스택에서 하나 빼고, 스택의 첫 화면이면 이전 top-level 스택으로 돌아간다. */
    fun goBack() {
        when {
            state.currentSubStack.size > 1 -> state.currentSubStack.removeLastOrNull()
            state.topLevelStack.size > 1 -> state.topLevelStack.removeLastOrNull()
        }
    }

    /**
     * 흐름을 통째로 바꾼다. (스플래시 → 로그인/메인, 로그인 완료 → 메인)
     * 모든 스택을 첫 화면만 남기고 비워서, 이전 흐름의 화면과 ViewModel이 남지 않게 한다.
     */
    fun resetTo(topLevelKey: NavKey) {
        require(topLevelKey in state.topLevelKeys) { "$topLevelKey is not a top level key" }

        state.subStacks.forEach { (key, stack) ->
            stack.clear()
            stack.add(key)
        }
        state.topLevelStack.apply {
            clear()
            add(topLevelKey)
        }
    }

    /** 현재 스택을 [key] 하나로 바꾼다. 이전 화면으로 돌아가면 안 될 때 쓴다. (가입 완료 → Welcome) */
    fun replaceCurrentStack(key: NavKey) {
        state.currentSubStack.apply {
            clear()
            add(key)
        }
    }

    /** 현재 스택의 첫 화면만 남긴다. */
    fun popToTopLevelStart() {
        state.currentSubStack.run {
            if (size > 1) subList(1, size).clear()
        }
    }

    private fun goToKey(key: NavKey) {
        state.currentSubStack.apply {
            remove(key)
            add(key)
        }
    }

    private fun goToTopLevel(key: NavKey) {
        state.topLevelStack.apply {
            if (size > 1) subList(1, size).clear()
            if (key != first()) add(key)
        }
    }
}
