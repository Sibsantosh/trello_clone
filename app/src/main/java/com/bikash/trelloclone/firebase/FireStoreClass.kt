package com.bikash.trelloclone.firebase

import android.app.Activity
import android.util.Log
import com.bikash.trelloclone.activities.MainActivity
import com.bikash.trelloclone.activities.MyProfileActivity
import com.bikash.trelloclone.activities.SignInActivity
import com.bikash.trelloclone.activities.SignUpActivity
import com.bikash.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity,userInfo: com.bikash.trelloclone.models.User){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.userRegisteredSuccess()
        }.addOnFailureListener{
        Log.e(activity.javaClass.simpleName,"Error writing document")
        }

    }

    fun loadUserData(activity: Activity){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).get().addOnSuccessListener {
            document ->
            val loggedInUser = document.toObject(com.bikash.trelloclone.models.User::class.java)

            when(activity){
                is SignInActivity -> {
                    activity.signInSuccess(loggedInUser)
                }
                is MainActivity -> {
                    activity.updateNavigationUserDetails(loggedInUser)
                }

                is MyProfileActivity -> {
                    activity.setUserData(loggedInUser)
                }
            }


        }.addOnFailureListener{
            when(activity){
                is SignInActivity -> {
                    activity.hideProgressDialog()
                }
                is MainActivity -> {
                    activity.hideProgressDialog()
                }
            }
            Log.e(activity.javaClass.simpleName,"Error writing document")
        }

    }


     fun getCurrentUserId():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}