package com.example.placeKeeper.presentation.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.placeKeeper.presentation.navigation.NavigationItem
import com.example.placeKeeper.utils.NavigationUtils.navigateToTab

@Composable
fun PlaceKeeperBottomNavigation(
    navController: NavHostController,
    items: List<NavigationItem>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(bottom = 4.dp)
            .background(MaterialTheme.colorScheme.primary),

        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        // First two items
        items.take(2).forEach { item ->
            NavItem(
                item = item,
                selected = currentRoute == item.route,
                onItemClick = { navController.navigateToTab(item.route) }
            )
        }

        // Empty space for FAB
        Spacer(modifier = Modifier.width(56.dp))

        // Last two items
        items.takeLast(2).forEach { item ->
            NavItem(
                item = item,
                selected = currentRoute == item.route,
                onItemClick = { navController.navigateToTab(item.route) }
            )
        }
    }
}

@Composable
private fun NavItem(
    item: NavigationItem,
    selected: Boolean,
    onItemClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .clickable(onClick = onItemClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            modifier = Modifier.size(28.dp),
            tint = if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}