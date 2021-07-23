package com.example.favoriteconsumer

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.favoriteconsumer.adapter.SectionPager
import com.example.favoriteconsumer.databinding.ActivityDetailBinding
import com.example.favoriteconsumer.model.Favorite
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity(){
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

        setFav()
        viewPager()
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