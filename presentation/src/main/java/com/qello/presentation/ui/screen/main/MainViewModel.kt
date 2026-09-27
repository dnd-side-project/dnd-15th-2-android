package com.qello.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.DirectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val directionRepository: DirectionRepository,
) : ViewModel() {
    private var hasUpdatedPresence = false

    fun onLocationObtained(latitude: Double, longitude: Double, accuracyMeters: Double) {
        if (hasUpdatedPresence) return
        hasUpdatedPresence = true

        viewModelScope.launch {
            directionRepository.updatePresence(
                latitude = latitude,
                longitude = longitude,
                accuracyMeters = accuracyMeters,
            )
        }
    }
}
