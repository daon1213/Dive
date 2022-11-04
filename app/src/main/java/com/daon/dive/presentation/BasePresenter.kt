package com.daon.dive.presentation

import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.cancel

interface BasePresenter {

    val scope: LifecycleCoroutineScope

    fun onViewCreated()

    fun onDestroyView()

    @CallSuper
    fun onDestroy() {
        scope.cancel()
    }
}