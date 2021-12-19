/**
 * Authors: Robin GAUDIN, Lev POZNIAKOFF, Axel Vallon
 *
 * Date: 19.12.2021
 */
package ch.heigvd.sym.labo3.nfc

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import ch.heigvd.sym.labo3.R

/**
 * A simple [Fragment] subclass.
 * Use the [AuthenticatedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthenticatedFragment : Fragment() {

    private lateinit var nfcAuthCountdown: TextView
    private lateinit var nfcAuthStatus: TextView
    private lateinit var maxBtn: Button
    private lateinit var medBtn: Button
    private lateinit var lowBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authenticated, container, false)
    }


    /**
     * Initialize the view with it's various buttons and text boxes
     */
    override fun onStart() {
        super.onStart()
        val act = activity as NfcActivity
        nfcAuthCountdown = view?.findViewById(R.id.auth_countdown)!!
        maxBtn = view?.findViewById(R.id.btn_max_level)!!
        maxBtn.setOnClickListener{
            showAuthenticationToast(act.MED_AUTH, act)
        }
        medBtn = view?.findViewById(R.id.btn_med_level)!!
        medBtn.setOnClickListener {
            showAuthenticationToast(act.LOW_AUTH, act)
        }
        lowBtn = view?.findViewById(R.id.btn_low_level)!!
        lowBtn.setOnClickListener {
            showAuthenticationToast(0, act)
        }
        nfcAuthCountdown.postDelayed(mUpdate, 0)
    }

    private val mUpdate: Runnable = object : Runnable {
        override fun run() {
            if (activity == null)
                return
            val act =  activity as NfcActivity
            updateAuthStatus(act)
            nfcAuthCountdown.setText("Countdown: " + act.getAuthenticationLevel().toString())
            nfcAuthCountdown.postDelayed(this, 1000);
        }
    }

    /**
     * updates the authentication status regarding the authentication
     * level
     */
    private fun updateAuthStatus(act: NfcActivity){
        val authLevel = act.getAuthenticationLevel()
        nfcAuthStatus = view?.findViewById(R.id.auth_status)!!
        if(act.MED_AUTH < authLevel){
            nfcAuthStatus.setText(R.string.max_auth_txt)
            nfcAuthStatus.setTextColor(Color.parseColor(R.color.green.toString()))
        } else if((act.LOW_AUTH < authLevel) and (authLevel <= act.MED_AUTH)){
            nfcAuthStatus.setText(R.string.med_auth_txt)
            nfcAuthStatus.setTextColor(Color.parseColor(R.color.elek_blue.toString()))
        } else if((0 < authLevel) and (authLevel <= act.LOW_AUTH)){
            nfcAuthStatus.setText(R.string.low_auth_txt)
            nfcAuthStatus.setTextColor(Color.parseColor(R.color.orange.toString()))
        } else {
            nfcAuthStatus.setText(R.string.auth_frag_status)
            nfcAuthStatus.setTextColor(Color.parseColor(R.color.red.toString()))
        }
    }

    private fun showAuthenticationToast(threshold: Int, act: NfcActivity){
        if(act.getAuthenticationLevel() > threshold){
            Toast.makeText(this.context, "Valid authentication", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Invalid authentication", Toast.LENGTH_SHORT).show()
        }
    }
}