package com.example.githubuserapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapp.CustomOnItemClickListener
import com.example.githubuserapp.DetailActivity
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.example.githubuserapp.databinding.ListItemBinding
import com.example.githubuserapp.model.Favorite
import com.example.githubuserapp.model.Person

class FavoriteAdapter (private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoritViewHolder>() {
    private lateinit var binding: ListItemBinding


    var listFav = ArrayList<Favorite>()
        set(fav){
            if (fav.size > 0){
                this.listFav.clear()
            }
            this.listFav.addAll(fav)
            notifyDataSetChanged()
        }

    inner class FavoritViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName: TextView = itemView.findViewById(R.id.tv_item_name)
        fun bind(fav: Favorite){
            with(itemView){
                txtName.text = fav.login_username
                Glide.with(itemView.context)
                    .load(fav.avatar_url)
                    .apply(RequestOptions().override(250, 250))
                    .into(itemView.findViewById(R.id.img_item_photo))

                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback{
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity,DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_POSITION, fav)
                                intent.putExtra(DetailActivity.EXTRA_NOTE,fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoritViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return FavoritViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoritViewHolder, position: Int) {
       holder.bind(listFav[position])
    }

    override fun getItemCount(): Int = this.listFav.size


}