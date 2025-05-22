package com.example.libraryapp.repository

import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB

class Repository(val booksDB: BooksDB) {
    suspend fun addBookToRoom(bookEntity: BookEntity){
        booksDB.bookDao().addBook(bookEntity)
    }

    fun getAllBooks() = booksDB.bookDao().getAllBooks()

    suspend fun deleteBookFromRoom(bookEntity: BookEntity){
        booksDB.bookDao().deleteBook(bookEntity = bookEntity)
    }

    suspend fun updateBook(bookEntity: BookEntity){
        booksDB.bookDao().updateBook(bookEntity = bookEntity)
    }

}