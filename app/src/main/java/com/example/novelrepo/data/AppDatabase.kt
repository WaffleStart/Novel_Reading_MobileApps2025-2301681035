package com.example.novelrepo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "novelrepo.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Prepopulate on IO
                        val scope = CoroutineScope(Dispatchers.IO)
                        scope.launch {
                            val dao = getInstance(context).bookDao()
                            dao.insertAll(
                                Book(
                                    id = 1,
                                    title = "The Hobbit",
                                    author = "J. R. R. Tolkien",
                                    description = "A brief glimpse of the story that draws you in.",
                                    genres = "Fantasy,Adventure",
                                    pages = 320,
                                    readTime = "12h 45m",
                                    coverResName = "hobbit",
                                    inLibrary = true,
                                    percentRead = 62,
                                    currentChapter = 12,
                                    rating = 4.8f,
                                    ratingCount = 12456
                                ),
                                Book(
                                    id = 2,
                                    title = "Last Light",
                                    author = "Terri Blackstock",
                                    description = "A tense, atmospheric tale of survival and restoration in a city on the edge.",
                                    genres = "Thriller,Suspense",
                                    pages = 384,
                                    readTime = "10h 30m",
                                    coverResName = "last_light",
                                    inLibrary = false,
                                    percentRead = 0,
                                    currentChapter = 0,
                                    rating = 4.5f,
                                    ratingCount = 842
                                ),
                                Book(
                                    id = 3,
                                    title = "The Silent Grove",
                                    author = "Nora K. Strange",
                                    description = "A quiet, mysterious novel about a grove that keeps its secrets close.",
                                    genres = "Mystery,Literary",
                                    pages = 280,
                                    readTime = "8h 15m",
                                    coverResName = "silent_grove",
                                    inLibrary = false,
                                    percentRead = 0,
                                    currentChapter = 3,
                                    rating = 4.2f,
                                    ratingCount = 412
                                ),
                                Book(
                                    id = 4,
                                    title = "Whispers",
                                    author = "Alec Worley",
                                    description = "A dark, lore-rich short novel set in a grim universe of faith and war.",
                                    genres = "Sci-Fi,Dark Fantasy",
                                    pages = 200,
                                    readTime = "6h 40m",
                                    coverResName = "whispers",
                                    inLibrary = false,
                                    percentRead = 0,
                                    currentChapter = 0,
                                    rating = 4.6f,
                                    ratingCount = 129
                                )
                            )

                        }
                    }
                })
                .build()
        }
    }
}
