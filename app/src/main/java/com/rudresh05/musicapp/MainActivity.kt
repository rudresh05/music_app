package com.rudresh05.musicapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rudresh05.musicapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

   private lateinit var myRecyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitBuilder = Retrofit.Builder().baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getData("arijit singh")
        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                 // if api works successfully then this method run and we get data from api
                val dataList = response.body()?.data
//                val textView = findViewById<TextView>(R.id.textView)
//                textView.text = dataList.toString()

                myAdapter = MyAdapter(this@MainActivity, dataList!!)
                binding.recyclerView.adapter = myAdapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                Log.d("TAG", "onResponse: " + response.body())
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                // if api fails then this method run
                Log.d("Tag", "OnFailure: " + t.message)
            }
        })
    }
}