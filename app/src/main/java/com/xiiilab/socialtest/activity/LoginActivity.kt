package com.xiiilab.socialtest.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.auth.*
import com.xiiilab.socialtest.auth.AuthState.FAILED
import com.xiiilab.socialtest.auth.AuthState.SUCCESS
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var mAuthStrategy: AbstractAuthStrategy
    private val mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDisposables.add(Observable.just(VkAuthStrategy, FbAuthStrategy, GAuthStrategy).
                subscribeOn(Schedulers.io()).
                map { strategy -> strategy.checkAuth(this) }.
                observeOn(AndroidSchedulers.mainThread()).
                skipWhile { !it }.
                subscribe { _ -> onAuthCompleted(AuthResult.SUCCESS) })

        setContentView(R.layout.activity_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let { mAuthStrategy.onAuthFlowResult(requestCode, resultCode, it) }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        mDisposables.clear()
    }

    fun startAuthFlow(view: View) {
        mAuthStrategy = when (view.id) {
            R.id.vk_sign_in_btn -> VkAuthStrategy
            R.id.google_sign_in_button -> GAuthStrategy
            R.id.fb_sign_in_button -> FbAuthStrategy
            else -> throw IllegalStateException("Unexpected view")
        }
        mDisposables.add(mAuthStrategy.subscribe(this::onAuthCompleted))
        mAuthStrategy.startAuthFlow(this)
    }

    private fun onAuthCompleted(result: AuthResult) {
        when (result.state) {
            SUCCESS -> startActivity(Intent(this, MainActivity::class.java))
            FAILED -> Toast.makeText(this, "Auth failed: ${result.error}", Toast.LENGTH_LONG).show()
        }
    }
}
