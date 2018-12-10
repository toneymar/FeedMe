package com.example.marty.feed_me

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.marty.feed_me.adapter.FavoritesAdapter
import com.example.marty.feed_me.data.Favorite
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritesAdapter = FavoritesAdapter(this,
                FirebaseAuth.getInstance().currentUser!!.uid)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        rvFavorites.layoutManager = layoutManager
        rvFavorites.adapter = favoritesAdapter

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val db = FirebaseFirestore.getInstance()
        val postsCollection = db.collection("favorites" + FirebaseAuth.getInstance().currentUser!!.uid)

        postsCollection.addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(querySnapshot: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                if (p1 != null) {
                    Toast.makeText(this@FavoritesActivity, "Error: ${p1.message}", Toast.LENGTH_LONG).show()
                    return
                }

                for (docChange in querySnapshot!!.getDocumentChanges()) {
                    when (docChange.type) {
                        DocumentChange.Type.ADDED -> {
                            val post = docChange.document.toObject(Favorite::class.java)
                            favoritesAdapter.addRecipe(post, docChange.document.id)
                        }
                        DocumentChange.Type.MODIFIED -> {

                        }
                        DocumentChange.Type.REMOVED -> {
                            favoritesAdapter.removeFavoriteByKey(docChange.document.id)
                        }
                    }
                }
            }
        })


        /*    Thread {

            //get values from firebase
            val recipes = AppDatabase.getInstance(this).recipeDao().findAllRecipes()

            favoritesAdapter = FavoritesAdapter(this@FavoritesActivity, recipes)

            runOnUiThread {
                rvFavorites.adapter = favoritesAdapter

                runOnUiThread {
                    rvFavorites.layoutManager = LinearLayoutManager(this@FavoritesActivity)
                    rvFavorites.adapter = favoritesAdapter
                    //val callback = RecipeTouchHelperCallback(favoritesAdapter)
                    //val touchHelper = ItemTouchHelper(callback)
                    //touchHelper.attachToRecyclerView(rvFavorites)
                }
            }
        }.start()*/
    }
}
