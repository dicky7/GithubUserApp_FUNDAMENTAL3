package com.example.favoriteconsumer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.favoriteconsumer.BuildConfig
import com.example.favoriteconsumer.DetailActivity
import com.example.favoriteconsumer.adapter.FollowAdapter
import com.example.favoriteconsumer.adapter.listFollowData
import com.example.favoriteconsumer.databinding.FragmentFollowersBinding
import com.example.favoriteconsumer.model.Favorite
import com.example.favoriteconsumer.model.Person
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject


class FollowersFragment : Fragment() {
    companion object{
        const val EXTRA_PERSON = "person"
        const val EXTRA_NOTE = "extra_note"
    }

    private var listData: ArrayList<Person> = ArrayList()
    private lateinit var binding: FragmentFollowersBinding
    var API_KEY: String = BuildConfig.API_KEY
    var BASE_URL: String = BuildConfig.BASE_URL
    private lateinit var adapter: FollowAdapter
    private var favorite: Favorite? = null
    private lateinit var person: Person
    private lateinit var favoritePerson: Favorite

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowAdapter(listData)
        listData.clear()
        favorite = activity!!.intent.getParcelableExtra(DetailActivity.EXTRA_NOTE)
        if (favorite != null){
             favoritePerson =  activity!!.intent.getParcelableExtra<Favorite>(EXTRA_NOTE) as Favorite
            getList(favoritePerson.login_username.toString())
        }
        else{
            person = activity!!.intent.getParcelableExtra<Person>(EXTRA_PERSON) as Person
            getList(person.login_username.toString())
        }
    }

    private fun showRecyclerList(){
        binding.rvFolow.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = FollowAdapter(listFollowData)
        binding.rvFolow.adapter = listUserAdapter
    }

    private fun showLoading(boolean: Boolean){
        if (boolean){
            binding.progressbarFollow.visibility = View.VISIBLE
        }
        else{
            binding.progressbarFollow.visibility = View.GONE
        }
    }

    private fun getList(dat: String){
        showLoading(true)
        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.addHeader("Authorization", "token $API_KEY")
        asyncHttpClient.addHeader("User-Agent", "request")
        val url = "$BASE_URL/$dat/followers"
        asyncHttpClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                showLoading(false)
                val res = responseBody?.let { String(it) }
                try {
                    val jsonArry = JSONArray(res)
                    for (position in 0 until jsonArry.length()) {
                        val jsonObj = jsonArry.getJSONObject(position)
                        val username: String = jsonObj.getString("login")
                        getListData(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getListData(idUser: String) {
        showLoading(true)
        val asyncHttpClient = AsyncHttpClient()
        asyncHttpClient.addHeader("Authorization","token $API_KEY")
        asyncHttpClient.addHeader("User-Agent","request")
        val url = "https://api.github.com/users/$idUser"
        asyncHttpClient.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                showLoading(false)
                val res = responseBody?.let { String(it) }
                try {
                    val person = Person()
                    val jsonObj = JSONObject(res)
                    person.login_username = jsonObj.getString("login").toString()
                    person.avatar_url = jsonObj.getString("avatar_url").toString()
                    person.company = jsonObj.getString("company").toString()
                    person.location = jsonObj.getString("location").toString()
                    person.repository = jsonObj.getInt("public_repos")
                    person.follower = jsonObj.getInt("followers")
                    person.following  = jsonObj.getInt("following")
                    listData.add(person)
                    showRecyclerList()
                } catch (e: Exception) {
                    if (context != null) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    }
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                showLoading(false)
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

}