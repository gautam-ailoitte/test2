package com.example.test2.utlis


import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils


object ContactUtils {
    private const val DAD_PHONE_NUMBER = "+917542036307" // Replace with actual dad's number

    fun isTrustedContact(contentResolver: ContentResolver, phoneNumber: String): Boolean {
        val uri: Uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )

        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        val isContact = (cursor?.count ?: 0) > 0
        cursor?.close()
        return isContact
    }

    fun isDadCall(phoneNumber: String): Boolean {
        return PhoneNumberUtils.compare(phoneNumber, DAD_PHONE_NUMBER)
    }
}
