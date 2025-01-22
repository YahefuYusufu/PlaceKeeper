package com.example.placeKeeper.presentation.screens.addplace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Add scrolling capability
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Map at the top
            LocationSection(
                state = inputState,
                onLocationEvent = viewModel::onLocationEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Name and Category in the same row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PlaceNameInput(
                    name = inputState.name,
                    onNameChange = viewModel::updateName,
                    isError = uiState is AddPlaceUiState.Error,
                    errorMessage = (uiState as? AddPlaceUiState.Error)?.message,
                    modifier = Modifier.weight(1f)
                )

                CategorySelector(
                    categories = categories,
                    selectedCategoryId = inputState.categoryId,
                    onCategorySelected = viewModel::updateCategory,
                    modifier = Modifier.weight(1f)
                )
            }
//
            // Rating
            RatingSelector(
                rating = inputState.rating,
                onRatingChange = viewModel::updateRating,
                modifier = Modifier.fillMaxWidth()
            )

            // Description
            PlaceDescription(
                description = inputState.description,
                onDescriptionChange = viewModel::updateDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Give remaining space to description
            )

            // Add some bottom padding to ensure content isn't cut off
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}