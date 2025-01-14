package com.example.placeKeeper.data.repository

import com.example.placeKeeper.data.dao.CategoryDao
import com.example.placeKeeper.data.mappers.toCategory
import com.example.placeKeeper.data.mappers.toCategoryEntity
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toCategory() }
        }
    }

    override suspend fun getCategoryById(categoryId: Long): Category? {
        return categoryDao.getCategoryById(categoryId)?.toCategory()
    }

    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category.toCategoryEntity())
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toCategoryEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toCategoryEntity())
    }
}