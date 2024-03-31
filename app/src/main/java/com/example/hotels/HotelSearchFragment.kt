package com.example.hotels


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.hotels.databinding.HotelSearchLayoutBinding
import com.example.hotels.hotelList.HotelListFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat


class HotelSearchFragment : Fragment() {
    private var _binding: HotelSearchLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var dateRangePicker:MaterialDatePicker<androidx.core.util.Pair<Long, Long>>
    private var checkIn:String? = null
    private var checkOut:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = HotelSearchLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateRangePicker.addOnPositiveButtonClickListener {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            checkIn = formatter.format(it.first+86400000)
            checkOut = formatter.format(it.second+86400000)
            binding.CheckInOutField.setText("$checkIn - $checkOut")
        }

        binding.CheckInOutField.setOnClickListener {
            binding.GuestCountField.clearFocus()
            binding.NameField.clearFocus()
            dateRangePicker.show(parentFragmentManager, "tag")
        }

        binding.SearchButton.setOnClickListener{

            val hotelListFragment = HotelListFragment()
            hotelListFragment.arguments = bundleOf(
                "checkIn" to checkIn,
                "checkOut" to checkIn,
                "name" to binding.NameField.text.toString(),
                "guestCount" to binding.GuestCountField.text.toString()
            )
            binding.NameField.clearFocus()
            binding.GuestCountField.clearFocus()

            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this)
            transaction.add(R.id.frame_layout, hotelListFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.ClearButton.setOnClickListener{
            checkIn = null
            checkOut = null
            binding.NameField.setText("")
            binding.GuestCountField.setText("")
            binding.CheckInOutField.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}