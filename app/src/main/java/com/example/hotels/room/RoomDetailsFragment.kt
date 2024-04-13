package com.example.hotels.room

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.hotels.API
import com.example.hotels.R
import com.example.hotels.databinding.FragmentRoomDetailsBinding
import com.example.hotels.getUser
import com.example.hotels.isBlank
import com.example.hotels.isName
import com.example.hotels.models.BookRequest
import com.example.hotels.models.Room
import com.example.hotels.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
import java.net.URL


class RoomDetailsFragment : Fragment() {
    private var _binding : FragmentRoomDetailsBinding? = null
    private val binding get() = _binding!!
    private var room : Room? = null
    private var user : User? = null
    private val api = API()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentRoomDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        room = arguments?.getSerializable("room", Room::class.java)
        GlobalScope.launch {
            user = getUser(requireContext())
            val bitmap = BitmapFactory.decodeStream(URL(room?.image).content as InputStream)
            Handler(Looper.getMainLooper()).post {
                binding.DetailsRoomImage.setImageBitmap(bitmap)
            }
        }
        binding.RoomDetailName.text = room?.name;
        binding.RoomDetailHotelName.text = room?.hotel?.name
        binding.RoomDetailBedCount.text = "${room?.bedCount} beds"
        binding.RoomDetailPrice.text = "\$${room?.price} per night"
        val guestCount = requireArguments().getInt("guestCount")
        val startDate = requireArguments().getString("checkIn").toString()
        val endDate = requireArguments().getString("checkOut").toString()
        var fields = mutableListOf<TextInputLayout>()
        for( i in 1..guestCount){
            val lay = TextInputLayout(requireContext())
            lay.id = i
            lay.isErrorEnabled = true
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0,40,0,0)
            lay.layoutParams = params
            val et = TextInputEditText(lay.context)
            et.hint = "Guest Name"
            if(i==1) et.setText(user?.name)
            et.imeOptions = if (i == guestCount) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
            et.isSingleLine = true
            lay.addView(et)
            fields.add(lay)
            binding.GuestCountList.addView(lay)
        }

        binding.BookButton.setOnClickListener{
            var isError = false
            val guestNames = mutableListOf<String>()
            for(lay in fields){
                lay.error = null
                val name = lay.editText?.text.toString()
                if(name.isBlank()) {
                    lay.error = "Please enter a name for your guest"
                    isError = true
                }
                if(!name.isName()){
                    lay.error = "Please enter a valid name for your guest"
                    isError = true
                }
                guestNames.add(lay.editText?.text.toString())
            }
            if(isError) return@setOnClickListener
            val booking = BookRequest(Instant.parse(startDate), Instant.parse(endDate),room?.id,user?.id, guestCount, guestNames.joinToString(","))
            api.book(booking, object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.fillInStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                        Snackbar.make(binding.BookButton, "Booking Complete", Snackbar.LENGTH_SHORT)
                            .show()
                        Handler(Looper.getMainLooper()).post{
                            findNavController().navigate(R.id.action_details_to_booking)
                        }
                    }
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}