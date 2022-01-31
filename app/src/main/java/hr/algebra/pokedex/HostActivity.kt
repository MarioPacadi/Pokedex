package hr.algebra.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import hr.algebra.pokedex.databinding.ActivityHostBinding

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(
            android.R.anim.fade_in, android.R.anim.fade_out)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initHamburgerMenu()
        initNavigation()
    }

    private fun initHamburgerMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(hr.algebra.pokedex.R.drawable.ic_menu)
    }

    private fun initNavigation() {
        var navController = Navigation.findNavController(this, hr.algebra.pokedex.R.id.navHostFragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                toggleDrawer()
                return true
            }
            hr.algebra.pokedex.R.id.menuExit -> {
                exitApp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun exitApp() {
        AlertDialog.Builder(this).apply {
            setTitle(hr.algebra.pokedex.R.string.exit)
            setMessage(getString(hr.algebra.pokedex.R.string.really))
            setIcon(hr.algebra.pokedex.R.drawable.exit)
            setCancelable(true)
            setNegativeButton(getString(hr.algebra.pokedex.R.string.cancel), null)
            setPositiveButton("Ok") { _, _ -> finish()}
            show()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(hr.algebra.pokedex.R.menu.host_menu, menu)
        return true
    }
}