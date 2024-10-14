//
//  DeviceListView.swift
//  Igatha
//
//  Created by Nizar Mahmoud on 13/10/2024.
//

import SwiftUI

struct DeviceListView: View {
    let devices: [Device]
    
    var onDeviceSelect: (Device) -> Void
    
    var body: some View {
        List {
            Section {
                if devices.isEmpty {
                    Text(
                        "No devices found nearby."
                    )
                    .foregroundColor(.gray)
                    .padding()
                } else {
                    ForEach(devices) { device in
                        DeviceRowView(device: device)
                            .onTapGesture {
                                // trigger the closure when tapped
                                onDeviceSelect(device)
                            }
                    }
                }
            } header: {
                Text("People Seeking Help")
                    .padding(.vertical)
            } footer: {
                Text("Note: Distance is approximate and varies due to signal fluctuations. It is for general guidance only.")
                    .padding(.vertical)
            }
        }
        .listStyle(.automatic)
    }
}

struct DeviceListView_Previews: PreviewProvider {
    static var previews: some View {
        // Creating mock devices
        let mockDevices = [
            Device(
                id: UUID(),
                rssi: -40
            ),
            Device(
                id: UUID(),
                rssi: -60
            ),
            Device(
                id: UUID(),
                rssi: -75
            ),
            Device(
                id: UUID(),
                rssi: -85
            )
        ]
        
        return DeviceListView(
            devices: mockDevices,
            onDeviceSelect: {_ in }
        )
    }
}
