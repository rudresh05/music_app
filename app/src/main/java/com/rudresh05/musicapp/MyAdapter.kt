package com.rudresh05.musicapp

import android.app.Activity
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.rudresh05.musicapp.databinding.EachItemBinding
import com.squareup.picasso.Picasso


class MyAdapter(val context: Activity, val dataList:List<Data>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        // create view in case the layout fails to create view for the data
        val itemView = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder,position: Int) {
       //populate the data into the view
        val currentData = dataList[position]

        val mediaPlayer = MediaPlayer.create(context, currentData.preview.toUri())
        holder.binding.musicTitle.text = currentData.title

        Picasso.get().load(currentData.album.cover).into(holder.binding.musicImage)

        holder.binding.playBtn.setOnClickListener{
            mediaPlayer.start()
        }
        holder.binding.pauseBtn.setOnClickListener{
            mediaPlayer.stop()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class MyViewHolder(val binding: EachItemBinding): RecyclerView.ViewHolder(binding.root){
    }
}