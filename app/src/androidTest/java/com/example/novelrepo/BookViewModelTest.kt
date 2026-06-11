package com.example.novelrepo

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.novelrepo.data.AppDatabase
import com.example.novelrepo.data.Book
import com.example.novelrepo.repository.BookRepository
import com.example.novelrepo.viewmodel.BookViewModel
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookViewModelTest {

    private lateinit var repo: BookRepository
    private lateinit var viewModel: BookViewModel

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repo = BookRepository.getInstance(context)
        viewModel = BookViewModel(context.applicationContext as Application)
    }

    @Test
    fun updateBook_updatesDao() = runBlocking {
        val uniqueId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        val book = Book(
            id = uniqueId,
            title = "A",
            author = "B",
            description = "C",
            genres = "X",
            pages = 100,
            readTime = "1h",
            coverResName = "res",
            inLibrary = false
        )

        repo.insert(book)

        val updated = book.copy(title = "Updated")
        repo.update(updated)

        val result = repo.getById(uniqueId)
        Assert.assertEquals("Updated", result?.title)
    }
}

