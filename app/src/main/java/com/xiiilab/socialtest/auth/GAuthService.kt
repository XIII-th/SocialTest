package com.xiiilab.socialtest.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.reactivex.Maybe


/**
 * Created by XIII-th on 04.09.2018
 */
object GAuthService : AbstractAuthService() {

    private const val RC_SIGN_IN = 1
    private lateinit var mClient: GoogleSignInClient

    override fun init(appContext: Context) {
        super.init(appContext)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
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
            mAuthResult.onNext(when {
                task.isCanceled -> AuthResult.CANCEL
                task.isSuccessful -> AuthResult.SUCCESS
                else -> AuthResult.error { task?.exception?.message }
            })
        }
    }

    override fun logout() {
        super.logout()
        mClient.signOut()
    }

    override fun loadUserInfo(): UserInfo? = fromAccount { UserInfo(it.givenName, it.familyName) }

    override fun avatarUrl(): Maybe<String> = Maybe.fromCallable { fromAccount { it.photoUrl?.toString() } }

    override fun getServiceName(): String = "GOOGLE$SERVICE_NAME_SUFFIX"

    private fun <T> fromAccount(function: (GoogleSignInAccount) -> T?): T? {
        return GoogleSignIn.getLastSignedInAccount(mClient.applicationContext)?.run(function::invoke)
    }
}