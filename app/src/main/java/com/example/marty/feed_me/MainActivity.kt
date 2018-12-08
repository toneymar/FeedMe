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
import com.example.marty.feed_me.adapter.SearchAdapter
import com.example.marty.feed_me.data.AppDatabase
import com.example.marty.feed_me.data.Recipe
import com.example.marty.feed_me.data.RecipeResult
import com.example.marty.feed_me.network.RecipeAPI
import com.example.marty.feed_me.touch.RecipeTouchHelperCallback
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, RecipeDialog.RecipeHandler {

    private val HOST_URL = "https://www.food2fork.com/api/"


    //Test array to see how the adapter works
    private var searchResults = mutableListOf<String>()


    // API KEY: 64b4b6087380614360a164f1fc132f28
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Toast.makeText(this@MainActivity,
                    "We'll implement the search function in a bit",
                    Toast.LENGTH_LONG)
                    .show()

            showSearchRecipeDialog()
        }
        initRecyclerView()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Testing out the API call with this function
        //showSearchResults("chicken")
    }

    private fun showSearchResults(recipe: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val recipeAPI = retrofit.create(RecipeAPI::class.java)
        val recipeCall =
                recipeAPI.getRecipe(
                        "64b4b6087380614360a164f1fc132f28",
                        recipe, 1
                )

        recipeCall.enqueue(object : Callback<RecipeResult> {

            override fun onFailure(call: Call<RecipeResult>, t: Throwable) {
                //imIcon.setImageResource(R.drawable.error)
                //tvCityName.text = getString(R.string.cannot_connect)

            }

            override fun onResponse(call: Call<RecipeResult>, response: Response<RecipeResult>) {
                val recipeResult = response.body()


                for(r in recipeResult?.recipes!!){
                    tvAPItest.append(r.title + "\n")
                }
            }

        })

    }

    private fun initRecyclerView() {
        Thread {
            val recipeList = AppDatabase.getInstance(this@MainActivity).recipeDao().findAllRecipes()
            searchAdapter = SearchAdapter(this@MainActivity, recipeList)

            runOnUiThread {
                recyclerView.adapter = searchAdapter
                //val callback = RecipeTouchHelperCallback(searchAdapter)
                //val touchHelper = ItemTouchHelper(callback)
                //touchHelper.attachToRecyclerView(recyclerView)
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

    private fun showSearchRecipeDialog() {
        RecipeDialog().show(supportFragmentManager, "TAG_CREATE")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                Thread {
                    AppDatabase.getInstance(this@MainActivity).recipeDao().deleteAll()
                    runOnUiThread {
                        searchAdapter.deleteAll()
                    }
                }.start()
            }
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_search_recipe -> {

                showSearchRecipeDialog()
            }
            R.id.nav_fav -> {
                startActivity(Intent(this@MainActivity, FavoritesActivity::class.java))
                //Probably need to put extra here
            }
            R.id.nav_about -> {
                Toast.makeText(this@MainActivity,
                        "Authors: Pratik Karki, Huseyin Altinisik, Marty Toney",
                        Toast.LENGTH_LONG).show()
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
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
            runOnUiThread {
                searchAdapter.addRecipe(recipe)
            }
        }.start()
    }
}
