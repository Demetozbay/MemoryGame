package com.r00t.demetapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.r00t.demetapp.R
import com.r00t.demetapp.activities.MainActivity
import com.r00t.demetapp.commons.AuthService
import com.r00t.demetapp.commons.CheckTexts
import com.r00t.demetapp.commons.DatabaseService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SignUpFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignUpFragment : Fragment() {
    protected var mainView: LinearLayout? = null
    protected var nameEditText: EditText? = null
    protected var emailEditText: EditText? = null
    protected var passwordEditText: EditText? = null
    protected var rePasswordEditText: EditText? = null
    protected var signUpButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v =  inflater.inflate(R.layout.fragment_sign_up, container, false)
        mainView = v.findViewById<LinearLayout>(R.id.mainView)
        nameEditText = v.findViewById<EditText>(R.id.nameEditText)
        emailEditText = v.findViewById<EditText>(R.id.emailEditText)
        passwordEditText = v.findViewById<EditText>(R.id.passwordEditText)
        rePasswordEditText = v.findViewById<EditText>(R.id.rePasswordEditText)
        signUpButton = v.findViewById<Button>(R.id.signUpButton)
        signUpButton?.setOnClickListener {
            onSignUpClicked(v)
        }
        return v
    }

    fun showMessage(view:View,text: String) {
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).show()
    }

    fun onSignUpClicked(view:View) {
        if(CheckTexts.isEmpty(nameEditText,emailEditText,passwordEditText,rePasswordEditText)) {
            showMessage(view,"Empty Area")
            return
        }

        if(!passwordEditText?.text.toString().trim().equals(rePasswordEditText?.text.toString().trim())) {
            showMessage(view,"Please enter same passwords in password blanks")
            return
        }

        AuthService
            .getAuth()
            .createUserWithEmailAndPassword(emailEditText?.text.toString().trim(),passwordEditText?.text.toString().trim())
            .addOnSuccessListener { authResult ->

                DatabaseService.updateDbForRegister(authResult.user!!.uid,nameEditText?.text.toString().trim())
                    .addOnSuccessListener {doc->
                        println("work success")

                    }
                    .addOnFailureListener { e ->
                        println("worked")
                        showMessage(view,e.message!!)
                    }
                (activity as MainActivity).changeFragment(SignInFragment())
            }
            .addOnFailureListener {e->
                println("worked2")
                showMessage(view,"Failed")
                println(e.message)
            }
    }
}
