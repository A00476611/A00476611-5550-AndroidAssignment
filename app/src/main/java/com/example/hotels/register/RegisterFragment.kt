package com.example.hotels.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hotels.API
import com.example.hotels.MainActivity
import com.example.hotels.R
import com.example.hotels.databinding.FragmentRegisterBinding
import com.example.hotels.isBlank
import com.example.hotels.isEmail
import com.example.hotels.models.User
import com.example.hotels.saveUser
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class RegisterFragment :Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val api = API()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SubmitButton.setOnClickListener {
            val name = binding.NameField.text.toString()
            val email = binding.EmailField.text.toString()
            val password = binding.PasswordField.text.toString()
            var errorFound = false
            if( name.isBlank()){
                binding.NameLayout.error = "Please enter a name"
                errorFound = true
            }
            if(email.isBlank()){
                binding.NameLayout.error = "Please enter an email"
                errorFound = true
            }
            if (!email.isEmail()){
                binding.EmailLayout.error = "Please enter a valid email"
                errorFound = true
            }
            if(password.isBlank()){
                binding.PasswordLayout.error = "Please enter a password"
                errorFound = true
            }
            if(password != binding.RePasswordField.text.toString()){
                binding.PasswordLayout.error = "Passwords do not match"
                errorFound = true
            }

            if(errorFound) return@setOnClickListener

            api.register(User(0, name, email, password), object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                        val user = Json.decodeFromString<User>(response.body()!!.string())
                        runBlocking {
                            saveUser(requireContext(), user)
                        }
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    }
                }

            })
        }

        binding.BackButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    fun isEmail(email:String) : Boolean {
        return email.matches("""\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*""".toRegex())
    }
}