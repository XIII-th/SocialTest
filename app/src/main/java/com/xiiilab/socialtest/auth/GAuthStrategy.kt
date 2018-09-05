package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.xiiilab.socialtest.R


/**
 * Created by XIII-th on 04.09.2018
 */
object GAuthStrategy : AbstractAuthStrategy() {

    private const val RC_SIGN_IN = 1
    private lateinit var mClient: GoogleSignInClient

    override fun init(appContext: Context) {
        super.init(appContext)
        val requestToken = appContext.getString(R.string.google_access_token)
        val gso = GoogleSignInOptions.Builder().requestIdToken(requestToken).build()
        mClient = GoogleSignIn.getClient(appContext, gso)
    }

    override fun checkAuth(context: Context): Boolean {
        return super.checkAuth(context) || GoogleSignIn.getLastSignedInAccount(context) !== null
    }

    override fun startAuthFlow(activity: Activity) {
        super.startAuthFlow(activity)
        activity.startActivityForResult(mClient.signInIntent, RC_SIGN_IN)
    }

    override fun onAuthFlowResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onAuthFlowResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            mAuthResult.onNext(if (task.isSuccessful) AuthResult.SUCCESS else AuthResult.error {
                task?.exception?.message
            })
        }
    }

    override fun logout() {
        super.logout()
        mClient.signOut()
    }

    override fun loadUserInfo(): UserInfo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}