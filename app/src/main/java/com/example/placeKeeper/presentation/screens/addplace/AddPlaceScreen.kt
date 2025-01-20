package com.example.placeKeeper.presentation.screens.addplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.presentation.screens.addplace.components.CategorySelector
import com.example.placeKeeper.presentation.screens.addplace.components.LocationSection
import com.example.placeKeeper.presentation.screens.addplace.components.PlaceDescription
import com.example.placeKeeper.presentation.screens.addplace.components.PlaceNameInput
import com.example.placeKeeper.presentation.screens.addplace.components.RatingSelector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {
    val inputState by viewModel.inputState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(uiState) {
        when (uiState) {
            is AddPlaceUiState.Success -> onNavigateBack()
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Place") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = viewModel::addPlace,
                        enabled = inputState.name.isNotBlank() &&
                                uiState !is AddPlaceUiState.Loading
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
                PlaceNameInput(
                    name = inputState.name,
                    onNameChange = viewModel::updateName,
                    isError = uiState is AddPlaceUiState.Error,
                    errorMessage = (uiState as? AddPlaceUiState.Error)?.message
                )

                CategorySelector(
                    categories = categories,
                    selectedCategoryId = inputState.categoryId,
                    onCategorySelected = viewModel::updateCategory
                )

                LocationSection(
                    latitude = inputState.latitude,
                    longitude = inputState.longitude,
                    onLocationSelected = viewModel::updateLocation
                )

                PlaceDescription(
                    description = inputState.description,
                    onDescriptionChange = viewModel::updateDescription
                )

                RatingSelector(
                    rating = inputState.rating,
                    onRatingChange = viewModel::updateRating
                )
            }

            // Loading indicator
            if (uiState is AddPlaceUiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}