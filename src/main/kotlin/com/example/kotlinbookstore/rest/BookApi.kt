package com.example.kotlinbookstore.rest

import com.example.kotlinbookstore.model.Book
import com.example.kotlinbookstore.repository.BookRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/api")
class BookApi(private val bookRepository: BookRepository) {

    @GetMapping("/books")
    fun getAllBooks(): ResponseEntity<List<Book>> {
        val books = bookRepository.findAll()
        return when (books.isEmpty()) {
            true -> ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT)
            false -> ResponseEntity<List<Book>>(books, HttpStatus.OK)
        }
    }

    @GetMapping("/books/{id}")
    fun getBookById(@PathVariable("id") bookId: Long): ResponseEntity<Book> {
        val book = bookRepository.findById(bookId)
        if (book.isPresent) {
            return ResponseEntity<Book>(book.get(), HttpStatus.OK)
        }
        return ResponseEntity<Book>(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/books")
    fun addNewBook(@RequestBody book: Book, uri: UriComponentsBuilder): ResponseEntity<Book> {
        val persistedBook = bookRepository.save(book)
        val headers = HttpHeaders()
        headers.setLocation(
            uri.path("/books/{id}")
                .buildAndExpand(persistedBook.id)
                .toUri()
        );
        return ResponseEntity(headers, HttpStatus.CREATED)
    }

    @PutMapping("/books/{id}")
    fun updateBookById(@PathVariable("id") bookId: Long, @RequestBody book: Book): ResponseEntity<Book> {
        return bookRepository.findById(bookId).map { bookDetails ->
            val updatedBook: Book = bookDetails.copy(
                description = book.description,
                name = book.name,
                price = book.price,
                isAvailable = book.isAvailable
            )
            ResponseEntity(bookRepository.save(updatedBook), HttpStatus.OK)
        }.orElse(ResponseEntity<Book>(HttpStatus.INTERNAL_SERVER_ERROR))
    }

    @DeleteMapping("/books")
    fun deleteAllBooks(): ResponseEntity<Void> {
        bookRepository.deleteAll()
        return ResponseEntity<Void>(HttpStatus.OK)
    }

    @DeleteMapping("/books/{id}")
    fun removeBookById(@PathVariable("id") bookId: Long): ResponseEntity<Void> {
        bookRepository.deleteById(bookId)
        return ResponseEntity<Void>(HttpStatus.NO_CONTENT)
    }
}