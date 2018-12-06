package com.example.marty.feed_me

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.marty.feed_me.adapter.RecipeAdapter
import com.example.marty.feed_me.data.AppDatabase
import com.example.marty.feed_me.data.Recipe
import com.example.marty.feed_me.touch.RecipeTouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, RecipeDialog.RecipeHandler {

    private lateinit var recipeAdapter: RecipeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showAddRecipeDialog()
        }
        initRecyclerView()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView() {
        Thread {
            val recipeList = AppDatabase.getInstance(this@MainActivity).recipeDao().findAllRecipes()
            recipeAdapter = RecipeAdapter(this@MainActivity, recipeList)
            runOnUiThread {
                recyclerView.adapter = recipeAdapter
                val callback = RecipeTouchHelperCallback(recipeAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerView)
            }
        }.start()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            val intentMain = Intent(Intent.ACTION_MAIN)
            intentMain.addCategory(Intent.CATEGORY_HOME)
            intentMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intentMain)
        }
    }
    private fun showAddRecipeDialog(){
        RecipeDialog().show(supportFragmentManager, "TAG_CREATE")
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_fav -> {
                Toast.makeText(this@MainActivity, "FAVES", Toast.LENGTH_LONG).show()
            }
            R.id.nav_about -> {
                Toast.makeText(this@MainActivity, "ABOUT", Toast.LENGTH_LONG).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this@MainActivity, "LOGOUT", Toast.LENGTH_LONG).show()
            }
            R.id.nav_view -> {
                Toast.makeText(this@MainActivity, "VIEW", Toast.LENGTH_LONG).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun recipeCreated(recipe: Recipe) {
        Thread {
            val recipeId = AppDatabase.getInstance(
                    this@MainActivity).recipeDao().insertRecipe(recipe)
            recipe.recipeId = recipeId
            runOnUiThread{
                recipeAdapter.addRecipe(recipe)
            }
        }.start()
    }
}
