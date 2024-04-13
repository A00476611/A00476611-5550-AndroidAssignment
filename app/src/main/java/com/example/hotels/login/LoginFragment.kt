package com.example.hotels.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.hotels.API
import com.example.hotels.LoginActivity
import com.example.hotels.MainActivity
import com.example.hotels.R
import com.example.hotels.UserStoreManager
import com.example.hotels.databinding.FragmentLoginBinding
import com.example.hotels.models.Login
import com.example.hotels.models.User
import com.example.hotels.register.RegisterFragment
import com.example.hotels.saveUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException



class LoginFragment : Fragment() {
    private var _binding:FragmentLoginBinding ? = null
    private val binding get() = _binding!!
    private val api = API()
    private val client = OkHttpClient()
    //private val Context.datastore :DataStore<Preferences> by preferencesDataStore(name="user")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userStore = UserStoreManager.getInstance(requireContext())
        binding.LoginButton.setOnClickListener{
            val email = binding.EmailField.text.toString()
            val pass = binding.PasswordField.text.toString()

            api.login(Login(email, pass),object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // Something went wrong
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseStr = response.body()!!.string()
                        val res = Json.decodeFromString<User>(responseStr)
                        Handler(Looper.getMainLooper()).post {
                            binding.IncorrectPasswordNotice.visibility = View.INVISIBLE
                        }
                        lifecycleScope.launch {
                            saveUser(requireContext(), res)
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            binding.IncorrectPasswordNotice.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }

        binding.RegisterButton.setOnClickListener{
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                com.google.android.material.R.anim.m3_motion_fade_enter,
                com.google.android.material.R.anim.m3_motion_fade_exit,
                com.google.android.material.R.anim.m3_motion_fade_enter,
                com.google.android.material.R.anim.m3_motion_fade_exit
            )
            transaction.remove(this)
            transaction.add(R.id.frame_layout, RegisterFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    fun login(callback:Callback) : Call{
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val body = RequestBody.create(JSON, Json.encodeToString(Login("dhenry@gmail.com", "1234")))
        val request = Request.Builder()
            .url("http://140.184.52.254:8080/user/login")
            .post(body)
            .build()
        val call: Call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

//    suspend fun saveUser(user:User){
//        val NAME = stringPreferencesKey("userName")
//        val EMAIL = stringPreferencesKey("userEmail")
//        val ID = intPreferencesKey("userId")
//        context?.datastore?.edit { pref ->
//            pref[NAME] = user.name
//            pref[EMAIL] = user.email
//            pref[ID] = user.id
//        }
//    }

}