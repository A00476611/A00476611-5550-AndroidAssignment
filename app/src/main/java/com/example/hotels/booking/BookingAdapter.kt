package com.example.hotels.booking

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
import com.example.hotels.models.Booking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

class BookingAdapter(var bookings:List<Booking>) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_booking_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hotelName.text = bookings[position].room.hotel.name
        holder.roomName.text = bookings[position].room.name
        GlobalScope.launch {
            try {
                val bitmap =
                    BitmapFactory.decodeStream(URL(bookings[position].room.image).content as InputStream)
                Handler(Looper.getMainLooper()).post {
                    holder.roomImage.setImageBitmap(bitmap)
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        holder.itemView.setOnClickListener{
            if (onClickListener != null) {
                onClickListener!!.onClick(position, bookings[position])
            }
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, booking: Booking)
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        val roomImage : ImageView = view.findViewById(R.id.BookingCardImage)
        val roomName : TextView = view.findViewById(R.id.BookingCardRoomName)
        val hotelName : TextView = view.findViewById(R.id.BookingCardHotelName)
    }
}