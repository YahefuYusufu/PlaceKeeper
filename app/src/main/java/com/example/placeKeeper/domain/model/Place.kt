package com.example.placeKeeper.domain.model

data class Place (
    val id: Long = 0,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val categoryId: Long,
    val description: String = "",
    val rating: Float = 0f,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)