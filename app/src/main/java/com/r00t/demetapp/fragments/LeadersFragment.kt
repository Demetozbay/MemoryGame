package com.r00t.demetapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.r00t.demetapp.R
import com.r00t.demetapp.activities.MainActivity
import com.r00t.demetapp.commons.DatabaseService
import com.r00t.demetapp.interfaces.IOnBackPressed

import com.r00t.demetapp.models.UserDataModel


class LeadersFragment : Fragment(),IOnBackPressed {

    // TODO: Customize parameters
    private var columnCount = 1
    private var listOfUsers = arrayListOf<UserDataModel>()
    var adapter:MyLeaderRecyclerViewAdapter? = null
    var recyclerView:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leader_list, container, false)
        DatabaseService.getLeaders()
            .addOnSuccessListener { querySnapshot ->
                listOfUsers.addAll(querySnapshot.toObjects(UserDataModel::class.java))
                println(querySnapshot.toObjects(UserDataModel::class.java).size)
                println("data changed")
                println(listOfUsers.size)
                adapter?.notifyDataSetChanged()
            }
        adapter = MyLeaderRecyclerViewAdapter(listOfUsers, ClickClass())
        recyclerView = view.findViewById(R.id.list)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter

        return view
    }

    inner class ClickClass:OnListFragmentInteractionListener {
        override fun onListFragmentInteraction(item: UserDataModel?) {

        }
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: UserDataModel?)
    }

    override fun onBackPressed(): Boolean {
        (activity as MainActivity).changeFragment(MainFragment())
        return false
    }
}
