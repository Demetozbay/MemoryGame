package com.r00t.demetapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.r00t.demetapp.R
import com.r00t.demetapp.activities.MainActivity
import com.r00t.demetapp.adapters.GridRecyclerViewAdapter
import com.r00t.demetapp.models.DataModel
import com.r00t.demetapp.models.LevelModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [GameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GameFragment : Fragment() {

    protected var mainView:LinearLayout?=null
    protected var gridRecyclerView:RecyclerView?=null
    protected var gridRecyclerViewAdapter:GridRecyclerViewAdapter? = null
    protected var dataModelList:ArrayList<DataModel> = arrayListOf()
    var remainingSeconds:Long = 60
    var duration:Long = 60
    var timer:GameTimer? = null
    var clickedLast:DataModel?=null
    var score:Long = 0
    var levelList:ArrayList<LevelModel> = arrayListOf(LevelModel(6,2),
        LevelModel(9,3), LevelModel
    (12,3), LevelModel
    (16,4)
    )
    var currentLevel=0

    enum class TimerState {
        Stopped,Paused,Resuming,Started
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var v = inflater.inflate(R.layout.fragment_game, container, false)
        //inits
        mainView=v.findViewById(R.id.mainView)
        gridRecyclerView=v.findViewById(R.id.gridRecyclerView)
        gridRecyclerViewAdapter= GridRecyclerViewAdapter(dataModelList)
        changeLevel(levelList[currentLevel].boxesCount,levelList[currentLevel].columnCount)
        gridRecyclerViewAdapter?.itemClickListener=ItemClick()
        timer=GameTimer(duration*1000,1000)
        return v
    }

    fun point() : Int {
        return (currentLevel+1)*3
    }

    inner class GameTimer : CountDownTimer {
        var state:TimerState=TimerState.Stopped
        constructor(m:Long,sec:Long) : super(m,sec) {
            remainingSeconds=duration
        }

        override fun onFinish() {
            activity?.setTitle("Time is up")
            dataModelList.forEach { data ->
                data.isEnabled=false
                data.isShowing=true
            }
            dataModelList.get(0).isEnabled=true
            gridRecyclerViewAdapter?.notifyDataSetChanged()
        }

        override fun onTick(p0: Long) {
            remainingSeconds--
            activity?.setTitle(" Remaining Seconds : " + remainingSeconds + "\tScore:"+score)
        }
    }

    fun changeLevel(boxCount:Int,columnCount:Int) {
        //Layout things
        gridRecyclerView?.layoutManager = GridLayoutManager(this.context,columnCount)
        gridRecyclerView?.adapter = gridRecyclerViewAdapter
        fillList(boxCount)
        //Set Title To Active Activity To Show Current Level
        activity?.setTitle(" Remaining Seconds : " + remainingSeconds + "\tScore:"+score)
        gridRecyclerViewAdapter?.notifyDataSetChanged()
    }

    fun fillList(boxCount: Int) {
        var i=0
        dataModelList.clear()
        while(i<boxCount) {
            var dataModel = DataModel()
            dataModel.id="id:"+i
            dataModel.tempData = if(boxCount%2==0) i%(boxCount/2) else i%((boxCount+1)/2)
            dataModelList.add(dataModel)
            i++
        }
        dataModelList.shuffle()
    }

    fun resetTimer() {
        timer?.cancel()
        timer = GameTimer(duration*1000,1000)
    }

    fun restart() {
        currentLevel=0
        score=0
        resetTimer()
        changeLevel(levelList.get(currentLevel).boxesCount,levelList.get(currentLevel).columnCount)
        activity?.setTitle(" Remaining Seconds : " + remainingSeconds + "\tScore:"+score)
        gridRecyclerViewAdapter?.notifyDataSetChanged()
    }

    inner class ItemClick : GridRecyclerViewAdapter.GridRecyclerViewItemClickListener {
        override fun onItemClicked(view: View, position: Int, data: DataModel) {
            if(activity?.title!!.equals("Time is up")) {
                restart()
                return
            }
            if(!(timer?.state==TimerState.Started)) {
                timer?.start()
                timer?.state=TimerState.Started
            }
            if(clickedLast==null) {
                data.isShowing=true
                data.isEnabled=false
                clickedLast=data
            }
            else {
                data.isShowing=true
                dataModelList.forEach {tempData ->
                    tempData.isEnabled=false
                }
                Handler().postDelayed({
                    var isLevelUp=true
                    if(currentLevel<=3 && clickedLast?.tempData==data.tempData && !(clickedLast==data)) {
                        data.isMatch=true
                        clickedLast?.isMatch=true
                        score+=point()
                    }
                    /*else if (currentLevel==3 && clickedLast?.tempData==data.tempData && !(clickedLast==data)) {
                        restart()
                        (activity as MainActivity).changeFragment(MainFragment())
                    }*/
                    dataModelList.forEach {tempData ->
                        tempData.isEnabled=true
                    }
                    clickedLast?.isShowing=false
                    data.isShowing=false
                    clickedLast=null
                    gridRecyclerViewAdapter?.notifyDataSetChanged()
                    dataModelList.forEach { data ->
                        if(!data.isMatch && data.tempData!! < ((dataModelList.size/2))) {
                            isLevelUp=false
                        }
                    }
                    if(isLevelUp) {
                        score += remainingSeconds*3
                        if(currentLevel==3) {
                            (activity as MainActivity).changeFragment(EndGameFragment(score,duration))
                            restart()
                            activity?.setTitle("Congratz!")
                        }
                        else {
                            currentLevel++
                            resetTimer()
                            changeLevel(levelList[currentLevel].boxesCount,levelList[currentLevel].columnCount)
                        }
                    }

                },1000)

            }
            gridRecyclerViewAdapter?.notifyDataSetChanged()
        }
    }
}
