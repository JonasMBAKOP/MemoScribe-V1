package com.example.memoscribe.model

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)