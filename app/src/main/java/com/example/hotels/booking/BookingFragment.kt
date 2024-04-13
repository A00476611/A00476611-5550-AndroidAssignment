package com.example.hotels.booking

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hotels.API
import com.example.hotels.R
import com.example.hotels.databinding.FragmentBookingBinding
import com.example.hotels.getUser
import com.example.hotels.models.Booking
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class BookingFragment : Fragment() {
    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!
    private val api = API()
    private var bookings = listOf<Booking>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val user = getUser(requireContext()) ?: return@launch
            api.getUserBookings(user.id, object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                        bookings = Json.decodeFromString<List<Booking>>(response.body()!!.string())
                        Handler(Looper.getMainLooper()).post{
                            setupRecyclerView()
                        }
                    }
                }

            })
        }
    }

    private fun setupRecyclerView(){
        val bookingListAdapter = BookingAdapter(bookings)
        bookingListAdapter.setOnClickListener( object : BookingAdapter.OnClickListener {
            override fun onClick(position: Int, booking:Booking){
                val bundle = Bundle()
                bundle.putSerializable("booking", booking);
                findNavController().navigate(R.id.navigation_booking_details, bundle)
            }
        })
        binding.BookingListView.adapter = bookingListAdapter
        binding.BookingListView.layoutManager = GridLayoutManager(requireContext(), 2)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}