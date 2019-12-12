package com.r00t.demetapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.r00t.demetapp.R
import com.r00t.demetapp.activities.MainActivity
import com.r00t.demetapp.commons.AuthService
import com.r00t.demetapp.commons.CurrentUserInfos
import com.r00t.demetapp.commons.DatabaseService
import com.r00t.demetapp.models.ScoreModel
import com.r00t.demetapp.models.UserDataModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {

    protected var mainView: ConstraintLayout? = null
    protected var fullNameTextView: TextView? = null
    protected var highScoreTextView: TextView? = null
    protected var signOutButton: Button? = null
    protected var playGameButton: Button? = null
    protected var leaderButton:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_main, container, false)
        mainView = v.findViewById(R.id.mainView)
        fullNameTextView = v.findViewById<TextView>(R.id.fullNameTextView)
        highScoreTextView = v.findViewById<TextView>(R.id.highScoreTextView)
        signOutButton = v.findViewById<Button>(R.id.signOutButton)
        playGameButton = v.findViewById(R.id.playGameButton)
        leaderButton=v.findViewById(R.id.leadersButton)
        playGameButton?.setOnClickListener {
            (activity as MainActivity).changeFragment(GameFragment())
        }
        signOutButton?.setOnClickListener {
            onSignOutClicked()
        }
        leaderButton?.setOnClickListener {
            (activity as MainActivity).changeFragment(LeadersFragment())
        }
        DatabaseService
            .getCurrentUserData()
            .addOnSuccessListener {documentSnapshot ->
                CurrentUserInfos.data = UserDataModel()
                CurrentUserInfos.data = documentSnapshot.toObject(UserDataModel::class.java)
                fullNameTextView?.setText(CurrentUserInfos.data?.fullName)
                highScoreTextView?.setText("High Score : "+CurrentUserInfos.data?.highScore?.score)
            }
            .addOnFailureListener { e->
                Snackbar
                    .make(v,e.message.toString(),Snackbar.LENGTH_SHORT).show()
            }

        return v
    }

    fun onSignOutClicked() {
        AuthService.getAuth().signOut()
        (activity as MainActivity).changeFragment(SignInFragment())
    }
}
