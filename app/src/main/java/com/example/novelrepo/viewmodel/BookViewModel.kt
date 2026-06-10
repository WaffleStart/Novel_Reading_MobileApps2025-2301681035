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
    fun toggleInLibrary(book: Book) {
        val updated = book.copy(inLibrary = !book.inLibrary)
        viewModelScope.launch {
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
}
