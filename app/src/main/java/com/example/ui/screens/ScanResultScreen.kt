package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.EAdditiveItem
import com.example.data.model.IngredientItem
import com.example.data.model.ProductScanEntity
import com.example.data.model.SafetyLevel
import com.example.ui.components.EAdditiveCard
import com.example.ui.components.HealthScoreBadge
import com.example.ui.components.IngredientCard
import com.example.ui.components.MacroProgressBar
import com.example.ui.theme.SafetyDangerRed
import com.example.ui.theme.SafetyDangerRedBg
import com.example.ui.theme.SafetySafeGreen
import com.example.ui.theme.SafetySafeGreenBg
import com.example.ui.theme.SafetyWarningOrange
import com.example.ui.theme.SafetyWarningOrangeBg
import com.example.ui.viewmodel.ScanViewModel
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScanResultScreen(
    scan: ProductScanEntity,
    viewModel: ScanViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var copiedToClipboard by remember { mutableStateOf(false) }

    val showTotalPortion by viewModel.showTotalPortion.collectAsState()
    val qaList by viewModel.qaList.collectAsState()
    val isAnswering by viewModel.isAnswering.collectAsState()

    var selectedSubTab by remember { mutableIntStateOf(0) }
    var ingredientFilter by remember { mutableStateOf("Все") }
    var userQuestionText by remember { mutableStateOf("") }

    // Parse ingredients from JSON
    val ingredients = remember(scan.ingredientsDetailedJson) {
        val list = mutableListOf<IngredientItem>()
        try {
            val array = JSONArray(scan.ingredientsDetailedJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val safetyStr = obj.optString("safety", "SAFE")
                val safety = when (safetyStr.uppercase()) {
                    "DANGER" -> SafetyLevel.DANGER
                    "CAUTION" -> SafetyLevel.CAUTION
                    else -> SafetyLevel.SAFE
                }
                list.add(
                    IngredientItem(
                        name = obj.optString("name", "Ингредиент"),
                        category = obj.optString("category", "Основной"),
                        safety = safety,
                        explanation = obj.optString("explanation", ""),
                        amountOrPercentage = if (obj.has("amountOrPercentage") && !obj.isNull("amountOrPercentage")) obj.optString("amountOrPercentage") else null
                    )
                )
            }
        } catch (_: Exception) {}
        list
    }

    // Parse E-numbers from JSON
    val eNumbers = remember(scan.eNumbersJson) {
        val list = mutableListOf<EAdditiveItem>()
        try {
            val array = JSONArray(scan.eNumbersJson)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val safetyStr = obj.optString("danger", "SAFE")
                val safety = when (safetyStr.uppercase()) {
                    "DANGER" -> SafetyLevel.DANGER
                    "CAUTION" -> SafetyLevel.CAUTION
                    else -> SafetyLevel.SAFE
                }
                list.add(
                    EAdditiveItem(
                        code = obj.optString("code", "E"),
                        name = obj.optString("name", "Добавка"),
                        danger = safety,
                        purpose = obj.optString("purpose", "Пищевая добавка"),
                        description = obj.optString("description", ""),
                        sideEffects = obj.optString("sideEffects", "Без побочных эффектов")
                    )
                )
            }
        } catch (_: Exception) {}
        list
    }

    // Parse allergens & dietary badges
    val allergens = remember(scan.allergensJson) {
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(scan.allergensJson)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (_: Exception) {}
        list
    }

    val dietaryBadges = remember(scan.dietaryBadgesJson) {
        val list = mutableListOf<String>()
        try {
            val array = JSONArray(scan.dietaryBadgesJson)
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
        } catch (_: Exception) {}
        list
    }

    // Share summary helper
    fun shareProductSummary() {
        val shareText = """
            📦 ${scan.productName} (${scan.brand})
            Оценка безопасности: ${scan.healthScore}/100
            
            🔬 Перевод состава:
            ${scan.translatedIngredientsRu}
            
            📊 Пищевая ценность (${scan.volumeOrWeight}):
            Калории: ${scan.calories}, Белки: ${scan.proteins}, Жиры: ${scan.fats}, Углеводы: ${scan.carbs} (Сахар: ${scan.sugar})
            
            ⚠️ Вердикт:
            ${scan.verdictText}
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Поделиться составом банки"))
    }

    BackHandler {
        viewModel.closeScanDetails()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = scan.productName,
                        maxLines = 1,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.closeScanDetails() },
                        modifier = Modifier.testTag("result_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(scan.id) },
                        modifier = Modifier.testTag("result_fav_button")
                    ) {
                        Icon(
                            imageVector = if (scan.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "В избранное",
                            tint = if (scan.isFavorite) Color(0xFFE11D48) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = { shareProductSummary() },
                        modifier = Modifier.testTag("result_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = "Поделиться"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Product Header & Verdict Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (scan.photoUri.isNotBlank()) {
                                AsyncImage(
                                    model = scan.photoUri,
                                    contentDescription = "Фото банки",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                if (scan.brand.isNotBlank()) {
                                    Text(
                                        text = scan.brand,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = scan.productName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${scan.category} • Объем: ${scan.volumeOrWeight}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            HealthScoreBadge(
                                score = scan.healthScore,
                                sizeDp = 64
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Verdict
                        Surface(
                            color = when {
                                scan.healthScore >= 80 -> SafetySafeGreenBg
                                scan.healthScore >= 50 -> SafetyWarningOrangeBg
                                else -> SafetyDangerRedBg
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Вердикт нутрициолога:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        scan.healthScore >= 80 -> SafetySafeGreen
                                        scan.healthScore >= 50 -> SafetyWarningOrange
                                        else -> SafetyDangerRed
                                    }
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = scan.verdictText.ifBlank { "Состав проверен и структурирован." },
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Dietary chips
                        if (dietaryBadges.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                dietaryBadges.forEach { badge ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = badge,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tab row
            item {
                SecondaryTabRow(
                    selectedTabIndex = selectedSubTab,
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedSubTab == 0,
                        onClick = { selectedSubTab = 0 },
                        text = { Text("Состав (${ingredients.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                        icon = { Icon(Icons.Filled.Translate, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedSubTab == 1,
                        onClick = { selectedSubTab = 1 },
                        text = { Text("КБЖУ", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                        icon = { Icon(Icons.Filled.Calculate, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedSubTab == 2,
                        onClick = { selectedSubTab = 2 },
                        text = { Text("E-добавки (${eNumbers.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                        icon = { Icon(Icons.Filled.Warning, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                    Tab(
                        selected = selectedSubTab == 3,
                        onClick = { selectedSubTab = 3 },
                        text = { Text("Вопрос", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                        icon = { Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    )
                }
            }

            // Tab 0: Состав & Перевод
            if (selectedSubTab == 0) {
                // Russian translation text box
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Translate,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Перевод состава на русский",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(scan.translatedIngredientsRu))
                                        copiedToClipboard = true
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (copiedToClipboard) Icons.Filled.Check else Icons.Filled.ContentCopy,
                                        contentDescription = "Скопировать",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = scan.translatedIngredientsRu,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (scan.originalIngredientsRaw.isNotBlank() && scan.originalIngredientsRaw != scan.translatedIngredientsRu) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "Текст с этикетки (оригинал):",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = scan.originalIngredientsRaw,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Filter chips for ingredients
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Все", "Безопасные", "Внимание").forEach { chip ->
                            FilterChip(
                                selected = ingredientFilter == chip,
                                onClick = { ingredientFilter = chip },
                                label = { Text(chip, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                // Filtered Ingredients List
                val displayIngredients = when (ingredientFilter) {
                    "Безопасные" -> ingredients.filter { it.safety == SafetyLevel.SAFE }
                    "Внимание" -> ingredients.filter { it.safety != SafetyLevel.SAFE }
                    else -> ingredients
                }

                if (displayIngredients.isEmpty()) {
                    item {
                        Text(
                            text = "Нет ингредиентов в этой категории",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(displayIngredients) { ingredient ->
                        IngredientCard(ingredient = ingredient)
                    }
                }
            }

            // Tab 1: КБЖУ & Порция
            if (selectedSubTab == 1) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = "Пищевая ценность (КБЖУ)",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (showTotalPortion) "Расчет на всю банку: ${scan.volumeOrWeight}" else "Стандартная норма на 100 г / мл",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Button(
                                    onClick = { viewModel.togglePortionView() },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (showTotalPortion) "На 100 г" else "На банку",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Large Calorie Display
                            Surface(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "Калорийность",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = scan.calories,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Surface(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = scan.volumeOrWeight,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Nutrients Breakdown
                            MacroProgressBar(
                                label = "Белки (протеин)",
                                valueString = scan.proteins,
                                progress = 0.5f,
                                color = Color(0xFF0284C7)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            MacroProgressBar(
                                label = "Жиры",
                                valueString = scan.fats,
                                progress = 0.2f,
                                color = Color(0xFFF59E0B)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            MacroProgressBar(
                                label = "Углеводы",
                                valueString = scan.carbs,
                                progress = 0.6f,
                                color = Color(0xFF10B981)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            MacroProgressBar(
                                label = "Из них сахара",
                                valueString = scan.sugar,
                                progress = 0.7f,
                                color = Color(0xFFEF4444)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            MacroProgressBar(
                                label = "Соль (натрий)",
                                valueString = scan.salt,
                                progress = 0.15f,
                                color = Color(0xFF8B5CF6)
                            )
                        }
                    }
                }

                // Allergens card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Warning,
                                    contentDescription = null,
                                    tint = SafetyWarningOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Аллергены и ограничения",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (allergens.isEmpty()) {
                                Text(
                                    text = "Явных аллергенов на этикетке не обнаружено.",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    allergens.forEach { allergen ->
                                        Surface(
                                            color = SafetyWarningOrangeBg,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = allergen,
                                                color = SafetyWarningOrange,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tab 2: E-добавки
            if (selectedSubTab == 2) {
                if (eNumbers.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SafetySafeGreenBg
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "В составе нет E-добавок! 🎉",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = SafetySafeGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "На этикетке не найдено синтетических красителей, консервантов или стабилизаторов с кодом E.",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                } else {
                    items(eNumbers) { eItem ->
                        EAdditiveCard(item = eItem)
                    }
                }
            }

            // Tab 3: Вопрос ассистенту
            if (selectedSubTab == 3) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Задайте любой вопрос о банке",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ИИ проанализирует конкретно этот состав и даст экспертный ответ",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick prompt chips
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf(
                                    "Можно ли детям?",
                                    "Сколько сахара в ложках?",
                                    "Подходит для похудения?",
                                    "Есть ли пальмовое масло?"
                                ).forEach { prompt ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.clickable {
                                            viewModel.askQuestion(prompt)
                                        }
                                    ) {
                                        Text(
                                            text = prompt,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = userQuestionText,
                                    onValueChange = { userQuestionText = it },
                                    placeholder = { Text("Например: безопасен ли краситель?") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("qa_input_field"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (userQuestionText.isNotBlank()) {
                                            viewModel.askQuestion(userQuestionText)
                                            userQuestionText = ""
                                        }
                                    },
                                    enabled = userQuestionText.isNotBlank() && !isAnswering,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("qa_send_button")
                                ) {
                                    if (isAnswering) {
                                        CircularProgressIndicator(
                                            color = Color.White,
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Send,
                                            contentDescription = "Спросить"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // QA History
                if (qaList.isNotEmpty()) {
                    items(qaList.reversed()) { (question, answer) ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "❓ $question",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = answer,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
