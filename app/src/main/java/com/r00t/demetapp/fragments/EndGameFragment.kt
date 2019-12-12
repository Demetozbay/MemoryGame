package com.r00t.demetapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.r00t.demetapp.R
import com.r00t.demetapp.activities.MainActivity
import com.r00t.demetapp.commons.CurrentUserInfos
import com.r00t.demetapp.commons.DatabaseService
import com.r00t.demetapp.models.ScoreModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class EndGameFragment constructor() : Fragment() {

    var scoreTextView: TextView?=null
    var highScoreTextView: TextView?=null
    var restartButton:Button?=null
    var homePageButton:Button?=null
    var score:Long = 0
    var duration:Long = 0

    constructor(score:Long,duration:Long) : this() {
        this.score=score
        this.duration=duration
    }

    fun getDateTime() : String {
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        var date = simpleDateFormat.format(Date())
        return date
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v = inflater.inflate(R.layout.fragment_end_game, container, false)
        scoreTextView=v.findViewById(R.id.scoreTextView)
        highScoreTextView=v.findViewById(R.id.highScoreTextView)
        restartButton=v.findViewById(R.id.restartGameButton)
        homePageButton=v.findViewById(R.id.gotoMainViewButton)
        restartButton?.setOnClickListener {
            (activity as MainActivity).changeFragment(GameFragment())
            activity?.setTitle(" Remaining Seconds : " + duration + "\tScore:"+0)
        }
        homePageButton?.setOnClickListener {
            (activity as MainActivity).changeFragment(MainFragment())
            activity?.setTitle("DemetApp")
        }
        scoreTextView?.setText("Score : "+score)

        var currentUserInfos = CurrentUserInfos.data
        if (currentUserInfos?.highScore?.score!=null) {
            highScoreTextView?.setText("High Score : "+currentUserInfos?.highScore?.score)
        }
        else {
            highScoreTextView?.setText("High Score : "+score)
        }

        var scoreModel = ScoreModel()
        scoreModel.date=getDateTime()
        scoreModel.score=score
        CurrentUserInfos.data?.history?.add(scoreModel)
        DatabaseService.updateDb(CurrentUserInfos.data)
        if (currentUserInfos?.highScore?.score==null) {
            currentUserInfos?.highScore = currentUserInfos?.history?.last()
            DatabaseService.updateDb(currentUserInfos)
            return v
        }
        if(currentUserInfos?.highScore?.score!! < score) {
            if(currentUserInfos.history?.size!!>0) {
                currentUserInfos?.highScore = currentUserInfos?.history?.last()
                DatabaseService.updateDb(currentUserInfos)
            }
        }
        return v
    }

}
