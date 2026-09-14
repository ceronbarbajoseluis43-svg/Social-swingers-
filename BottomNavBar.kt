package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.DynamicFeed
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkOutline
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryRose
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.NavigationTab

data class NavItem(
    val tab: NavigationTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun BottomNavBar(
    selectedTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    val items = listOf(
        NavItem(
            tab = NavigationTab.FEED,
            label = "Muro",
            selectedIcon = Icons.Filled.DynamicFeed,
            unselectedIcon = Icons.Outlined.DynamicFeed
        ),
        NavItem(
            tab = NavigationTab.EVENTS,
            label = "Fiestas",
            selectedIcon = Icons.Filled.Celebration,
            unselectedIcon = Icons.Outlined.Celebration,
            badgeCount = 2
        ),
        NavItem(
            tab = NavigationTab.GROUPS,
            label = "Grupos",
            selectedIcon = Icons.Filled.Groups,
            unselectedIcon = Icons.Outlined.Groups
        ),
        NavItem(
            tab = NavigationTab.MESSAGES,
            label = "Chats",
            selectedIcon = Icons.Filled.ChatBubble,
            unselectedIcon = Icons.Outlined.ChatBubbleOutline,
            badgeCount = 1
        ),
        NavItem(
            tab = NavigationTab.PROFILE,
            label = "Perfil",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    Surface(
        color = DarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedTab == item.tab

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .testTag("nav_tab_${item.tab.name.lowercase()}")
                ) {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge(
                                    containerColor = PrimaryRose,
                                    contentColor = Color.White
                                ) {
                                    Text("${item.badgeCount}", fontSize = 8.sp)
                                }
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    color = if (isSelected) PrimaryRose.copy(alpha = 0.15f) else Color.Transparent,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                tint = if (isSelected) PrimaryRose else TextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) PrimaryRose else TextMuted
                    )
                }
            }
        }
    }
}
