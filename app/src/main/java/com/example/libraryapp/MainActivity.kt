 package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB
import com.example.libraryapp.ui.theme.LibraryAppTheme
import com.example.libraryapp.viewmodel.BokViewModel

 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibraryAppTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    val mContext = LocalContext.current
                    val db = BooksDB.getInstance(mContext)
                    val repository = Repository(db)
                    var myViewModel = BokViewModel(repository = repository )

                    MainScreen(myViewModel)
                }
            }
        }
    }
}
 @Composable
 fun MainScreen(viewModel: BokViewModel){

     var inputBook by remember { mutableStateOf("") }

     Column (modifier = Modifier.padding(top = 32.dp),
         horizontalAlignment = Alignment.CenterHorizontally){
         OutlinedTextField(
             value = inputBook,
             onValueChange = {
                 enteredText -> inputBook = enteredText
             },
             label = {Text(text =  "Название книги")},
             placeholder = {"Введите название вашей книги"}
         )

         Button(
             onClick = {
                 viewModel.addBook(BookEntity(0, inputBook))
             }
         ) {
             Text(text = "Вставьте книгу в БД")
         }

         BooksList(viewModel = viewModel)
     }
 }

 @Composable
 fun BookCard(viewModel: BokViewModel, book: BookEntity){

     Card (modifier = Modifier
         .padding(8.dp)
         .fillMaxWidth()
     ){
         Row {
             Text(text = ""+ book.id, fontSize = 24.sp,
                 modifier = Modifier.padding(start = 4.dp, end = 4.dp))

             Text(text =  book.title, fontSize = 24.sp)

             IconButton(onClick = {viewModel.deleteBook(book = book)}) {
                 Icon(imageVector = Icons.Default.Delete,
                     contentDescription = "Delete")
             }


         }
     }
 }

 @Composable
 fun BooksList(viewModel: BokViewModel){
     val books by viewModel.books.collectAsState(initial = emptyList())

     LazyColumn {
         items(items = books){
             item -> BookCard(
                 viewModel = viewModel,
                 book = item
             )
         }
     }

 }

