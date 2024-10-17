//
//  SettingsView.swift
//  Igatha
//
//  Created by Nizar Mahmoud on 14/10/2024.
//

import SwiftUI

struct SettingsView: View {
    @StateObject private var viewModel = SettingsViewModel()
    
    var body: some View {
        Form {
            Section {
                Toggle(isOn: $viewModel.disasterMonitoringEnabled) {
                    Text("Disaster Monitoring")
                    
                    Text("Detects disasters and sends SOS when the app is not in use. This may increase battery consumption.")
                        .font(.caption)
                        .foregroundColor(.gray)
                }
            } header: {
                Text("Background Services")
                    .padding(.vertical, 4)
            } footer: {
                Text("Services might require additional permissions.")
                    .padding(.vertical, 4)
            }
        }
        .navigationTitle("Settings")
    }
}

#Preview {
    SettingsView()
}
