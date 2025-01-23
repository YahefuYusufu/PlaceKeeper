package com.example.placeKeeper.presentation.screens.addplace.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RatingSelector(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            "Rating",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) { index ->
                val starFilled = index < rating
                IconButton(
                    onClick = { onRatingChange(index + 1f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (starFilled) {
                            Icons.Default.Star
                        } else {
                            Icons.Default.StarBorder
                        },
                        contentDescription = "Rate ${index + 1}",
                        tint = if (starFilled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}