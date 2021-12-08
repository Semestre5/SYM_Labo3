package ch.heigvd.sym.labo3.beacon

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.heigvd.sym.labo3.R
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

private const val BEACON_FORMAT: String = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
private const val INTERVAL: Long = 1000

class Beacon : AppCompatActivity() {

    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

        val REQUEST_ENABLE_BT = 1
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        // Test if the Bluetooth is supported on the device
        if (bluetoothAdapter != null) {
            // Check if Bluetooth is activated, and ask the user to activate it if not
            if (!bluetoothAdapter.isEnabled) {
                val toast = Toast.makeText(applicationContext, "Please enable the Bluetooth", Toast.LENGTH_LONG)
                toast.show()
            }
        } else {
            finish()
        }

        // Ask for permission to use localisation if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        // Initialize BeaconManager
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_FORMAT))

        // Set scan interval
        beaconManager.foregroundBetweenScanPeriod = INTERVAL
        beaconManager.updateScanPeriods()
    }
}