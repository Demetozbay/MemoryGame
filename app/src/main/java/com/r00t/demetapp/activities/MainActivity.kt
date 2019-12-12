package com.r00t.demetapp.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.r00t.demetapp.R
import com.r00t.demetapp.commons.AuthService
import com.r00t.demetapp.fragments.*
import com.r00t.demetapp.interfaces.IOnBackPressed

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //splash screen preparations
        //lets hide the view of system ui
        window.decorView.apply {
            systemUiVisibility=View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        //change content to splash screen
        setContentView(R.layout.fragment_start)
        //hide actionBar to get fullscreen view
        supportActionBar?.hide()
        //splash screen end

        //here is the postdelayed view changer it will be invoke after 3 sec and main view gets activity_main layout
        Handler().postDelayed({
            setContentView(R.layout.activity_main)
            supportActionBar?.show()
            window.decorView.apply {
                systemUiVisibility=View.SYSTEM_UI_FLAG_VISIBLE
            }
            if(AuthService.getAuthStatus()) {
                changeFragment(MainFragment())
            }
            else {
                changeFragment(SignInFragment())
            }

        },3000)

        //Firebase things for init (date fix)
        /*var firestore = FirebaseFirestore.getInstance();
    var settings = FirebaseFirestoreSettings.Builder()
        .setTimestampsInSnapshotsEnabled(true)
        .build();
    firestore.setFirestoreSettings(settings);*/
    }

    public fun changeFragment(fragment:Fragment) {
        val fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout,fragment);
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val fragment =
            this.supportFragmentManager.findFragmentById(R.id.fragment_layout)
        (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {

        }
    }
}
