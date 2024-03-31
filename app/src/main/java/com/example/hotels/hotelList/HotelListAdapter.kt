package com.example.hotels.hotelList

import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotels.R

class HotelListAdapter(private val hotelItems : List<HotelViewModel>) : RecyclerView.Adapter<HotelListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_hotel_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.hotelName.text = hotelItems[position].name
        viewHolder.hotelPrice.text = "\$${hotelItems[position].price}"
    }

    override fun getItemCount() : Int {
        return hotelItems.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hotelName: TextView
        val hotelPrice: TextView

        init {
            // Define click listener for the ViewHolder's View
            hotelName = view.findViewById(R.id.HotelName)
            hotelPrice = view.findViewById(R.id.HotelPrice)
        }
    }

}