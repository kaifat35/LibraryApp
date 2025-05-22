package com.example.libraryapp.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.viewmodel.BokViewModel

@Composable
fun UpdateScreen(viewModel: BokViewModel, bookId: String?){

    var inputBook by remember { mutableStateOf("") }

    Column() {

        OutlinedTextField(
            value = inputBook,
            onValueChange ={
                enteredText -> inputBook = enteredText
            },
            label = { Text(text = "Обнови название книги") },
            placeholder = { Text(text = "Новое название книги")}
        )

        Button (onClick = {
            var newBook = BookEntity(bookId!!.toInt(), inputBook)
            viewModel.updateBook(newBook)
        }) {
            Text(text =  "Обновить книгу")

        }
    }
}