
package com.example.placeKeeper.presentation.screens.categories


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import com.example.placeKeeper.utils.CategoryConstants
import com.example.placeKeeper.utils.ColorOption
import com.example.placeKeeper.utils.IconOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onNavigateBack: () -> Unit,
    onSaveCategory: (String, String, String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(CategoryConstants.AVAILABLE_COLORS[0].hex) }
    var selectedIcon by remember { mutableStateOf(CategoryConstants.AVAILABLE_ICONS[0].iconName) }
    var showNameError by remember { mutableStateOf(false) }

    val isFormValid = remember(categoryName) {
        categoryName.length in 3..30
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
                            if (isFormValid) {
                                onSaveCategory(categoryName, selectedColor, selectedIcon)
                                onNavigateBack()
                            } else {
                                showNameError = true
                            }
                        },
                        enabled = categoryName.isNotBlank()
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Category Name Input
            Column {
                OutlinedTextField(
                    value = categoryName,
                    onValueChange = {
                        categoryName = it
                        showNameError = false
                    },
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    isError = showNameError,
                    supportingText = if (showNameError) {
                        { Text("Name must be between 3 and 30 characters") }
                    } else null
                )
                Text(
                    text = "${categoryName.length}/30",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.End)
                )
            }

            // Color Selection
            Text(
                text = "Select Color",
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
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
                text = "Select Icon",
                style = MaterialTheme.typography.titleMedium
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(CategoryConstants.AVAILABLE_ICONS) { iconOption ->
                    IconSelectionButton(
                        iconOption = iconOption,
                        isSelected = iconOption.iconName == selectedIcon,
                        onClick = { selectedIcon = iconOption.iconName }
                    )
                }
            }

            // Preview
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Preview",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CategoryPreview(
                        name = categoryName.ifEmpty { "Category Name" },
                        color = selectedColor,
                        icon = CategoryConstants.AVAILABLE_ICONS.find { it.iconName == selectedIcon }?.imageVector
                            ?: Icons.Default.Place
                    )
                }
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