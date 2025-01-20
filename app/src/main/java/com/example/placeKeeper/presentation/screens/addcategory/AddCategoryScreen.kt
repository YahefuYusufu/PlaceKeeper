package com.example.placeKeeper.presentation.screens.addcategory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.presentation.screens.addcategory.components.CategoryNameInput
import com.example.placeKeeper.presentation.screens.addcategory.components.ColorSelectionSection
import com.example.placeKeeper.presentation.screens.addcategory.components.IconSelectionSection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddCategoryViewModel = hiltViewModel()
) {
    val inputState by viewModel.inputState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is CategoryUiState.Success -> onNavigateBack()
            else -> {} // Handle other states if needed
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Category") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.addCategory() },
                        enabled = inputState.name.isNotBlank() &&
                                uiState !is CategoryUiState.Loading
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name Input
                CategoryNameInput(
                    name = inputState.name,
                    onNameChange = viewModel::updateName,
                    isError = uiState is CategoryUiState.Error,
                    errorMessage = (uiState as? CategoryUiState.Error)?.message
                )

                // Color Selection
                ColorSelectionSection(
                    selectedColor = inputState.color,
                    onColorSelected = viewModel::updateColor
                )

                // Icon Selection
                IconSelectionSection(
                    selectedIcon = inputState.iconName,
                    onIconSelected = viewModel::updateIcon
                )

            }

            // Loading Indicator
            if (uiState is CategoryUiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}











