package com.r00t.demetapp.commons

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

public class AuthService {
    companion object {
        private var auth:FirebaseAuth = FirebaseAuth.getInstance()

        fun getAuthStatus():Boolean {
            var user:FirebaseUser? = auth.currentUser
            if (user==null) {
                return false
            }

            return true
        }

        fun getCurrentUser():FirebaseUser? {
            return auth.currentUser
        }

        fun getAuth():FirebaseAuth {
            return auth
        }
    }
}