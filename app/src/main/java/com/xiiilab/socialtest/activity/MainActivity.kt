package com.xiiilab.socialtest.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.xiiilab.socialtest.R
import com.xiiilab.socialtest.auth.AbstractAuthService
import com.xiiilab.socialtest.auth.AuthServiceLocator
import com.xiiilab.socialtest.databinding.NavHeaderMainBinding
import com.xiiilab.socialtest.vm.GithubVmFactory
import com.xiiilab.socialtest.vm.UserEventsListViewModel
import com.xiiilab.socialtest.vm.UserInfoViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_LAST_QUERY = "com.xiiilab.socialtest.activity.MainActivity_LAST_QUERY"
    }

    private lateinit var mListVm: UserEventsListViewModel
    private lateinit var mAuthService: AbstractAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListVm = ViewModelProviders.of(this, GithubVmFactory)[UserEventsListViewModel::class.java]

        mAuthService = getAuthStrategy(intent?.extras)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val binding = NavHeaderMainBinding.inflate(layoutInflater)
                .apply { setLifecycleOwner(this@MainActivity) }
        ViewModelProviders.of(this)[UserInfoViewModel::class.java]
                .apply { init(mAuthService) }
                .also { binding.userInfoVm = it }
        nav_view.addHeaderView(binding.root)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            onLogout(null)
            super.onBackPressed()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_LAST_QUERY)?.let { mListVm.mQuery.onNext(it) }
        mAuthService = getAuthStrategy(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mListVm.mQuery.value?.let { outState.putString(KEY_LAST_QUERY, it) }
        outState.putString(LoginActivity.KEY_SELECTED_AUTH_STRATEGY, mAuthService.getServiceName())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchMenuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mListVm.mQuery.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { mListVm.mQuery.onNext(it) }
                return true
            }
        })
        mListVm.mQuery.value?.takeIf(String::isNotEmpty)?.let {
            // restore search view state https://stackoverflow.com/a/37305921/3926506
            searchMenuItem.expandActionView()
            searchView.setQuery(it, false)
        }
        return true
    }

    fun onLogout(@Suppress("UNUSED_PARAMETER") menuItem: MenuItem?) {
        drawer_layout.closeDrawer(GravityCompat.START)
        mAuthService.logout()
        finish()
    }

    private fun getAuthStrategy(bundle: Bundle?): AbstractAuthService {
        val strategyKey = bundle?.getString(LoginActivity.KEY_SELECTED_AUTH_STRATEGY)
                ?: throw IllegalStateException("Auth strategy key is absent")
        return AuthServiceLocator[strategyKey]
    }
}
