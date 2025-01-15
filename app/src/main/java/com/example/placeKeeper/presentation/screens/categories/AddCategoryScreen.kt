
package com.example.placeKeeper.presentation.screens.categories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.placeKeeper.utils.CategoryConstants
import com.example.placeKeeper.utils.ColorOption
import com.example.placeKeeper.utils.IconOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddCategoryViewModel = hiltViewModel()
) {
    var categoryName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(CategoryConstants.AVAILABLE_COLORS[0].hex) }
    var selectedIcon by remember { mutableStateOf(CategoryConstants.AVAILABLE_ICONS[0].iconName) }
    var showNameError by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Handle UI state changes
    LaunchedEffect(uiState) {
        when (uiState) {
            is AddCategoryUiState.Success -> {
                onNavigateBack()
            }
            is AddCategoryUiState.Error -> {
                showNameError = true
            }
            else -> { /* no-op */ }
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
                        onClick = {
                            viewModel.addCategory(
                                name = categoryName,
                                color = selectedColor,
                                iconName = selectedIcon
                            )
                        },
                        enabled = categoryName.isNotBlank() &&
                                uiState !is AddCategoryUiState.Loading
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
                // Category Name Input
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                        showNameError = false
                    },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = showNameError,
                    supportingText = if (showNameError) {
                        { Text("Name must be between 3 and 30 characters") }
                    } else {
                        { Text("${categoryName.length}/30") }
                    },
                    singleLine = true
                )

                // Color Selection
                Text(
                    "Select Color",
                    style = MaterialTheme.typography.titleMedium
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CategoryConstants.AVAILABLE_COLORS) { colorOption ->
                        ColorButton(
                            colorOption = colorOption,
                            isSelected = colorOption.hex == selectedColor,
                            onClick = { selectedColor = colorOption.hex }
                        )
                    }
                }

                // Icon Selection
                Text(
                    "Select Icon",
                    style = MaterialTheme.typography.titleMedium
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(CategoryConstants.AVAILABLE_ICONS) { iconOption ->
                        IconButton(
                            onClick = { selectedIcon = iconOption.iconName },
                            modifier = Modifier
                                .padding(4.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (selectedIcon == iconOption.iconName)
                                        MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline,
                                    shape = MaterialTheme.shapes.small
                                )
                        ) {
                            Icon(
                                imageVector = iconOption.imageVector,
                                contentDescription = iconOption.displayName,
                                tint = if (selectedIcon == iconOption.iconName)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Preview
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Preview",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CategoryPreview(
                            name = categoryName.ifEmpty { "Category Name" },
                            color = selectedColor,
                            icon = CategoryConstants.AVAILABLE_ICONS.find {
                                it.iconName == selectedIcon
                            }?.imageVector ?: Icons.Default.Place
                        )
                    }
                }
            }

            // Loading Indicator
            if (uiState is AddCategoryUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun ColorButton(
    colorOption: ColorOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    val buttonColor = remember(colorOption.hex) {
        try {
            Color(android.graphics.Color.parseColor(colorOption.hex))
        } catch (e: IllegalArgumentException) {
            defaultColor
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            color = buttonColor,
            shape = MaterialTheme.shapes.small,
            border = if (isSelected) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
            } else null,
            modifier = Modifier.size(48.dp)
        ) { }
        if (isSelected) {
            Text(
                text = colorOption.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun IconSelectionButton(
    iconOption: IconOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            1.dp,
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline
        ),
        modifier = Modifier.size(48.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconOption.imageVector,
                contentDescription = iconOption.displayName,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CategoryPreview(
    name: String,
    color: String,
    icon: ImageVector
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    val iconTint = remember(color) {
        try {
            Color(android.graphics.Color.parseColor(color))
        } catch (e: IllegalArgumentException) {
            defaultColor
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium
        )
    }
}