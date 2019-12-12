package com.r00t.demetapp.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.util.Logger
import com.r00t.demetapp.R
import com.r00t.demetapp.activities.MainActivity
import com.r00t.demetapp.commons.AuthService
import com.r00t.demetapp.commons.CheckTexts
import kotlinx.android.synthetic.main.activity_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignInFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignInFragment : Fragment() {

    private var mainView:LinearLayout? = null
    private var emailEditText:EditText? = null
    private var passwordEditText:EditText? = null
    private var signInButton:Button? = null
    private var registerTextView:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun onSignClicked(view:View) {
        if(!CheckTexts.isEmpty(emailEditText,passwordEditText)) {
            AuthService.getAuth()
                .signInWithEmailAndPassword(emailEditText?.text.toString()
                    ,passwordEditText?.text.toString()).addOnSuccessListener {
                    (activity as MainActivity).changeFragment(MainFragment())
                }.addOnFailureListener {
                    Snackbar
                        .make(view,
                            "Empty Area",
                            Snackbar.LENGTH_SHORT)
                        .show()
                }
        }
    }

    fun onRegisterClicked() {
        (activity as MainActivity).changeFragment(SignUpFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_sign_in, container, false)
        mainView = v.findViewById(R.id.mainView)
        emailEditText = v.findViewById(R.id.emailEditText)
        passwordEditText = v.findViewById(R.id.passwordEditText)
        signInButton = v.findViewById(R.id.signInButton)
        registerTextView = v.findViewById(R.id.registerTextView)
        signInButton?.setOnClickListener {
            onSignClicked(v)
        }
        registerTextView?.setOnClickListener {
            onRegisterClicked()
        }

        return v
    }
}
