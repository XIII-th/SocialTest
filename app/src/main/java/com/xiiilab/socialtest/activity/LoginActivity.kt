package com.xiiilab.socialtest.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.auth.AuthResult
import com.xiiilab.socialtest.auth.AuthState.*
import com.xiiilab.socialtest.auth.FbAuthService
import com.xiiilab.socialtest.auth.GAuthService
import com.xiiilab.socialtest.auth.VkAuthService
import com.xiiilab.socialtest.databinding.ActivityLoginBinding
import com.xiiilab.socialtest.vm.LoginViewModel


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AUTH_ACTIVITY"
        const val KEY_SELECTED_AUTH_STRATEGY = "com.xiiilab.socialtest.activity.LoginActivity_SELECTED_AUTH_STRATEGY"
    }

    private val mViewModel: LoginViewModel by lazy { ViewModelProviders.of(this)[LoginViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getAuthResult().observe(this, Observer { onAuthCompleted(it) })

        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
                .apply { loginVm = mViewModel }
                .setLifecycleOwner(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mViewModel.onAuthFlowResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun startAuthFlow(view: View) {
        mViewModel.authWith(this, when (view.id) {
            R.id.vk_sign_in_btn -> VkAuthService
            R.id.google_sign_in_button -> GAuthService
            R.id.fb_sign_in_button -> FbAuthService
            else -> throw IllegalStateException("Unexpected view")
        })
    }

    private fun onAuthCompleted(result: AuthResult?) {
        when (result?.state) {
            SUCCESS -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(KEY_SELECTED_AUTH_STRATEGY, mViewModel.getServiceName())
                startActivity(intent)
                // FIXME: think about better solution for automatically clearing view model state and subscription
                mViewModel.onCleared()
            }
            FAILED -> Toast.makeText(this, "Auth failed: ${result.error}", Toast.LENGTH_LONG).show()
            CANCEL -> Log.d(TAG, "User cancel auth flow")
        }
    }
}
