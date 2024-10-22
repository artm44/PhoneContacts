package com.artm44.phonecontacts.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.artm44.phonecontacts.model.Contact
import com.artm44.phonecontacts.utils.fetchAllContacts

@Composable
fun MainScreen(context: Context) {
    val contacts = remember { mutableStateListOf<Contact>() }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val fetchedContacts = context.fetchAllContacts().sortedBy { it.name }
            contacts.addAll(fetchedContacts)
            val count = contacts.size
            val suffix = if (count == 1) "" else "ов"
            Toast.makeText(context, "Найдено $count контакт$suffix", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Разрешение не получено\n ", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            val fetchedContacts = context.fetchAllContacts().sortedBy { it.name }
            contacts.addAll(fetchedContacts)
            val count = contacts.size
            val suffix = if (count == 1) "" else "ов"
            Toast.makeText(context, "Найдено $count контакт$suffix", Toast.LENGTH_SHORT).show()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    if (contacts.isNotEmpty()) {
        ContactsScreen(context, contacts)
    }
}

