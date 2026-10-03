package com.example.data.repository

import com.example.data.local.ProductDao
import com.example.data.model.ProductScanEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class ProductRepository(private val productDao: ProductDao) {

    val allScans: Flow<List<ProductScanEntity>> = productDao.getAllScans()
    val favoriteScans: Flow<List<ProductScanEntity>> = productDao.getFavoriteScans()

    fun getScanById(id: Long): Flow<ProductScanEntity?> = productDao.getScanById(id)

    suspend fun getScanByIdSync(id: Long): ProductScanEntity? = productDao.getScanByIdSync(id)

    fun searchScans(query: String): Flow<List<ProductScanEntity>> = productDao.searchScans(query)

    suspend fun saveScan(scan: ProductScanEntity): Long = withContext(Dispatchers.IO) {
        productDao.insertScan(scan)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        productDao.setFavorite(id, isFavorite)
    }

    suspend fun deleteScan(id: Long) = withContext(Dispatchers.IO) {
        productDao.deleteScanById(id)
    }

    suspend fun prepopulateSamplesIfEmpty() = withContext(Dispatchers.IO) {
        // Check if DB is empty
        val existing = productDao.getScanByIdSync(1)
        if (existing == null) {
            getPresetSamples().forEach { sample ->
                productDao.insertScan(sample)
            }
        }
    }

    companion object {
        fun getPresetSamples(): List<ProductScanEntity> {
            // Tuna Can
            val tunaENumbers = JSONArray()
            val tunaIngredients = JSONArray().apply {
                put(JSONObject().apply {
                    put("name", "Филе тунца полосатого (Katsuwonus pelamis)")
                    put("category", "Основной")
                    put("safety", "SAFE")
                    put("explanation", "Дикая морская рыба, богатая легкоусвояемым белком и омега-3 жирными кислотами")
                    put("amountOrPercentage", "70%")
                })
                put(JSONObject().apply {
                    put("name", "Вода питьевая")
                    put("category", "Бульон")
                    put("safety", "SAFE")
                    put("explanation", "Чистая вода для создания собственного сока при автоклавировании")
                    put("amountOrPercentage", null)
                })
                put(JSONObject().apply {
                    put("name", "Соль пищевая морская")
                    put("category", "Приправа")
                    put("safety", "SAFE")
                    put("explanation", "Натуральный консервант и источник натрия")
                    put("amountOrPercentage", "1.1%")
                })
            }
            val tunaAllergens = JSONArray().apply { put("Рыба и продукты ее переработки") }
            val tunaDietary = JSONArray().apply {
                put("Высокий белок")
                put("Кето")
                put("Без сахара")
                put("Без глютена")
            }

            // Cola Can
            val colaENumbers = JSONArray().apply {
                put(JSONObject().apply {
                    put("code", "E150d")
                    put("name", "Сахарный колер IV")
                    put("danger", "CAUTION")
                    put("purpose", "Краситель")
                    put("description", "Карамельный колер с сульфитно-аммиачной обработкой")
                    put("sideEffects", "Рекомендуется умеренное употребление")
                })
                put(JSONObject().apply {
                    put("code", "E338")
                    put("name", "Ортофосфорная кислота")
                    put("danger", "CAUTION")
                    put("purpose", "Подкислитель")
                    put("description", "Придает резкую характерную кислотность напиткам")
                    put("sideEffects", "При частом употреблении истощает зубную эмаль")
                })
            }
            val colaIngredients = JSONArray().apply {
                put(JSONObject().apply {
                    put("name", "Очищенная газированная вода")
                    put("category", "Основной")
                    put("safety", "SAFE")
                    put("explanation", "Основа напитка с углекислым газом")
                    put("amountOrPercentage", null)
                })
                put(JSONObject().apply {
                    put("name", "Сахар белый (сахароза)")
                    put("category", "Подсластитель")
                    put("safety", "CAUTION")
                    put("explanation", "Быстрый углевод высокой калорийности. Около 7 чайных ложек на банку!")
                    put("amountOrPercentage", "10.6 г / 100 мл")
                })
                put(JSONObject().apply {
                    put("name", "Краситель Сахарный колер IV (E150d)")
                    put("category", "Краситель")
                    put("safety", "CAUTION")
                    put("explanation", "Создает темно-коричневый карамельный оттенок колы")
                    put("amountOrPercentage", null)
                })
                put(JSONObject().apply {
                    put("name", "Ортофосфорная кислота (E338)")
                    put("category", "Регулятор кислотности")
                    put("safety", "CAUTION")
                    put("explanation", "Снижает приторность высокого количества сахара")
                    put("amountOrPercentage", null)
                })
                put(JSONObject().apply {
                    put("name", "Натуральные ароматизаторы")
                    put("category", "Ароматизатор")
                    put("safety", "SAFE")
                    put("explanation", "Смесь эфирных масел корицы, цитрусовых и мускатного ореха")
                    put("amountOrPercentage", null)
                })
                put(JSONObject().apply {
                    put("name", "Кофеин")
                    put("category", "Тоник")
                    put("safety", "SAFE")
                    put("explanation", "Мягкий стимулятор бодрости")
                    put("amountOrPercentage", "10 мг / 100 мл")
                })
            }

            // Beans Can
            val beansIngredients = JSONArray().apply {
                put(JSONObject().apply {
                    put("name", "Фасоль красная зерновая")
                    put("category", "Основной")
                    put("safety", "SAFE")
                    put("explanation", "Бобовая культура с высоким содержанием клетчатки и растительного белка")
                    put("amountOrPercentage", "50%")
                })
                put(JSONObject().apply {
                    put("name", "Томатная паста концентрированная")
                    put("category", "Соус")
                    put("safety", "SAFE")
                    put("explanation", "Натуральные спелые томаты, богатые антиоксидантом ликопином")
                    put("amountOrPercentage", "18%")
                })
                put(JSONObject().apply {
                    put("name", "Вода питьевая")
                    put("category", "Соус")
                    put("safety", "SAFE")
                    put("explanation", "Основа соуса")
                    put("amountOrPercentage", null)
                })
                put(JSONObject().apply {
                    put("name", "Сахар и соль")
                    put("category", "Приправа")
                    put("safety", "SAFE")
                    put("explanation", "Для баланса вкуса томатного соуса")
                    put("amountOrPercentage", "2.5%")
                })
                put(JSONObject().apply {
                    put("name", "Специи (перец душистый, луковый порошок)")
                    put("category", "Пряности")
                    put("safety", "SAFE")
                    put("explanation", "Натуральные пряности для аппетитного аромата")
                    put("amountOrPercentage", null)
                })
            }

            return listOf(
                ProductScanEntity(
                    id = 1,
                    productName = "Тунец полосатый в собственном соку",
                    brand = "Ocean Prime",
                    category = "Консервы",
                    photoUri = "",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
                    originalIngredientsRaw = "Skipjack tuna (Katsuwonus pelamis) 70%, water, sea salt 1.1%. Sterilized canned product.",
                    translatedIngredientsRu = "Тунец полосатый (70%), вода питьевая, соль морская (1.1%). Стерилизованные рыбные консервы.",
                    healthScore = 96,
                    verdictText = "Превосходный чистый состав из 3 ингредиентов. Высокое содержание белка (24 г), отсутствие сахара, консервантов и вредных E-добавок. Идеально для спортивного и здорового питания.",
                    calories = "108 ккал",
                    proteins = "24.5 г",
                    fats = "0.8 г",
                    carbs = "0 г",
                    sugar = "0 г",
                    salt = "1.1 г",
                    volumeOrWeight = "185 г (банка)",
                    allergensJson = tunaAllergens.toString(),
                    eNumbersJson = tunaENumbers.toString(),
                    ingredientsDetailedJson = tunaIngredients.toString(),
                    dietaryBadgesJson = tunaDietary.toString(),
                    isFavorite = true
                ),
                ProductScanEntity(
                    id = 2,
                    productName = "Газированный напиток Cola Classic",
                    brand = "American Cola",
                    category = "Напитки",
                    photoUri = "",
                    timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
                    originalIngredientsRaw = "Carbonated water, sugar, caramel color (E150d), phosphoric acid (E338), natural flavorings, caffeine.",
                    translatedIngredientsRu = "Вода газированная, сахар белый, краситель сахарный колер IV (E150d), ортофосфорная кислота (E338), натуральные ароматизаторы, кофеин.",
                    healthScore = 48,
                    verdictText = "Высокое содержание свободного сахара (35 грамм на банку 330 мл — превышает суточную норму ВОЗ!). Присутствует ортофосфорная кислота, негативно влияющая на эмаль зубов. Употреблять изредка.",
                    calories = "42 ккал",
                    proteins = "0 г",
                    fats = "0 г",
                    carbs = "10.6 г",
                    sugar = "10.6 г",
                    salt = "0.01 г",
                    volumeOrWeight = "330 мл (банка)",
                    allergensJson = "[]",
                    eNumbersJson = colaENumbers.toString(),
                    ingredientsDetailedJson = colaIngredients.toString(),
                    dietaryBadgesJson = JSONArray().apply { put("Веган"); put("Без глютена") }.toString(),
                    isFavorite = false
                ),
                ProductScanEntity(
                    id = 3,
                    productName = "Красная фасоль в густом томатном соусе",
                    brand = "Bonduelle Natur",
                    category = "Консервы",
                    photoUri = "",
                    timestamp = System.currentTimeMillis() - 1000 * 3600 * 8,
                    originalIngredientsRaw = "Red kidney beans (50%), tomato puree (18%), water, sugar, salt, modified corn starch, onion powder, spice extracts.",
                    translatedIngredientsRu = "Красная фасоль (50%), томатная паста (18%), вода питьевая, сахар, соль поваренная, кукурузный крахмал, сушеный лук, экстракты пряностей.",
                    healthScore = 88,
                    verdictText = "Натуральный качественный продукт с обилием клетчатки и растительного белка. Без искусственных консервантов и опасных красителей. Отличный сытный гарнир.",
                    calories = "86 ккал",
                    proteins = "5.8 г",
                    fats = "0.5 г",
                    carbs = "14.2 г",
                    sugar = "3.2 г",
                    salt = "0.8 г",
                    volumeOrWeight = "400 г (банка)",
                    allergensJson = "[]",
                    eNumbersJson = "[]",
                    ingredientsDetailedJson = beansIngredients.toString(),
                    dietaryBadgesJson = JSONArray().apply { put("Веган"); put("Источник клетчатки"); put("Постный продукт") }.toString(),
                    isFavorite = true
                )
            )
        }
    }
}
