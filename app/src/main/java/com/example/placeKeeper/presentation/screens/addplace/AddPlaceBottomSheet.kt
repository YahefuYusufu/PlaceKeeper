package com.example.placeKeeper.presentation.screens.addplace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun AddPlaceBottomSheet(
    onDismiss: () -> Unit,
    viewModel: AddPlaceViewModel = hiltViewModel()
) {
    val inputState by viewModel.inputState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val categories by viewModel.categories.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top bar with title and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Place",
                    style = MaterialTheme.typography.titleLarge
                )
                TextButton(
                    onClick = viewModel::addPlace,
                    enabled = inputState.name.isNotBlank() &&
                            uiState !is AddPlaceUiState.Loading
                ) {
                    Text("Save")
                }
            }

            // Map
            LocationSection(
                state = inputState,
                onLocationEvent = viewModel::onLocationEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // Name and Category
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

            PlaceDescription(
                description = inputState.description,
                onDescriptionChange = viewModel::updateDescription,
                modifier = Modifier.fillMaxWidth()
            )

            // Add extra padding at the bottom for better UX
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
