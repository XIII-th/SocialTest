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
import com.xiiilab.socialtest.auth.AbstractAuthStrategy
import com.xiiilab.socialtest.auth.AuthServiceLocator
import com.xiiilab.socialtest.vm.GithubVmFactory
import com.xiiilab.socialtest.vm.UserEventsListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_LAST_QUERY = "com.xiiilab.socialtest.activity.MainActivity_LAST_QUERY"
    }

    private lateinit var mListVm: UserEventsListViewModel
    private lateinit var mAuthStrategy: AbstractAuthStrategy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListVm = ViewModelProviders.of(this, GithubVmFactory)[UserEventsListViewModel::class.java]

        val strategyKey = intent?.getStringExtra(LoginActivity.KEY_SELECTED_AUTH_STRATEGY) ?:
                throw IllegalStateException("Auth strategy key is absent")
        mAuthStrategy = AuthServiceLocator[strategyKey]

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mListVm.mQuery.value?.let { outState.putString(KEY_LAST_QUERY, it) }
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

    fun onLogout(menuItem: MenuItem?) {
        drawer_layout.closeDrawer(GravityCompat.START)
        mAuthStrategy.logout()
        finish()
    }
}
