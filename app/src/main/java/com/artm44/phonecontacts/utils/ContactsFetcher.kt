package com.artm44.phonecontacts.utils

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import com.artm44.phonecontacts.model.Contact

fun normalizePhoneNumber(phoneNumber: String): String {
    var normalizedNumber = phoneNumber.replace(Regex("[^\\d+]"), "")

    if (normalizedNumber.startsWith("8") && normalizedNumber.length == 11) {
        normalizedNumber = "+7" + normalizedNumber.substring(1)
    }

    return normalizedNumber
}


fun Context.fetchAllContacts(): List<Contact> {
    return try {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
            .use { cursor ->
                if (cursor == null) return emptyList()
                val uniqueContacts = mutableSetOf<Contact>()
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            ?: "N/A"
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            ?: "N/A"
                    val photoUri =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                            ?.let { Uri.parse(it) }
                    uniqueContacts.add(Contact(name, normalizePhoneNumber(phoneNumber), photoUri))
                }
                return uniqueContacts.toList()
            }
    } catch (e: SecurityException) {
    emptyList()
    } catch (e: Exception) {
        Toast.makeText(this, "Ошибка при доступе к контактам", Toast.LENGTH_SHORT).show()
        emptyList()
    }
}


fun addContacts(context: Context, contacts: MutableList<Contact>) {
    val fetchedContacts = context.fetchAllContacts().sortedBy { it.name }
    contacts.addAll(fetchedContacts)
    val count = contacts.size
    val suffix = if (count == 1) "" else "ов"
    Toast.makeText(context, "Найдено $count контакт$suffix", Toast.LENGTH_SHORT).show()
}