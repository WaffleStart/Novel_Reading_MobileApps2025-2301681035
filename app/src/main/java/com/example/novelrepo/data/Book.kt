    package com.example.novelrepo.data

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "books")
    data class Book(

        @PrimaryKey
        val id: Int,

        val title: String,

        val author: String,

        val genre: String,

        val description: String,

        val content: String,

        val imageRes: Int,

        var inLibrary: Boolean = false,

        var isCompleted: Boolean = false
    )