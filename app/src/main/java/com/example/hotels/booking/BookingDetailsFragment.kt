package com.example.hotels.booking

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hotels.API
import com.example.hotels.R
import com.example.hotels.databinding.FragmentBookingDetailsBinding
import com.example.hotels.models.Booking
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class BookingDetailsFragment : Fragment() {
    private var _binding: FragmentBookingDetailsBinding? = null
    private val binding get() = _binding!!
    private var booking : Booking? = null
    val api = API()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBookingDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booking = arguments?.getSerializable("booking", Booking::class.java)
        binding.BookingDetailsHotelName.text = booking?.room?.hotel?.name;
        binding.BookingDetailsRoomName.text = booking?.room?.name
        binding.BookingDetailsPrice.text = "\$${booking?.room?.price} per night"
        binding.BookingDetailsDuration.text = "${formatDate(booking?.startDate)} - ${formatDate(booking?.endDate)}"
        var guests = booking?.guestNames?.split(",")
        if(guests != null ){
            for(guest in guests){
                val guestChip = Chip(requireContext())
                guestChip.text = guest
                binding.BookingDetailsGuestChips.addView(guestChip)
            }
        }
        GlobalScope.launch {
            val bitmap = BitmapFactory.decodeStream(URL(booking?.room?.image).content as InputStream)
            Handler(Looper.getMainLooper()).post {
                binding.BookingDetailsImage.setImageBitmap(bitmap)
            }
        }

        binding.CancelBookingButton.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setNegativeButton("No") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Yes") { dialog, which ->
                    api.deleteBooking(booking?.id!!, object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                            if(response.isSuccessful){
                                Snackbar.make(binding.CancelBookingButton, "Booking Canceled", Snackbar.LENGTH_SHORT)
                                    .show()
                                Handler(Looper.getMainLooper()).post{
                                    findNavController().navigate(R.id.action_details_to_booking)
                                }
                            }
                        }

                    })
                }
                .show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(instant: Instant?) : String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault())
       return formatter.format(instant?.toJavaInstant());
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}