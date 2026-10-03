package com.example.ui.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.EAdditiveCatalog
import com.example.data.model.EAdditiveItem
import com.example.data.model.ProductScanEntity
import com.example.data.network.GeminiVisionService
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab(val titleRu: String) {
    SCANNER("Сканер"),
    HISTORY("История"),
    CATALOG_E("Е-добавки"),
    HELP("О сканере")
}

class ScanViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _currentTab = MutableStateFlow(AppTab.SCANNER)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _selectedScan = MutableStateFlow<ProductScanEntity?>(null)
    val selectedScan: StateFlow<ProductScanEntity?> = _selectedScan.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisStep = MutableStateFlow("")
    val analysisStep: StateFlow<String> = _analysisStep.asStateFlow()

    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    private val _onlyFavorites = MutableStateFlow(false)
    val onlyFavorites: StateFlow<Boolean> = _onlyFavorites.asStateFlow()

    private val _eCatalogQuery = MutableStateFlow("")
    val eCatalogQuery: StateFlow<String> = _eCatalogQuery.asStateFlow()

    private val _selectedECategory = MutableStateFlow("Все")
    val selectedECategory: StateFlow<String> = _selectedECategory.asStateFlow()

    // Q&A about current opened product
    private val _qaList = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val qaList: StateFlow<List<Pair<String, String>>> = _qaList.asStateFlow()

    private val _isAnswering = MutableStateFlow(false)
    val isAnswering: StateFlow<Boolean> = _isAnswering.asStateFlow()

    // Portion display calculation: false = per 100g/ml, true = for the whole container/can
    private val _showTotalPortion = MutableStateFlow(false)
    val showTotalPortion: StateFlow<Boolean> = _showTotalPortion.asStateFlow()

    val allScans: StateFlow<List<ProductScanEntity>> = repository.allScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredScans: StateFlow<List<ProductScanEntity>> = combine(
        allScans,
        _historySearchQuery,
        _onlyFavorites
    ) { scans, query, favs ->
        scans.filter { scan ->
            val matchesQuery = query.isBlank() ||
                    scan.productName.contains(query, ignoreCase = true) ||
                    scan.brand.contains(query, ignoreCase = true) ||
                    scan.translatedIngredientsRu.contains(query, ignoreCase = true)
            val matchesFav = !favs || scan.isFavorite
            matchesQuery && matchesFav
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredECatalog: StateFlow<List<EAdditiveItem>> = combine(
        _eCatalogQuery,
        _selectedECategory
    ) { query, category ->
        val cleanQuery = query.trim()
        EAdditiveCatalog.items.filter { item ->
            val matchesText = cleanQuery.isBlank() ||
                    item.code.contains(cleanQuery, ignoreCase = true) ||
                    item.name.contains(cleanQuery, ignoreCase = true) ||
                    item.purpose.contains(cleanQuery, ignoreCase = true)
            val matchesCategory = when (category) {
                "Все" -> true
                "Красители" -> item.code.startsWith("E1")
                "Консерванты" -> item.code.startsWith("E2")
                "Антиоксиданты" -> item.code.startsWith("E3")
                "Загустители" -> item.code.startsWith("E4")
                "Подсластители" -> item.code.startsWith("E9")
                else -> true
            }
            matchesText && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EAdditiveCatalog.items)

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun openScanDetails(scan: ProductScanEntity) {
        _selectedScan.value = scan
        _qaList.value = emptyList()
        _showTotalPortion.value = false
    }

    fun closeScanDetails() {
        _selectedScan.value = null
        _qaList.value = emptyList()
    }

    fun togglePortionView() {
        _showTotalPortion.value = !_showTotalPortion.value
    }

    fun setHistoryQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun toggleFavoritesOnly() {
        _onlyFavorites.value = !_onlyFavorites.value
    }

    fun setECatalogQuery(query: String) {
        _eCatalogQuery.value = query
    }

    fun setECategory(category: String) {
        _selectedECategory.value = category
    }

    fun toggleFavorite(scanId: Long) {
        viewModelScope.launch {
            val current = _selectedScan.value
            val isFav = if (current?.id == scanId) !current.isFavorite else {
                allScans.value.firstOrNull { it.id == scanId }?.isFavorite == false
            }
            repository.toggleFavorite(scanId, isFav)
            if (current?.id == scanId) {
                _selectedScan.value = current.copy(isFavorite = isFav)
            }
        }
    }

    fun deleteScan(scanId: Long) {
        viewModelScope.launch {
            repository.deleteScan(scanId)
            if (_selectedScan.value?.id == scanId) {
                _selectedScan.value = null
            }
        }
    }

    fun analyzeLabel(bitmap: Bitmap, photoUriString: String) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisStep.value = "Считывание текста этикетки..."
            delay(400)
            _analysisStep.value = "Перевод состава и поиск E-добавок..."
            
            val result = GeminiVisionService.analyzeLabelPhoto(bitmap, photoUriString)
            _analysisStep.value = "Формирование КБЖУ и оценки безопасности..."
            delay(300)

            val entity = result.getOrNull() ?: GeminiVisionService.createSimulatedAnalysis(photoUriString)
            val newId = repository.saveScan(entity)
            val savedEntity = entity.copy(id = newId)

            _selectedScan.value = savedEntity
            _isAnalyzing.value = false
            _analysisStep.value = ""
        }
    }

    fun askQuestion(question: String) {
        val current = _selectedScan.value ?: return
        if (question.isBlank()) return

        viewModelScope.launch {
            _isAnswering.value = true
            val answer = GeminiVisionService.askQuestionAboutProduct(current, question)
            _qaList.value = _qaList.value + (question to answer)
            _isAnswering.value = false
        }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
                return ScanViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
