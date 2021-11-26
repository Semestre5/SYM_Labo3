package ch.heigvd.sym.labo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ch.heigvd.sym.labo3.barcode.BarCode

class MainActivity : AppCompatActivity() {
    private lateinit var barcode: Button
    private lateinit var nfc : Button
    private lateinit var beacon: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barcode = findViewById(R.id.button_barcode)
        beacon = findViewById(R.id.button_beacon)
        nfc = findViewById(R.id.button_nfc)
        barcode.setOnClickListener {
            val intent = Intent(this@MainActivity, BarCode::class.java)
            startActivity(intent)
        }
        beacon.setOnClickListener {
            //val intent = Intent(this@MainActivity, DeferredActivity::class.java)
            //startActivity(intent)
        }
        nfc.setOnClickListener {
            //val intent = Intent(this@MainActivity, DeferredActivity::class.java)
            //startActivity(intent)
        }
    }
}