package com.example.novelrepo.repository

import com.example.novelrepo.data.Book
import com.example.novelrepo.data.BookDao

class BookRepository(
    private val dao: BookDao,
    private val firebaseRepository: FirebaseRepository
) {

    val allBooks = dao.getAllBooks()

    val libraryBooks = dao.getLibraryBooks()

    suspend fun insert(book: Book) {

        dao.insert(book)
        firebaseRepository.syncBook(book)
    }

    suspend fun update(book: Book) {

        dao.update(book)
        firebaseRepository.syncBook(book)
    }

    suspend fun delete(book: Book) {

        dao.delete(book)
        firebaseRepository.deleteBook(book)
    }
}