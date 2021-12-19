/**
 * Authors: Robin GAUDIN, Lev POZNIAKOFF, Axel Vallon
 *
 * Date: 19.12.2021
 */

package ch.heigvd.sym.labo3.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import ch.heigvd.sym.labo3.R
import android.content.IntentFilter.MalformedMimeTypeException
import android.content.IntentFilter
import android.app.PendingIntent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.os.CountDownTimer

var loggedIn = false;

class NfcActivity:  AppCompatActivity() {

    val MIME_TEXT_PLAIN = "text/plain"
    val TAG = "Nfc Activity"

    val MAX_AUTH = 12
    val MED_AUTH = 8
    val LOW_AUTH = 4
    private val NFC_TAG_STR = "test"

    private var authenticationLevel = 0

    private lateinit var mNfcAdapter: NfcAdapter

    /**
     * Initialize the activity and verifies that nfc is activated
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter == null) {
            Toast.makeText(this, R.string.no_nfc, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        if (!mNfcAdapter.isEnabled)
            Toast.makeText(this, R.string.enable_nfc, Toast.LENGTH_LONG).show()
        if(savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(R.id.nfc_fragment_container,
                LoginFragment()).commit()
        setContentView(R.layout.activity_nfc)

    }

    override fun onResume() {
        super.onResume()
        setupForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        stopForegroundDispatch()

    }

    /**
     * manages the nfc tag data when it's detected by the app
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        processNfcTag(intent)
    }

    /**
     *  processes the data from the nfc tag
     * @param the intent containing the nfc tag data
     */
    private fun processNfcTag(intent: Intent?){
        if (intent != null) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                val record = deserializeNfcData(intent)?.get(0)
                if (record != null) {
                    val payload: String = String(record.payload)
                    if(payload.subSequence(3, payload.length) == NFC_TAG_STR) {
                        authenticationLevel = MAX_AUTH;
                        object: CountDownTimer(12000, 1000) {
                            override fun onTick(p0: Long) {
                                authenticationLevel -= 1
                            }
                            override fun onFinish() {
                                authenticationLevel = 0
                            }
                        }.start()
                    }
                }
            }
        }
    }

    private fun setupForegroundDispatch() {
        val intent = Intent(this.applicationContext, this.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(this.applicationContext, 0, intent, 0)
        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        // On souhaite être notifié uniquement pour les TAG au format NDEF
        filters[0] = IntentFilter()
        filters[0]!!.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]!!.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]!!.addDataType(MIME_TEXT_PLAIN)
        } catch (e: MalformedMimeTypeException) {
            Log.e(TAG, "MalformedMimeTypeException", e)
        }
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    private fun stopForegroundDispatch() {
        mNfcAdapter.disableForegroundDispatch(this)
    }

    /**
     * @param intent: the intent containing the NFC tag data
     * @return the message contained in the nfc tag
     */
    private fun deserializeNfcData(intent: Intent?): Array<out NdefRecord>? {
        val ndefMessagesArray = intent!!.getParcelableArrayExtra(
            NfcAdapter.EXTRA_NDEF_MESSAGES
        )
        if (ndefMessagesArray != null) {
            val parsedMessage: NdefMessage = ndefMessagesArray[0] as NdefMessage
            return parsedMessage.records
        }
        return null
    }

    /**
     * @brief gives the status loggedIn to the nfcActivity and launches the authenticated fragment
     */
    fun loggedIn() {
        loggedIn = true;
        supportFragmentManager.findFragmentById(R.id.nfc_fragment_container)?.let {
            supportFragmentManager.beginTransaction().remove(it).add(R.id.nfc_fragment_container,
                AuthenticatedFragment()
            ).commit()
        };
    }

    fun getAuthenticationLevel(): Int {
        return authenticationLevel
    }


}
