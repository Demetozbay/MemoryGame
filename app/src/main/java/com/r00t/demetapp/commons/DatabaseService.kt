package com.r00t.demetapp.commons

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.core.OrderBy
import com.r00t.demetapp.models.ScoreModel
import com.r00t.demetapp.models.UserDataModel

class DatabaseService {
    companion object {
        private var store = FirebaseFirestore.getInstance()

        fun getStore():FirebaseFirestore {
            return store
        }

        fun updateDbForRegister(userId:String,fullName:String):Task<Void> {
            println("userId:"+userId)
            println(fullName)
            var userDataModel = UserDataModel()
            userDataModel.fullName=fullName
            userDataModel.history= arrayListOf<ScoreModel>()
            userDataModel.highScore = ScoreModel()
            return store
                .collection("users")
                .document(userId)
                .set(userDataModel)
        }
        fun updateDb(data:UserDataModel?):Task<Void> {
            var userDataModel = UserDataModel()
            userDataModel.fullName=data?.fullName
            userDataModel.history=data?.history
            userDataModel.highScore = data?.highScore
            return store
                .collection("users")
                .document(AuthService.getCurrentUser()!!.uid)
                .set(userDataModel)
        }

        fun getCurrentUserData():Task<DocumentSnapshot> {
            return store
                .collection("users")
                .document(AuthService.getCurrentUser()!!.uid)
                .get()

        }

        fun getLeaders():Task<QuerySnapshot> {
            return store
                .collection("users")
                .orderBy("highScore",Query.Direction.DESCENDING)
                .limit(10)
                .get()
        }
    }
}