package com.example.placeKeeper.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val color: String, // Hex color code
    val iconName: String,
    val createdAt: Long = System.currentTimeMillis()
)