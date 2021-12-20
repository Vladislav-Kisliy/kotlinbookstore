package com.example.kotlinbookstore.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "BOOK")
data class Book(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long,
    val name: String,
    val description: String?,
    val price: Double,
    val isAvailable: Boolean = true
)