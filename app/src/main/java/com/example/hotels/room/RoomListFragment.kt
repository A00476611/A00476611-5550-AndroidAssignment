package com.example.hotels.room


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotels.API
import com.example.hotels.R
import com.example.hotels.databinding.FragmentHotelListBinding
import com.example.hotels.models.Room
import com.example.hotels.models.Search
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class RoomListFragment : Fragment() {
    private var _binding : FragmentHotelListBinding? = null
    private val binding get() = _binding!!

    private var rooms = listOf<Room>(Room())
    private var guestCount = 0
    private var checkIn = ""
    private var checkOut = ""

    private var api = API()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentHotelListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        checkIn =requireArguments().getString("checkIn").toString()
        checkOut = requireArguments().getString("checkOut").toString()
        guestCount = requireArguments().getInt("guestCount")

        if(checkIn!=null && checkOut!= null && guestCount!=null)
            api.searchRooms(Search(Instant.parse(checkIn), Instant.parse(checkOut), guestCount), object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseStr = response.body()!!.string()
                        rooms = Json.decodeFromString<List<Room>>(responseStr)

                        Handler(Looper.getMainLooper()).post{
                            binding.ProgressBar.hide()
                            setupRecyclerView()
                        }
                    } else {
                        var t = ""
                        // Request not successful
                    }
                }
            })
    }

    private fun setupRecyclerView() {
        binding.ProgressBar.setVisibility(View.GONE)
        val recyclerView: RecyclerView = binding.RoomListView;
        recyclerView.setLayoutManager(LinearLayoutManager(activity))
        val roomListAdapter = RoomListAdapter(rooms)
        recyclerView.setAdapter(roomListAdapter)

        //Bind the click listener
        roomListAdapter.setOnClickListener(object : RoomListAdapter.OnClickListener {
            override fun onClick(position: Int, model: Room) {
                val bundle = Bundle();
                bundle.putSerializable("room", model)
                bundle.putInt("guestCount", guestCount)
                bundle.putString("checkIn", checkIn)
                bundle.putString("checkOut", checkOut)
                findNavController().navigate(R.id.action_list_to_details, bundle)
            }
        } )


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}