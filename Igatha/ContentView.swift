//
//  ContentView.swift
//  Igatha
//
//  Created by Nizar Mahmoud on 05/10/2024.
//

import SwiftUI

struct ContentView: View {
    @State private var bluetoothManager = BluetoothManager()
    
    var body: some View {
        Text("Hello, world!")
            .padding()
    }
}

#Preview {
    ContentView()
}
