package com.example.novelrepo.viewmodel

import androidx.lifecycle.*
import com.example.novelrepo.data.Book
import com.example.novelrepo.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(
    private val repository: BookRepository
) : ViewModel() {

    val allBooks =
        repository.allBooks.asLiveData()

    val libraryBooks =
        repository.libraryBooks.asLiveData()

    fun addToLibrary(book: Book) {

        viewModelScope.launch {

            repository.update(
                book.copy(
                    inLibrary = true
                )
            )
        }
    }

    fun removeFromLibrary(book: Book) {

        viewModelScope.launch {

            repository.update(
                book.copy(
                    inLibrary = false
                )
            )
        }
    }

    fun markCompleted(book: Book) {

        viewModelScope.launch {

            repository.update(
                book.copy(
                    isCompleted = true
                )
            )
        }
    }
}