package com.artm44.phonecontacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.artm44.phonecontacts.ui.screens.MainScreen
import com.artm44.phonecontacts.ui.theme.PhoneContactsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneContactsTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(this)
                }
            }
        }
    }
}
