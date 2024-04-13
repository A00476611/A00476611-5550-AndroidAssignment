package com.example.hotels.room


import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotels.R
import com.example.hotels.models.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL


class RoomListAdapter(var rooms : List<Room>) : RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_room_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.roomName.text = rooms[position].name
        viewHolder.roomPrice.text = "\$${rooms[position].price} per night"
        viewHolder.hotelName.text = rooms[position].hotel.name
        GlobalScope.launch {
            try {
                val bitmap = BitmapFactory.decodeStream(URL(rooms[position].image).content as InputStream)
                Handler(Looper.getMainLooper()).post {
                    viewHolder.roomImage.setImageBitmap(bitmap)
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        viewHolder.itemView.setOnClickListener{
            if (onClickListener != null) {
                onClickListener!!.onClick(position, rooms[position])
            }
        }
    }

    override fun getItemCount() : Int {
        return rooms.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Room)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val roomName: TextView
        val roomPrice: TextView
        val hotelName: TextView
        val roomImage: ImageView

        init {
            // Define click listener for the ViewHolder's View
            roomName = view.findViewById(R.id.RoomName)
            roomPrice = view.findViewById(R.id.RoomPrice)
            hotelName = view.findViewById(R.id.HotelName)
            roomImage = view.findViewById(R.id.RoomImage)
        }
    }
}