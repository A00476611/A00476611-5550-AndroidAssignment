package com.example.hotels

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.hotels.R
import com.example.hotels.UserStoreManager
import com.example.hotels.login.LoginFragment
import com.example.hotels.models.User
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val user:User? = getUser(this@MainActivity)
            if(user==null) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            finish()
        }

    }
}