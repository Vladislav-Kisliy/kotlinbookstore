package com.example.kotlinbookstore.rest

import com.example.kotlinbookstore.Constants
import com.example.kotlinbookstore.Constants.BOOK_PRICE
import com.example.kotlinbookstore.model.Book
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookApiIntegrationTest(@Autowired val restTemplate: TestRestTemplate) {

    @Test
    fun `Smoke test`() {
        val entity = restTemplate.getForEntity<String>("/api")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `Create, read`() {
        val book = Book(id = 1, name = Constants.BOOK_NAME, description = Constants.BOOK_DESCRIPTION, price = BOOK_PRICE)
        restTemplate.postForEntity("/api/books", book, Book::class.java)
        val entity = restTemplate.getForEntity<String>("/api/books")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(Constants.BOOK_NAME, Constants.BOOK_DESCRIPTION)
    }

    @Test
    fun `Create, read, update, read`() {
        val book = Book(id = 1, name = Constants.BOOK_NAME, description = Constants.BOOK_DESCRIPTION, price = BOOK_PRICE)
        val responseEntity: ResponseEntity<Book> = restTemplate.postForEntity("/api/books", book, Book::class.java)

        val entity = restTemplate.getForEntity<List<Book>>("/api/books")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body!!.size).isEqualTo(1)

        val savedBook = restTemplate.getForEntity<Book>(responseEntity.headers.get("Location")!!.get(0))

        val id: Long = savedBook.body!!.id
        val updatedBook =
            Book(id = id, name = Constants.BOOK_NEW_NAME, description = Constants.BOOK_DESCRIPTION, price = BOOK_PRICE)
        restTemplate.put("/api/books/${id}", updatedBook, Book::class.java)

        val updatedEntity = restTemplate.getForEntity<List<Book>>("/api/books")
        assertThat(updatedEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(updatedEntity.body!!.size).isEqualTo(1)

        val updatedRestBook = restTemplate.getForEntity<Book>(responseEntity.headers.get("Location")!!.get(0)).body!!
        assertThat(updatedRestBook.name).isEqualTo(Constants.BOOK_NEW_NAME)
        assertThat(updatedRestBook.description).isEqualTo(Constants.BOOK_DESCRIPTION)
    }

    @Test
    fun `Delete all`() {
        val book = Book(id = 1, name = Constants.BOOK_NAME, description = Constants.BOOK_DESCRIPTION, price = BOOK_PRICE)
        restTemplate.postForEntity("/api/books", book, Book::class.java)
        val entity = restTemplate.getForEntity<String>("/api/books")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(Constants.BOOK_NAME, Constants.BOOK_DESCRIPTION)

        restTemplate.delete("/api/books")
        val existedEntity = restTemplate.getForEntity<String>("/api/books")
        assertThat(existedEntity.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `Delete by id`() {
        val book = Book(id = 1, name = Constants.BOOK_NAME, description = Constants.BOOK_DESCRIPTION, price = BOOK_PRICE)
        restTemplate.postForEntity("/api/books", book, Book::class.java)
        val entity = restTemplate.getForEntity<String>("/api/books")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains(Constants.BOOK_NAME, Constants.BOOK_DESCRIPTION)

        restTemplate.delete("/api/books/1")
        val existedEntity = restTemplate.getForEntity<String>("/api/books")
        assertThat(existedEntity.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @AfterEach
    fun teardown() {
        restTemplate.delete("/api/books")
    }

}