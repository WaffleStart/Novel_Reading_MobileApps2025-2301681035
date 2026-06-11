package com.example.novelrepo.repository

import android.content.Context
import com.example.novelrepo.data.AppDatabase
import com.example.novelrepo.data.Book
import kotlinx.coroutines.flow.Flow

class BookRepository private constructor(context: Context) {
    private val db = AppDatabase.getInstance(context.applicationContext)
    private val dao = db.bookDao()

    fun allFlow(): Flow<List<Book>> = dao.getAllFlow()
    fun libraryFlow(): Flow<List<Book>> = dao.getLibraryFlow()
    suspend fun getById(id: Int): Book? = dao.getById(id)
    suspend fun update(book: Book) = dao.update(book)
    suspend fun delete(book: Book) = dao.delete(book)

    suspend fun insert(book: Book) = dao.insert(book)

    companion object {
        @Volatile private var INSTANCE: BookRepository? = null
        fun getInstance(context: Context): BookRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: BookRepository(context).also { INSTANCE = it }
            }
    }
}
