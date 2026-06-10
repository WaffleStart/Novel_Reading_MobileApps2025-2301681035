package com.example.novelrepo.repository

import android.util.Log
import com.example.novelrepo.data.Book
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {

    private val db = FirebaseFirestore.getInstance()

    fun syncBook(book: Book) {

        db.collection("library")
            .document(book.id.toString())
            .set(book)
            .addOnSuccessListener {
                Log.d("Firestore", "Book synced")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Sync failed", it)
            }
    }

    fun deleteBook(book: Book) {

        db.collection("library")
            .document(book.id.toString())
            .delete()
    }
}