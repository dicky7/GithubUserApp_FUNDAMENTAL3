package com.example.favoriteconsumer

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteconsumer.adapter.FavoriteAdapter
import com.example.favoriteconsumer.alarm.SettingActivity
import com.example.favoriteconsumer.databinding.ActivityMainBinding
import com.example.favoriteconsumer.db.DatabaseContract
import com.example.favoriteconsumer.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.favoriteconsumer.helper.MappingHelper
import com.example.favoriteconsumer.model.Favorite
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var favoriteAdapter: FavoriteAdapter
    companion object{
        private const val  EXTRA_STATE = "EXTRA_STATE"
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showRecyclerList()

        val handlerThread = HandlerThread("Observer")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserv = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadData()
            }
        }


        contentResolver.registerContentObserver(CONTENT_URI,true,myObserv)
        if (savedInstanceState == null){
            loadData()
        }
        else{
            val listData = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (listData != null){
                favoriteAdapter.listFav = listData
            }
        }

    }

    private fun showRecyclerList(){
        binding.rvFav.layoutManager = LinearLayoutManager(this)
        favoriteAdapter = FavoriteAdapter(this)
        binding.rvFav.adapter = favoriteAdapter

    }


    private fun loadData(){
        GlobalScope.launch (Dispatchers.Main){
            binding.progressBar.visibility = View.VISIBLE
            val deferred = async(Dispatchers.IO){
                val cursor = contentResolver?.query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null,null,null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val listFavoriteData = deferred.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (listFavoriteData.size > 0){
                favoriteAdapter.listFav = listFavoriteData
            }
            else{
                favoriteAdapter.listFav = ArrayList()
                showSnackbarMessage()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.listFav)
    }

    private fun showSnackbarMessage() {
        Snackbar.make(binding.rvFav, R.string.list_fav, Snackbar.LENGTH_SHORT).show()
    }

    // run this func when open again for refresh data
    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
                true
            }
            R.id.notification_set ->{
                val mIntent = Intent(this, SettingActivity::class.java)
                startActivity(mIntent)
                true
            }
            else -> true
        }
    }

}