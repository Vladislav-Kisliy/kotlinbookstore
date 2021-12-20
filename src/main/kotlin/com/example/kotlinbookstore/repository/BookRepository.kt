package com.example.kotlinbookstore.repository

import com.example.kotlinbookstore.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long>