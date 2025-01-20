package com.example.placeKeeper.presentation.screens.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Log.d("CategoryViewModel", "ViewModel initialized")
        loadCategories()
    }

    private fun loadCategories() {
        repository.getAllCategories()
            .onStart {
                _uiState.update { it.copy(isLoading = true) }
            }
            .catch { e ->
                Log.e("CategoryViewModel", "Error fetching categories", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
            .onEach { categories ->
                _uiState.update {
                    it.copy(
                        categories = categories,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }


    fun showDeleteConfirmation(category: Category) {
        _uiState.update { it.copy(categoryToDelete = category) }
    }

    fun dismissDeleteConfirmation() {
        _uiState.update { it.copy(categoryToDelete = null) }
    }

    fun deleteCategory() {
        val categoryToDelete = _uiState.value.categoryToDelete ?: return
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                repository.deleteCategory(categoryToDelete)
                _uiState.update { it.copy(categoryToDelete = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
                Log.e("CategoryViewModel", "Error deleting category", e)
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun toggleViewMode() {
        _uiState.update { it.copy(isGridView = !it.isGridView) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }


}