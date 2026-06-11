package com.example.novelrepo

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.novelrepo.data.AppDatabase
import com.example.novelrepo.data.Book
import com.example.novelrepo.data.BookDao
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: BookDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.bookDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndReadBook() = runBlocking {
        val book = Book(
            id = 10,
            title = "Test",
            author = "Tester",
            description = "Desc",
            genres = "Fantasy",
            pages = 100,
            readTime = "1h",
            coverResName = "test",
            inLibrary = true
        )

        dao.insert(book)
        val result = dao.getById(10)

        Assert.assertEquals("Test", result?.title)
        Assert.assertEquals(true, result?.inLibrary)
    }
}
