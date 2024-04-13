package com.example.hotels.room


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hotels.API
import com.example.hotels.R
import com.example.hotels.databinding.FragmentHotelSearchBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import java.text.SimpleDateFormat
import java.util.Date


class RoomSearchFragment : Fragment() {
    private var _binding: FragmentHotelSearchBinding? = null
    private val binding get() = _binding!!
    private val api = API()
    private var checkIn:Instant? = null
    private var checkOut:Instant? = null
    private lateinit var CheckInDPD: MaterialDatePicker<Long>
    private lateinit var CheckOutDPD: MaterialDatePicker<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CheckInDPD = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Check In Date")
            .build()
        CheckOutDPD = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Check Out Date")
            .build()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHotelSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CheckInDPD.addOnPositiveButtonClickListener{
            checkIn = Instant.fromEpochMilliseconds(it)
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it + 86400000 ))
            binding.CheckInField.setText(date)
        }

        CheckOutDPD.addOnPositiveButtonClickListener{
            checkOut = Instant.fromEpochMilliseconds(it)
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormatter.format(Date(it + 86400000 ))
            binding.CheckOutField.setText(date)
        }

        binding.CheckInField.setOnClickListener {
            binding.GuestCountField.clearFocus()
            CheckInDPD.show(parentFragmentManager, "startdate")
        }

        binding.CheckOutField.setOnClickListener {
            binding.GuestCountField.clearFocus()
            CheckOutDPD.show(parentFragmentManager, "startdate")
        }

        binding.SearchButton.setOnClickListener{
            binding.CheckInLayout.error = null
            binding.CheckOutLayout.error = null
            binding.GuestCountLayout.error = null
            var isError = false
            var bedCount = 0;
            if(binding.GuestCountField.text.toString() == "") {
                binding.GuestCountLayout.error = "Please enter a guest count"
                isError = true
            }else bedCount = binding.GuestCountField.text.toString().toInt()
            if(checkIn == null) {
                binding.CheckInLayout.error = "Please enter a check in date"
                isError = true
            }
            if(checkOut == null) {
                binding.CheckOutLayout.error = "Please enter a check out date"
                isError = true
            }
            if(checkIn != null && checkOut != null) {
                if (checkIn!!.daysUntil(Clock.System.now(), TimeZone.currentSystemDefault()) > 0) {
                    binding.CheckInLayout.error = "Check in date cannot be in the past"
                    isError = true
                }
                if (checkOut!!.daysUntil(Clock.System.now(), TimeZone.currentSystemDefault()) > 0) {
                    binding.CheckOutLayout.error = "Check out date cannot be in the past"
                    isError = true
                }
                if (checkIn!!.daysUntil(checkOut!!, TimeZone.currentSystemDefault()) <= 0) {
                    binding.CheckInLayout.error = "Check in date must be before check out date"
                    binding.CheckOutLayout.error = "Check out date must be after check in date"
                    isError = true
                }
            }
            if(isError) return@setOnClickListener


            val bundle = bundleOf(
                "checkIn" to checkIn.toString(),
                "checkOut" to checkOut.toString(),
                "guestCount" to binding.GuestCountField.text.toString().toInt()
            )
            binding.GuestCountField.clearFocus()
            findNavController().navigate(R.id.action_search_to_list, bundle)

        }

        binding.ClearButton.setOnClickListener{
            checkIn = null
            checkOut = null
            binding.GuestCountField.setText("")
            binding.CheckInField.setText("")
            binding.CheckOutField.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}