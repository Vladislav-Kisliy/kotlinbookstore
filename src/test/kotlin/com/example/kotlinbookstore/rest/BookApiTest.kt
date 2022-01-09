package com.example.kotlinbookstore.rest

import com.example.kotlinbookstore.Constants.BOOK_DESCRIPTION
import com.example.kotlinbookstore.Constants.BOOK_NAME
import com.example.kotlinbookstore.Constants.BOOK_PRICE
import com.example.kotlinbookstore.model.Book
import com.example.kotlinbookstore.repository.BookRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

class BookApiTest {

    private var target: BookApi? = null
    private var bookRepository: BookRepository? = null

    @BeforeEach
    fun setUp() {
        bookRepository = Mockito.mock(BookRepository::class.java)
        target = BookApi(bookRepository!!)
    }

    @Test
    fun getAllBooks() {
        val books = listOf(Book(1, BOOK_NAME, BOOK_DESCRIPTION, BOOK_PRICE, true))
        Mockito.`when`(bookRepository!!.findAll()).thenReturn(books)

        val allBooks: ResponseEntity<List<Book>> = target!!.getAllBooks();

        assertThat(allBooks.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(allBooks.body!!.size).isEqualTo(1)

        assertThat(allBooks.body!!.get(0).name).isEqualTo(BOOK_NAME)
        assertThat(allBooks.body!!.get(0).description).isEqualTo(BOOK_DESCRIPTION)
    }

    @Test
    fun getBookById() {
        var book = Book(1, BOOK_NAME, BOOK_DESCRIPTION, BOOK_PRICE, true)
        Mockito.`when`(bookRepository!!.findById(1)).thenReturn(Optional.of(book))

        val allBooks: ResponseEntity<Book> = target!!.getBookById(1);

        assertThat(allBooks.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(allBooks.body!!.name).isEqualTo(BOOK_NAME)
        assertThat(allBooks.body!!.description).isEqualTo(BOOK_DESCRIPTION)
    }

}