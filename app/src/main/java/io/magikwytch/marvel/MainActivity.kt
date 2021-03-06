package io.magikwytch.marvel

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.magikwytch.marvel.fragment.CharacterFragment
import io.magikwytch.marvel.fragment.ComicFragment
import io.magikwytch.marvel.fragment.FavoriteFragment
import io.magikwytch.marvel.network.MarvelApi
import io.magikwytch.marvel.network.dto.character.CharacterDataWrapper
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    CharacterFragment.OnFragmentInteractionListener,
    ComicFragment.OnFragmentInteractionListener,
    FavoriteFragment.OnFragmentInteractionListener {

    lateinit var characterFragment: CharacterFragment
    lateinit var comicFragment: ComicFragment
    lateinit var favoriteFragment: FavoriteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        characterFragment = CharacterFragment.newInstance()
        comicFragment = ComicFragment.newInstance()
        favoriteFragment = FavoriteFragment.newInstance()


        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, characterFragment)
            .addToBackStack(characterFragment.toString())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_character -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, characterFragment)
                    .addToBackStack(characterFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_comic -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, comicFragment)
                    .addToBackStack(comicFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.nav_favorite -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, favoriteFragment)
                    .addToBackStack(favoriteFragment.toString())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
