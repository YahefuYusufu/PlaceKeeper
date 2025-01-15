package com.example.placeKeeper.data.repository

import android.util.Log
import com.example.placeKeeper.data.dao.CategoryDao
import com.example.placeKeeper.data.mappers.toCategory
import com.example.placeKeeper.data.mappers.toCategoryEntity
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return try {
            categoryDao.getAllCategories()
                .map { entities ->
                    entities.map { it.toCategory() }
                }
                .catch { e ->
                    Log.e("CategoryRepository", "Error getting categories", e)
                    emit(emptyList())
                }
                .onEach { categories ->
                    Log.d("CategoryRepository", "Fetched ${categories.size} categories")
                }
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error setting up category flow", e)
            flow { emit(emptyList()) }
        }
    }

    override suspend fun getCategoryById(categoryId: Long): Category? {
        return try {
            categoryDao.getCategoryById(categoryId)?.toCategory()?.also {
                Log.d("CategoryRepository", "Retrieved category: ${it.name}")
            }
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error getting category by id: $categoryId", e)
            null
        }
    }

    override suspend fun insertCategory(category: Category): Long {
        return try {
            categoryDao.insertCategory(category.toCategoryEntity()).also { id ->
                Log.d("CategoryRepository", "Inserted category: ${category.name} with id: $id")
            }
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error inserting category: ${category.name}", e)
            throw e  // Re-throw to handle in ViewModel
        }
    }

    override suspend fun updateCategory(category: Category) {
        try {
            categoryDao.updateCategory(category.toCategoryEntity())
            Log.d("CategoryRepository", "Updated category: ${category.name}")
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error updating category: ${category.name}", e)
            throw e  // Re-throw to handle in ViewModel
        }
    }

    override suspend fun deleteCategory(category: Category) {
        try {
            categoryDao.deleteCategory(category.toCategoryEntity())
            Log.d("CategoryRepository", "Deleted category: ${category.name}")
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error deleting category: ${category.name}", e)
            throw e  // Re-throw to handle in ViewModel
        }
    }
}