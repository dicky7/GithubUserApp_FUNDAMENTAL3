package com.example.githubuserapp

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.adapter.SectionPager
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.example.githubuserapp.db.DatabaseContract
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.AVATAR
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.COMPANY
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.FAVORITE
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWERS
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.FOLLOWING
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.LOCATION
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.REPOSITORY
import com.example.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.USERNAME
import com.example.githubuserapp.db.FavoriteHelper
import com.example.githubuserapp.model.Favorite
import com.example.githubuserapp.model.Person
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_PERSON = "person"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"

    }
    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteHelper: FavoriteHelper
    private var favorite: Favorite? = null
    private var isFavorite = false
    private lateinit var imageAvatar: String
    private lateinit var txtUsername: String
    private lateinit var txtCompany: String
    private lateinit var txtLocation: String
    private lateinit var txtRepository: String
    private lateinit var txtFollower: String
    private lateinit var txtFollowing: String

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        favorite = intent.getParcelableExtra(EXTRA_NOTE)
            if (favorite != null) {
                setFav()
                isFavorite = true
                binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                setData()
                val find = favoriteHelper.queryById(txtUsername)
                if (find.count == 1){
                    binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    isFavorite = true
                }
            }

        viewPager()
        binding.btnFavorite.setOnClickListener (this)
    }

    private fun setData(){
        val person = intent.getParcelableExtra<Person>(EXTRA_PERSON) as Person
        binding.detailName.text = person.login_username.toString()
        binding.detailCompany.text = person.company.toString()
        binding.detailLocation.text = person.location.toString()
        binding.detailRepotories.text = person.repository.toString()
        binding.detailFollowers.text = person.follower.toString()
        binding.detailFollowing.text = person.following.toString()
        Glide.with(this)
            .load(person.avatar_url.toString())
            .into(binding.detailAvatar)
        imageAvatar = person.avatar_url.toString()
        txtUsername = person.login_username.toString()
        txtCompany = person.company.toString()
        txtLocation = person.location.toString()
        txtRepository = person.repository.toString()
        txtFollower = person.follower.toString()
        txtFollowing = person.following.toString()
    }
    private fun setFav(){
        val personFav = intent.getParcelableExtra<Favorite>(EXTRA_NOTE) as Favorite
        binding.detailName.text = personFav.login_username.toString()
        binding.detailCompany.text = personFav.company.toString()
        binding.detailLocation.text = personFav.location.toString()
        binding.detailRepotories.text = personFav.repository.toString()
        binding.detailFollowers.text = personFav.follower.toString()
        binding.detailFollowing.text = personFav.following.toString()
        Glide.with(this)
            .load(personFav.avatar_url.toString())
            .into(binding.detailAvatar)
        imageAvatar = personFav.avatar_url.toString()
        txtUsername = personFav.login_username.toString()
        txtCompany = personFav.company.toString()
        txtLocation = personFav.location.toString()
        txtRepository = personFav.repository.toString()
        txtFollower = personFav.follower.toString()
        txtFollowing = personFav.following.toString()
    }

    override fun onClick(p0: View) {
        if (p0.id == R.id.btn_favorite){
            if (isFavorite){
                deleteFav()
            }else{
                addToFav()
            }
        }
    }

    private fun addToFav(){
        val values = ContentValues()
        values.put(AVATAR, imageAvatar)
        values.put(USERNAME,txtUsername)
        values.put(COMPANY, txtCompany)
        values.put(LOCATION,txtLocation)
        values.put(REPOSITORY,txtRepository)
        values.put(FOLLOWING, txtFollowing)
        values.put(FOLLOWERS, txtFollower)
        values.put(FAVORITE,"1")

        isFavorite = true
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        favoriteHelper.insert(values)
        Toast.makeText(this, R.string.add_user, Toast.LENGTH_SHORT).show()
        binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
    }

    private fun deleteFav(){
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        favoriteHelper.deleteById(txtUsername)
        Toast.makeText(this, R.string.delete_user, Toast.LENGTH_SHORT).show()
        binding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        isFavorite = false
    }

    private fun viewPager(){
        val sectionPager = SectionPager(this,supportFragmentManager)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPager
        val tabs: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabs,viewPager){tab,position->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
}