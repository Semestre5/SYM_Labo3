package ch.heigvd.sym.labo3.beacon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.heigvd.sym.labo3.R
import android.bluetooth.BluetoothAdapter
import android.content.Intent


class Beacon : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

        // Chack if Bluetooth is activated, and ask the user to activate it if not
        val REQUEST_ENABLE_BT = 1
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        // Test if the Bluetooth is supported on the device
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }
        }

        
    }
}