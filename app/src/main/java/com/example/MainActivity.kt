package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.ui.screens.ECatalogScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.InfoScreen
import com.example.ui.screens.ScanResultScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.ScanViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as LabelScanApplication
        val viewModel = ViewModelProvider(
            this,
            ScanViewModel.Factory(app.repository)
        )[ScanViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: ScanViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val selectedScan by viewModel.selectedScan.collectAsState()

    if (selectedScan != null) {
        ScanResultScreen(
            scan = selectedScan!!,
            viewModel = viewModel
        )
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (currentTab) {
                                AppTab.SCANNER -> "LabelScan • Анализ банок"
                                AppTab.HISTORY -> "История сканирований"
                                AppTab.CATALOG_E -> "Справочник E-добавок"
                                AppTab.HELP -> "О сканере"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentTab == AppTab.SCANNER,
                        onClick = { viewModel.selectTab(AppTab.SCANNER) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.CameraAlt,
                                contentDescription = "Сканер"
                            )
                        },
                        label = { Text("Сканер") },
                        modifier = Modifier.testTag("nav_tab_scanner")
                    )
                    NavigationBarItem(
                        selected = currentTab == AppTab.HISTORY,
                        onClick = { viewModel.selectTab(AppTab.HISTORY) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.History,
                                contentDescription = "История"
                            )
                        },
                        label = { Text("История") },
                        modifier = Modifier.testTag("nav_tab_history")
                    )
                    NavigationBarItem(
                        selected = currentTab == AppTab.CATALOG_E,
                        onClick = { viewModel.selectTab(AppTab.CATALOG_E) },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = "Е-добавки"
                            )
                        },
                        label = { Text("Е-база") },
                        modifier = Modifier.testTag("nav_tab_e_catalog")
                    )
                    NavigationBarItem(
                        selected = currentTab == AppTab.HELP,
                        onClick = { viewModel.selectTab(AppTab.HELP) },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Инфо"
                            )
                        },
                        label = { Text("Инфо") },
                        modifier = Modifier.testTag("nav_tab_info")
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    AppTab.SCANNER -> HomeScreen(viewModel = viewModel)
                    AppTab.HISTORY -> HistoryScreen(viewModel = viewModel)
                    AppTab.CATALOG_E -> ECatalogScreen(viewModel = viewModel)
                    AppTab.HELP -> InfoScreen()
                }
            }
        }
    }
}
