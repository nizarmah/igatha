//
//  IncidentDetector.swift
//  Igatha
//
//  Created by Nizar Mahmoud on 12/10/2024.
//

import Foundation

class IncidentDetector: SensorDelegate {
    weak var delegate: IncidentDetectorDelegate?
    
    private let eventTimeWindow: TimeInterval
    private var eventTimes: [SensorType: Date] = [:]
    
    private let accelerometerSensor: AccelerometerSensor
    private let gyroscopeSensor: GyroscopeSensor
    private let barometerSensor: BarometerSensor
    
    public var isAvailable: Bool {
        return (
            accelerometerSensor.isAvailable &&
            gyroscopeSensor.isAvailable &&
            barometerSensor.isAvailable
        )
    }
    
    init(
        accelerationThreshold: Double,
        rotationThreshold: Double,
        pressureThreshold: Double,
        eventTimeWindow: TimeInterval
    ) {
        self.eventTimeWindow = eventTimeWindow
        
        accelerometerSensor = AccelerometerSensor(
            threshold: accelerationThreshold,
            updateInterval: 0.1
        )
        gyroscopeSensor = GyroscopeSensor(
            threshold: rotationThreshold,
            updateInterval: 0.1
        )
        barometerSensor = BarometerSensor(
            threshold: pressureThreshold
        )
        
        accelerometerSensor.delegate = self
        gyroscopeSensor.delegate = self
        barometerSensor.delegate = self
        
        delegate?.incidentDetectorAvailabilityUpdate(isAvailable)
    }
    
    deinit {
        stopDetection()
    }
    
    func startDetection() {
        guard isAvailable else { return }
        
        accelerometerSensor.startUpdates()
        gyroscopeSensor.startUpdates()
        barometerSensor.startUpdates()
        
        delegate?.incidentDetectionStarted()
    }
    
    func stopDetection() {
        accelerometerSensor.stopUpdates()
        gyroscopeSensor.stopUpdates()
        barometerSensor.stopUpdates()
        
        delegate?.incidentDetectionStopped()
    }
    
    // called when a sensor exceeds a threshold
    func sensorExceededThreshold(
        sensorType: SensorType,
        eventTime: Date
    ) {
        eventTimes[sensorType] = eventTime
        
        checkForIncident()
    }
    
    // called when an incident is suspected
    private func checkForIncident() {
        let currentTime = Date()
        
        // ensure all events have occurred
        guard eventTimes.count == 3 else { return }
        
        // ensure all events occurred within the time window
        for (_, eventTime) in eventTimes {
            if currentTime.timeIntervalSince(eventTime) > eventTimeWindow {
                // event is too old; do not consider it
                return
            }
        }
        
        // incident detected
        delegate?.incidentDetected()
    }
}

protocol IncidentDetectorDelegate: AnyObject {
    func incidentDetected()
    
    func incidentDetectionStarted()
    func incidentDetectionStopped()
    
    func incidentDetectorAvailabilityUpdate(_ isAvailable: Bool)
}