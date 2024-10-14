//
//  Constants.swift
//  Igatha
//
//  Created by Nizar Mahmoud on 06/10/2024.
//

import CoreBluetooth

struct Constants {
    static let SOSBeaconServiceID: CBUUID = CBUUID(string: "1802")
    
    // threshold for sudden changes in linear acceleration
    // 3.0 g ~= dropping your phone on a hard surface
    static let SensorAccelerationThreshold: Double = 3.0
    
    // threshold for sudden changes in rotation
    // 6.0 r/s ~= almost a full rotation in 1 second
    static let SensorRotationThreshold: Double = 6.0
    
    // threshold for sudden changes in atmospheric pressure
    // 0.1 kPa ~= altitude change of approx. 8 to 12 meters
    static let SensorPressureThreshold: Double = 0.1
    
    // time window for temporally correlating sensor readings
    // if all thresholds exceed in 1.5s then we have an incident
    static let IncidentTemporalCorrelationTimeWindow: Double = 1.5
    
    // grace period (seconds) before an incident response is triggered
    static let IncidentResponseGracePeriod: Double = 120.0
    
    // percentage of how much a new value should affect the old value
    static let RSSIExponentialMovingAverageSmoothingFactor: Double = 0.18
}
