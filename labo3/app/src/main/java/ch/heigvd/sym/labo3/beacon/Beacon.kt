package ch.heigvd.sym.labo3.beacon

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.heigvd.sym.labo3.R
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region
import androidx.lifecycle.Observer
import org.altbeacon.beacon.service.BeaconService.TAG

private const val BEACON_FORMAT: String = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

class Beacon : AppCompatActivity() {

    private lateinit var beaconManager: BeaconManager
    private lateinit var beaconAdapter: BeaconAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beacon)

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

        // Attach the recycler view, and the adapter to it
        val recyclerView: RecyclerView = findViewById(R.id.beacon_recycler_view)
        beaconAdapter = BeaconAdapter()
        recyclerView.adapter = beaconAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize BeaconManager
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_FORMAT))

        // Set scan interval
        beaconManager.foregroundBetweenScanPeriod = 1000
        beaconManager.updateScanPeriods()

        // Begin the scan
        val region = Region("all-beacons-region", null, null, null)
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(region)
        regionViewModel.rangedBeacons.observeForever( centralRangingObserver)
        beaconManager.startRangingBeacons(region)
    }

    val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->
        beaconManager.removeAllRangeNotifiers()
        beaconManager.addRangeNotifier { beacons, region ->

            // Update RecyclerView UI
            beaconAdapter.setBeacons(beacons)
            beaconAdapter.notifyDataSetChanged()
        }
    }
}