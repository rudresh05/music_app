package com.rudresh05.musicapp

import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rudresh05.musicapp.databinding.EachItemBinding
import com.squareup.picasso.Picasso


class MyAdapter(val context: Activity, val dataList: List<Data>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var mediaPlayer: MediaPlayer? = null

    private var currentlyPlayingPosition: Int = -1
    private val handler = Handler(Looper.getMainLooper())
    private var updateSeekbarRunnable: Runnable? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val currentData = dataList[position]

        // Title and Image
        holder.binding.musicTitle.text = currentData.title
        Picasso.get().load(currentData.album.cover).into(holder.binding.musicImage)

        // SeekBar and button default state
        holder.binding.musicSeekBar.progress = 0
        holder.binding.playBtn.visibility = View.VISIBLE
        holder.binding.pauseBtn.visibility = View.GONE

        //  Play Button
        holder.binding.playBtn.setOnClickListener {
            // Stop previous track if any
            if (currentlyPlayingPosition != -1 && currentlyPlayingPosition != position) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                notifyItemChanged(currentlyPlayingPosition)
            }

            // Play current
            mediaPlayer = MediaPlayer().apply {
                setDataSource(currentData.preview)
                prepare()
                start()


            }

            currentlyPlayingPosition = position
            holder.binding.playBtn.visibility = View.GONE
            holder.binding.pauseBtn.visibility = View.VISIBLE

            updateSeekBar(holder)
        }


        // ⏸ Pause Button
        holder.binding.pauseBtn.setOnClickListener {
            mediaPlayer?.pause()
            holder.binding.playBtn.visibility = View.VISIBLE
            holder.binding.pauseBtn.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = dataList.size
    private fun formatTime(milliseconds: Int?): String {
        val minutes = (milliseconds?.div(1000))?.div(60)
        val seconds = (milliseconds?.div(1000))?.rem(60)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updateSeekBar(holder: MyViewHolder) {

        updateSeekbarRunnable = object : Runnable{
            override fun run() {
                mediaPlayer?.let {
                    holder.binding.musicSeekBar.max = it.duration
                    holder.binding.musicSeekBar.progress = it.currentPosition
                    holder.binding.currentTime.text = formatTime(currentlyPlayingPosition)
                    holder.binding.totalTime.text = formatTime(it.duration)

                    if (it.isPlaying) {
                        handler.postDelayed(this, 500)
                    }
                }
            }
        }
        handler.post(updateSeekbarRunnable!!)
    }

    class MyViewHolder(val binding: EachItemBinding) : RecyclerView.ViewHolder(binding.root)
}
