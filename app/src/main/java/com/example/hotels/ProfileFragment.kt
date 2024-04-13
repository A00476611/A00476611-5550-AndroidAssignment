package com.example.hotels

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.hotels.databinding.FragmentProfileBinding
import com.example.hotels.models.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val api = API()
    private var user : User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            user = getUser(requireContext())
            Handler(Looper.getMainLooper()).post{
                binding.ProfileNameField.setText(user?.name)
                binding.ProfileEmailField.setText(user?.email)
            }
        }

        binding.ProfileLogOutButton.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Cancel") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Log Out") { dialog, which ->
                    runBlocking {
                        clearUser(requireContext())
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                .show()
        }

        binding.UpdateProfileButton.setOnClickListener{
            val name = binding.ProfileNameField.text.toString()
            val email = binding.ProfileEmailField.text.toString()
            val nameLayout = binding.ProfileNameLayout
            val emailLayout = binding.ProfileEmailLayout
            var error = false
            if(name.isBlank()) {
                nameLayout.error = "Please enter a name"
                error = true;
            }
            if(email.isBlank()) {
                emailLayout.error = "Please enter an email"
                error = true;
            }
            if(!email.isEmail()) {
                emailLayout.error = "Please enter a valid email"
                error = true;
            }

            if(!error && user != null) {
                user!!.name = name
                user!!.email = email
                api.updateUser(user!!, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful) {
                            val newUser = Json.decodeFromString<User>(response.body()!!.string())
                            runBlocking {
                                saveUser(requireContext(),newUser)
                            }
                            Snackbar.make(binding.UpdateProfileButton, "Profile Updated Successfully", Snackbar.LENGTH_SHORT)
                                .show()

                        }
                    }

                })
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}