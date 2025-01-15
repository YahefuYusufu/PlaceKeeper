package com.example.placeKeeper.presentation.screens.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()


    // Get all categories as a StateFlow
    val categories: StateFlow<List<Category>> = repository
        .getAllCategories()
        .catch { e ->
            _error.value = e.message
            Log.e("CategoryViewModel", "Error fetching categories", e)
            emit((emptyList()))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Delete category with error handling
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteCategory(category)
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("CategoryViewModel", "Error deleting category", e)
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Clear error state
    fun clearError() {
        _error.value = null
    }

    init {
        Log.d("CategoryViewModel", "ViewModel initialized")
    }

}