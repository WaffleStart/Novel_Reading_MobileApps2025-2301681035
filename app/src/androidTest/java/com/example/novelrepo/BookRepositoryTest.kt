package com.example.novelrepo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.novelrepo.data.AppDatabase
import com.example.novelrepo.data.Book
import com.example.novelrepo.repository.BookRepository
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repo: BookRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repo = BookRepository.getInstance(context)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun updateBook_updatesDao() = runBlocking {
        val book = Book(999, "A", "B", "C", "X", 100, "1h", "res", false)
        repo.insert(book)

        val updated = book.copy(title = "Updated")
        repo.update(updated)

        val result = repo.getById(999)
        Assert.assertEquals("Updated", result?.title)
    }

}
