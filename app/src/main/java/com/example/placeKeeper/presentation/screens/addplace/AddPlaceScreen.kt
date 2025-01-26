package com.example.placeKeeper.presentation.screens.addplace

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.DisposableEffect
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
    onPlaceSaved: (Long) -> Unit,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {
    val inputState by viewModel.inputState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(uiState) {
        Log.d("AddPlaceScreen", "Current uiState: $uiState")
        when (uiState) {
            is AddPlaceUiState.Success -> {
                Log.d("AddPlaceScreen", "Success state detected, navigating back")
                viewModel.resetState()
            }
            is AddPlaceUiState.Loading -> {
                Log.d("AddPlaceScreen", "Loading state")
            }
            is AddPlaceUiState.Error -> {
                Log.d("AddPlaceScreen", "Error state: ${(uiState as AddPlaceUiState.Error).message}")
            }
            is AddPlaceUiState.Initial -> {
                Log.d("AddPlaceScreen", "Initial state")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("AddPlaceScreen", "Screen disposed")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Place") },
                navigationIcon = {
                    IconButton(onClick = {
                        onPlaceSaved(-1)
                    }) {
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Map at the top
            LocationSection(
                state = inputState,
                onLocationEvent = viewModel::onLocationEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
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

             RatingSelector(
                rating = inputState.rating,
                onRatingChange = viewModel::updateRating,
                modifier = Modifier.fillMaxWidth()
            )

            // Description
            PlaceDescription(
                description = inputState.description,
                onDescriptionChange = viewModel::updateDescription,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}