package com.example.libraryapp.repository

import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB

class Repository(val booksDB: BooksDB) {
    suspend fun addBookToRoom(bookEntity: BookEntity){
        booksDB.bookDao().addBook(bookEntity)
    }
}