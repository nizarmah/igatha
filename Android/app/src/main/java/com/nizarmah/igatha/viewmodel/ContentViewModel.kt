package com.nizarmah.igatha.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.nizarmah.igatha.model.Device
import com.nizarmah.igatha.service.ProximityScanner
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class ContentViewModel(app: Application) : AndroidViewModel(app) {
    private val _isSOSAvailable = MutableStateFlow(false)
    val isSOSAvailable: StateFlow<Boolean> = _isSOSAvailable.asStateFlow()

    private val _isSOSActive = MutableStateFlow(false)
    val isSOSActive: StateFlow<Boolean> = _isSOSActive.asStateFlow()

    private val _activeAlert = MutableStateFlow<AlertType?>(null)
    val activeAlert: StateFlow<AlertType?> = _activeAlert.asStateFlow()

    private val _devicesMap = MutableStateFlow<Map<String, Device>>(emptyMap())
    val devices: StateFlow<List<Device>> = _devicesMap.asStateFlow()
        .map { devicesMap ->
            devicesMap.values.sortedByDescending { it.rssi }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    private val proximityScanner = ProximityScanner(app)

    init {
        updateSOSAvailability()

        // Observe proximity scanner's scanned devices
        viewModelScope.launch {
            proximityScanner.scannedDevices.collect { device ->
                device?.let { updateDevice(it) }
            }
        }

        viewModelScope.launch {
            proximityScanner.isAvailable.collect { isAvailable ->
                if (!isAvailable) {
                    proximityScanner.stopScanning()
                } else {
                    proximityScanner.startScanning()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        proximityScanner.deinit()
    }

    private fun updateDevice(device: Device) {
        _devicesMap.update { currentMap ->
            val updatedMap = currentMap.toMutableMap()
            if (!updatedMap.containsKey(device.id)) {
                updatedMap[device.id] = device
            } else {
                updatedMap[device.id]?.update(device.rssi)
            }
            updatedMap
        }
    }

    fun startDetector() {
        // TODO: Add logic
    }

    fun stopDetector() {
        // TODO: Add logic
    }

    fun startSOS() {
        // TODO: Add logic
    }

    fun stopSOS() {
        // TODO: Add logic
    }

    private fun updateSOSAvailability(
        isAvailable: Boolean? = null,
        isActive: Boolean? = null
    ) {
        _isSOSAvailable.value = isAvailable == true
        _isSOSActive.value = isActive == true
    }
}

sealed class AlertType(val id: Int) {
    object SOSConfirmation : AlertType(1)
    object DisasterDetected : AlertType(2)
}
