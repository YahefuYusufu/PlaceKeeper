package com.example.placeKeeper.presentation.screens.addcategory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {
    private val addCategoryViewModel = "AddCategoryViewModel"

    private val _inputState = MutableStateFlow(CategoryInputState())
    val inputState = _inputState.asStateFlow()

    private val _uiState = MutableStateFlow<CategoryUiState>(CategoryUiState.Initial)
    val uiState = _uiState.asStateFlow()

    init {
        Log.d(addCategoryViewModel, "ViewModel initialized with initial state: ${_inputState.value}")
    }

    fun updateName(name: String) {
        Log.d(addCategoryViewModel, "Updating name: $name")
        _inputState.update { it.copy(name = name) }

        if (_uiState.value is CategoryUiState.Error) {
            Log.d(addCategoryViewModel, "Resetting error state after name update")
            _uiState.value = CategoryUiState.Initial
        }
    }

    fun updateColor(color: String) {
        Log.d(addCategoryViewModel, "Updating color: $color")
        _inputState.update { it.copy(color = color) }
    }

    fun updateIcon(iconName: String) {
        Log.d(addCategoryViewModel, "Updating icon: $iconName")
        _inputState.update { it.copy(iconName = iconName) }
    }

    fun addCategory() {
        val currentState = _inputState.value
        Log.d(addCategoryViewModel, "Attempting to add category with state: $currentState")

        // Validate name
        if (currentState.name.length !in 3..30) {
            Log.w(addCategoryViewModel, "Invalid name length: ${currentState.name.length}")
            _uiState.value = CategoryUiState.Error(
                "Name must be between 3 and 30 characters"
            )
            return
        }

        viewModelScope.launch {
            try {
                Log.d(addCategoryViewModel, "Setting loading state")
                _uiState.value = CategoryUiState.Loading

                val category = currentState.toCategory()
                Log.d(addCategoryViewModel, "Created category entity: $category")

                val id = repository.insertCategory(category)
                Log.d(addCategoryViewModel, "Category inserted with ID: $id")

                if (id > 0) {
                    Log.d(addCategoryViewModel, "Category successfully added with ID: $id")
                    _uiState.value = CategoryUiState.Success(id)
                    _inputState.update { it.copy(id = id) }
                } else {
                    Log.e(addCategoryViewModel, "Failed to add category - returned ID: $id")
                    _uiState.value = CategoryUiState.Error("Failed to add category")
                }
            } catch (e: Exception) {
                Log.e(addCategoryViewModel, "Error adding category", e)
                _uiState.value = CategoryUiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun resetState() {
        Log.d(addCategoryViewModel, "Resetting state to initial values")
        _inputState.value = CategoryInputState()
        _uiState.value = CategoryUiState.Initial
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(addCategoryViewModel, "ViewModel cleared")
    }

    // Helper function to log state changes
    private fun logStateChange(description: String, oldState: Any, newState: Any) {
        Log.d(addCategoryViewModel, "$description - Old: $oldState, New: $newState")
    }

    // Extension function to safely handle state updates with logging
    private fun <T : Any> MutableStateFlow<T>.updateWithLog(description: String, update: (T) -> T) {
        val oldState = value
        update(oldState).let { newState ->
            value = newState
            logStateChange(description, oldState, newState)
        }
    }

    companion object {
        private const val MAX_NAME_LENGTH = 30
        private const val MIN_NAME_LENGTH = 3
    }
}