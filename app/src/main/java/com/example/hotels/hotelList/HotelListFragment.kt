package com.example.hotels.hotelList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotels.databinding.FragmentHotelListBinding
import java.text.SimpleDateFormat


class HotelListFragment : Fragment() {
    private var _binding : FragmentHotelListBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentHotelListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = arguments?.getString("name") ?: "user"
        var checkIn = "your check in date"
        if(arguments?.getString("checkIn") != null ) checkIn = formatDate(arguments?.getString("checkIn"))
        var checkOut = "your check out date"
        if(arguments?.getString("checkOut") != null) checkOut = formatDate(arguments?.getString("checkOut"))
        val guestCount = arguments?.getString("guestCount") ?: "your specified number of"
        binding.TitleText.text = "Hi ${name},"
        binding.SubtitleText.text = "Here are some hotels available between ${checkIn} and ${checkOut} that can fit ${guestCount} guests."
        binding.HotelListView.setLayoutManager(LinearLayoutManager(getActivity()))
        binding.HotelListView.adapter = HotelListAdapter(listOf(
            HotelViewModel("Hilton", 2000, true),
            HotelViewModel("Bell", 1050, true),
            HotelViewModel("Mariotte", 3000, true),
            HotelViewModel("First Western", 2500, true),
            HotelViewModel("Chakra", 600, true),
            HotelViewModel("Blue Horizons", 1100, true),
            HotelViewModel("Nova Sol", 1100, true),
        ))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(dateString:String?):String{
        val hardFormat = SimpleDateFormat("dd-MM-yyyy")
        val easyFormat = SimpleDateFormat("MMMM d, yyyy")
        return easyFormat.format(hardFormat.parse(dateString))

    }
}