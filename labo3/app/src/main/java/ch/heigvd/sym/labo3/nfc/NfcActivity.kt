package ch.heigvd.sym.labo3.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.View
import android.widget.Toast
import ch.heigvd.sym.labo3.R
import android.content.IntentFilter.MalformedMimeTypeException
import android.content.IntentFilter
import android.app.PendingIntent
import android.nfc.NdefMessage

class NfcActivity:  AppCompatActivity()  {

    val MIME_TEXT_PLAIN = "text/plain"
    public final val TAG = "Nfc Activity";

    private lateinit var mTextView: TextView;
    private lateinit var mTagDataTextView: TextView;
    private lateinit var mNfcAdapter: NfcAdapter;

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc)
        mTextView = findViewById<View>(R.id.textView_explanation) as TextView
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.")
        } else {
            mTextView.setText(R.string.explanation)
        }
    }


    override fun onResume() {
        super.onResume()
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch()
    }
    override fun onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch()
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "hello")


        if (intent != null) {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                val ndefMessagesArray = intent!!.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES
                )
                if (ndefMessagesArray != null){
                    for (message in ndefMessagesArray) {
                        val parsedMessage: NdefMessage = message as NdefMessage
                        val records = parsedMessage.records
                        if(records != null) {
                            for(record in records){
                                val payload: String = String(record.payload)
                                mTextView.setText(payload.subSequence(3, payload.length))
                                Log.d(TAG, "payload: ${payload.subSequence(3, payload.length)}")
                            }
                        }
                    }
                }
                /*
                val ndefMessage = ndefMessageArray!![0] as NdefMessage
                val msg = String(ndefMessage.records[0].payload)
                Log.d(TAG, msg);

                 */
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
            filters[0]!!.addDataType("text/plain")
        } catch (e: MalformedMimeTypeException) {
            Log.e(TAG, "MalformedMimeTypeException", e)
        }
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, filters, techList)
    }

    private fun stopForegroundDispatch() {
        mNfcAdapter.disableForegroundDispatch(this)
    }


}
