package com.example.placeKeeper.presentation.screens.addcategory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.placeKeeper.domain.model.Category
import com.example.placeKeeper.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AddCategoryUiState>(AddCategoryUiState.Initial)
    val uiState: StateFlow<AddCategoryUiState> = _uiState.asStateFlow()

    fun addCategory(name: String, color: String, iconName: String) {
        if (!validateInputs(name, color, iconName)) {
            return
        }

        viewModelScope.launch {
            _uiState.value = AddCategoryUiState.Loading
            try {
                val newCategory = Category(
                    name = name.trim(),
                    color = color,
                    iconName = iconName,
                    createdAt = System.currentTimeMillis()
                )

                // Using the repository's insertCategory that returns Long
                val categoryId = repository.insertCategory(newCategory)

                if (categoryId > 0) {
                    Log.d("AddCategoryViewModel", "Category added with ID: $categoryId")
                    _uiState.value = AddCategoryUiState.Success
                } else {
                    _uiState.value = AddCategoryUiState.Error("Failed to add category")
                }

            } catch (e: Exception) {
                Log.e("AddCategoryViewModel", "Error adding category", e)
                _uiState.value = AddCategoryUiState.Error(
                    e.message ?: "Failed to add category"
                )
            }
        }
    }

    private fun validateInputs(name: String, color: String, iconName: String): Boolean {
        when {
            name.length < 3 || name.length > 30 -> {
                _uiState.value = AddCategoryUiState.Error(
                    "Name must be between 3 and 30 characters"
                )
                return false
            }
            color.isEmpty() -> {
                _uiState.value = AddCategoryUiState.Error("Please select a color")
                return false
            }
            iconName.isEmpty() -> {
                _uiState.value = AddCategoryUiState.Error("Please select an icon")
                return false
            }
        }
        return true
    }

    fun resetState() {
        _uiState.value = AddCategoryUiState.Initial
    }

    // Optional: Function to verify if a category was successfully added
    suspend fun getCategoryById(id: Long): Category? {
        return repository.getCategoryById(id)
    }
}

sealed class AddCategoryUiState {
    object Initial : AddCategoryUiState()
    object Loading : AddCategoryUiState()
    object Success : AddCategoryUiState()
    data class Error(val message: String) : AddCategoryUiState()
}