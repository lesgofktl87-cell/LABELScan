package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_scans")
data class ProductScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productName: String,
    val brand: String = "",
    val category: String = "Напитки",
    val photoUri: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val originalIngredientsRaw: String = "",
    val translatedIngredientsRu: String = "",
    val healthScore: Int = 75,
    val verdictText: String = "",
    val calories: String = "0 ккал",
    val proteins: String = "0 г",
    val fats: String = "0 г",
    val carbs: String = "0 г",
    val sugar: String = "0 г",
    val salt: String = "0 г",
    val volumeOrWeight: String = "100 г / мл",
    val allergensJson: String = "[]",
    val eNumbersJson: String = "[]",
    val ingredientsDetailedJson: String = "[]",
    val dietaryBadgesJson: String = "[]",
    val isFavorite: Boolean = false
)

enum class SafetyLevel(val labelRu: String) {
    SAFE("Безопасно"),
    CAUTION("С осторожностью"),
    DANGER("Вредно / Нежелательно")
}

data class IngredientItem(
    val name: String,
    val category: String = "Основной",
    val safety: SafetyLevel = SafetyLevel.SAFE,
    val explanation: String = "",
    val amountOrPercentage: String? = null
)

data class EAdditiveItem(
    val code: String,
    val name: String,
    val danger: SafetyLevel = SafetyLevel.SAFE,
    val purpose: String = "",
    val description: String = "",
    val sideEffects: String = ""
)

data class NutritionInfo(
    val calories: String = "0 ккал",
    val proteins: String = "0 г",
    val fats: String = "0 г",
    val carbs: String = "0 г",
    val sugar: String = "0 г",
    val salt: String = "0 г",
    val volumeOrWeight: String = "100 мл/г"
)
