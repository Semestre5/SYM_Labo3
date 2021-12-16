package ch.heigvd.sym.labo3.nfc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ch.heigvd.sym.labo3.R
import android.util.Log
import android.widget.EditText
import android.widget.Toast

class LoginFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var pswd : EditText
    private lateinit var submit: Button
    private lateinit var cancel: Button

    private val TAG = "Login Fragment";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart(){
        super.onStart()
        // get reference to all views
        username = view?.findViewById(R.id.et_user_name)!!
        pswd = view?.findViewById(R.id.et_password)!!
        cancel = view?.findViewById(R.id.btn_reset)!!
        submit = view?.findViewById(R.id.btn_submit)!!

        cancel.setOnClickListener {
            // clearing user_name and password edit text views on reset button click
            username.setText("")
            pswd.setText("")
        }

        // set on-click listener
        submit.setOnClickListener {
            if((username.text.toString() == "hello") && (pswd.text.toString() == "world")) {
                (activity as NfcActivity).loggedIn()
            } else {
                username.setText("")
                pswd.setText("")
                Toast.makeText(this.context, "Username: hello, password: world)", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}

