 package com.example.libraryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.repository.Repository
import com.example.libraryapp.room.BookEntity
import com.example.libraryapp.room.BooksDB
import com.example.libraryapp.screens.UpdateScreen
import com.example.libraryapp.ui.theme.LibraryAppTheme
import com.example.libraryapp.viewmodel.BookViewModel

 @Suppress("UNCHECKED_CAST")
 class MainActivity : ComponentActivity() {
     private val viewModel: BookViewModel by viewModels {
         object : ViewModelProvider.Factory {
             override fun <T : ViewModel> create(modelClass: Class<T>): T {
                 val db = BooksDB.getInstance(this@MainActivity)
                 val repository = Repository(db)
                 return BookViewModel(repository) as T
             }
         }
     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()
         setContent {
             LibraryAppTheme {
                 Surface(
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colorScheme.background
                 ) {
                     val navController = rememberNavController()

                     NavHost(
                         navController = navController,
                         startDestination = "MainScreen"
                     ) {
                         composable("MainScreen") {
                             MainScreen(viewModel = viewModel, navController)
                         }
                         composable("UpdateScreen/{bookId}") {
                             UpdateScreen(
                                 viewModel = viewModel,
                                 bookId = it.arguments?.getString("bookId")
                             )
                         }
                     }
                 }
             }
         }
     }
 }

 @Composable
 fun MainScreen(viewModel: BookViewModel, navController: NavHostController){

     var inputBook by remember { mutableStateOf("") }

     Column (modifier = Modifier.padding(top = 32.dp,
         start = 6.dp, end = 6.dp),
         horizontalAlignment = Alignment.CenterHorizontally){

         Text(text = "Добавление книги в ROOM DB", fontSize = 22.sp)
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
             },
             colors = ButtonDefaults.buttonColors(Color.Blue)
         ) {
             Text(text = "Вставьте книгу в БД")
         }

         BooksList(viewModel = viewModel, navController)
     }
 }

 @Composable
 fun BookCard(viewModel: BookViewModel, book: BookEntity, navController: NavHostController){

     Card (modifier = Modifier
         .padding(8.dp)
         .fillMaxWidth()
     ){
         Row(verticalAlignment = Alignment.CenterVertically) {

             Text(text = ""+ book.id, fontSize = 24.sp,
                 modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                 color = Color.Blue)

             Text(text =  book.title, fontSize = 24.sp,
                 modifier = Modifier.fillMaxSize(0.7f),
                 color = Color.Black)
             Row(
                 horizontalArrangement = Arrangement.End
             ) {

                 IconButton(onClick = { viewModel.deleteBook(book = book) }) {
                     Icon(
                         imageVector = Icons.Default.Delete,
                         contentDescription = "Удалить"
                     )
                 }

                 IconButton(onClick = {
                     navController.navigate("UpdateScreen/${book.id}")
                 }) {
                     Icon(
                         imageVector = Icons.Default.Edit,
                         contentDescription = "Редактировать"
                     )
                 }
             }
         }
     }
 }

 @Composable
 fun BooksList(viewModel: BookViewModel, navController: NavHostController){
     val books by viewModel.books.collectAsState(initial = emptyList())

     Column(Modifier.padding(16.dp)) {
         Text(text = "Моя библиотека:", fontSize = 24.sp, color = Color.Red)

         LazyColumn {
             items(items = books) { item ->
                 BookCard(
                     viewModel = viewModel,
                     book = item,
                     navController
                 )
             }
         }
     }
 }

