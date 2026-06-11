package com.example.novelrepo.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.novelrepo.data.Book
import com.example.novelrepo.repository.BookRepository
import kotlinx.coroutines.launch

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: BookRepository = BookRepository.getInstance(application)

    // Expose flows as LiveData for easy observation in Activities
    val allBooks: LiveData<List<Book>> = repo.allFlow().asLiveData()
    val libraryBooks: LiveData<List<Book>> = repo.libraryFlow().asLiveData()

    // Load a single book by id (returns LiveData<Book?>)
    fun loadBook(id: Int): LiveData<Book?> = liveData {
        val book = repo.getById(id)
        emit(book)
    }
    // Add these methods inside BookViewModel class
    fun setInLibrary(book: com.example.novelrepo.data.Book, inLibrary: Boolean) {
        viewModelScope.launch {
            val updated = book.copy(inLibrary = inLibrary, isNewRelease = book.isNewRelease)
            repo.update(updated)
        }
    }
    fun toggleInLibrary(book: com.example.novelrepo.data.Book) {
        viewModelScope.launch {
            val updated = book.copy(inLibrary = !book.inLibrary, isNewRelease = book.isNewRelease)
            repo.update(updated)
        }
    }
    // Update book (suspend call wrapped in viewModelScope)
    fun updateBook(book: Book) {
        viewModelScope.launch {
            repo.update(book)
        }
    }

    // Delete book
    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repo.delete(book)
        }
    }

    // viewmodel/BookViewModel.kt
    fun createBook(book: Book) {
        viewModelScope.launch {
            repo.insert(book)
        }
    }

}
