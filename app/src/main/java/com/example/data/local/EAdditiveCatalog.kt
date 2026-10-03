package com.example.data.local

import com.example.data.model.EAdditiveItem
import com.example.data.model.SafetyLevel

object EAdditiveCatalog {
    val items: List<EAdditiveItem> = listOf(
        // Красители (E100 - E199)
        EAdditiveItem(
            code = "E100",
            name = "Куркумин",
            danger = SafetyLevel.SAFE,
            purpose = "Натуральный желтый краситель",
            description = "Экстракт из корня куркумы. Оказывает противовоспалительное и антиоксидантное действие.",
            sideEffects = "Полностью безопасен, полезен для здоровья."
        ),
        EAdditiveItem(
            code = "E102",
            name = "Тартразин",
            danger = SafetyLevel.DANGER,
            purpose = "Синтетический желтый краситель",
            description = "Широко используется в сладостях и газировках. Запрещен в некоторых странах.",
            sideEffects = "Может вызывать гиперактивность у детей, крапивницу и аллергические реакции."
        ),
        EAdditiveItem(
            code = "E110",
            name = "Желтый «Солнечный закат»",
            danger = SafetyLevel.DANGER,
            purpose = "Оранжевый краситель",
            description = "Искусственный азокраситель для напитков, десертов и соусов.",
            sideEffects = "Сильный аллерген, повышает возбудимость у детей."
        ),
        EAdditiveItem(
            code = "E120",
            name = "Кармин (Кошениль)",
            danger = SafetyLevel.CAUTION,
            purpose = "Красный краситель",
            description = "Натуральный пигмент, получаемый из насекомых кошенили.",
            sideEffects = "Может вызывать аллергические реакции у чувствительных людей. Не подходит веганам."
        ),
        EAdditiveItem(
            code = "E129",
            name = "Красный очаровательный АС",
            danger = SafetyLevel.DANGER,
            purpose = "Синтетический красный краситель",
            description = "Популярен в конфетах, напитках и сухих завтраках.",
            sideEffects = "Может провоцировать аллергию и синдром дефицита внимания."
        ),
        EAdditiveItem(
            code = "E150d",
            name = "Сахарный колер IV (Аммиачно-сульфитный)",
            danger = SafetyLevel.CAUTION,
            purpose = "Карамельный краситель (Кола)",
            description = "Придает напиткам вроде Колы характерный темно-коричневый цвет.",
            sideEffects = "В высоких дозах может содержать побочные продукты 4-метилимидазола. Рекомендуется умеренность."
        ),
        EAdditiveItem(
            code = "E160a",
            name = "Бета-каротин",
            danger = SafetyLevel.SAFE,
            purpose = "Натуральный оранжевый краситель",
            description = "Провитамин А, содержится в моркови и облепихе.",
            sideEffects = "Безопасен, является источником витамина А."
        ),
        EAdditiveItem(
            code = "E171",
            name = "Диоксид титана",
            danger = SafetyLevel.DANGER,
            purpose = "Белый краситель",
            description = "Использовался в жевательной резинке, соусах и выпечке. Запрещен в ЕС как пищевая добавка.",
            sideEffects = "Наночастицы могут накапливаться в организме и повреждать ДНК."
        ),

        // Консерванты (E200 - E299)
        EAdditiveItem(
            code = "E200",
            name = "Сорбиновая кислота",
            danger = SafetyLevel.SAFE,
            purpose = "Природный консервант",
            description = "Эффективно подавляет рост плесневых грибов и дрожжей в консервах и сырах.",
            sideEffects = "Практически не токсичен, легко усваивается организмом."
        ),
        EAdditiveItem(
            code = "E202",
            name = "Сорбат калия",
            danger = SafetyLevel.SAFE,
            purpose = "Консервант",
            description = "Калиевая соль сорбиновой кислоты. Один из самых популярных и безопасных консервантов.",
            sideEffects = "Безопасен в разрешенных концентрациях."
        ),
        EAdditiveItem(
            code = "E211",
            name = "Бензоат натрия",
            danger = SafetyLevel.CAUTION,
            purpose = "Консервант для кислых продуктов",
            description = "Подавляет плесень в газировках, соусах и консервах.",
            sideEffects = "В сочетании с витамином C (аскорбиновой кислотой) может образовывать бензол — токсичное соединение."
        ),
        EAdditiveItem(
            code = "E250",
            name = "Нитрит натрия",
            danger = SafetyLevel.CAUTION,
            purpose = "Фиксатор окраски и консервант",
            description = "Обязателен в колбасах и мясных консервах для предотвращения опаснейшего ботулизма.",
            sideEffects = "При термообработке образует нитрозамины. Рекомендуется ограничивать употребление."
        ),

        // Антиокислители и регуляторы кислотности (E300 - E399)
        EAdditiveItem(
            code = "E300",
            name = "Аскорбиновая кислота (Витамин C)",
            danger = SafetyLevel.SAFE,
            purpose = "Антиоксидант",
            description = "Предотвращает окисление и потемнение продуктов. Жизненно важный витамин.",
            sideEffects = "Полезен для иммунитета."
        ),
        EAdditiveItem(
            code = "E322",
            name = "Лецитин (Соевый/Подсолнечный)",
            danger = SafetyLevel.SAFE,
            purpose = "Натуральный эмульгатор",
            description = "Связывает жиры и воду в шоколаде, соусах и выпечке. Содержит фосфолипиды.",
            sideEffects = "Полезен для клеток печени и мозга. Может вызывать аллергию при аллергии на сою."
        ),
        EAdditiveItem(
            code = "E330",
            name = "Лимонная кислота",
            danger = SafetyLevel.SAFE,
            purpose = "Регулятор кислотности и консервант",
            description = "Природная кислота, участвует в цикле Кребса обмена веществ человека.",
            sideEffects = "Безопасна. При избытке может раздражать зубную эмаль и желудок."
        ),
        EAdditiveItem(
            code = "E338",
            name = "Ортофосфорная кислота",
            danger = SafetyLevel.CAUTION,
            purpose = "Подкислитель напитков",
            description = "Придает резкую кислинку напиткам типа Cola.",
            sideEffects = "При частом употреблении вымывает кальций из костей и эмали зубов."
        ),

        // Стабилизаторы и загустители (E400 - E499)
        EAdditiveItem(
            code = "E407",
            name = "Каррагинан",
            danger = SafetyLevel.CAUTION,
            purpose = "Загуститель из морских водорослей",
            description = "Используется в молочных коктейлях, мороженом, колбасах.",
            sideEffects = "У некоторых людей может вызывать воспаление ЖКТ и вздутие."
        ),
        EAdditiveItem(
            code = "E412",
            name = "Гуаровая камедь",
            danger = SafetyLevel.SAFE,
            purpose = "Натуральный загуститель",
            description = "Получают из семян растения гуар. Растворимая клетчатка.",
            sideEffects = "Безопасна, снижает уровень холестерина."
        ),
        EAdditiveItem(
            code = "E415",
            name = "Ксантановая камедь",
            danger = SafetyLevel.SAFE,
            purpose = "Текстуратор и загуститель",
            description = "Натуральный биополимер бактериального происхождения.",
            sideEffects = "Безопасен для организма."
        ),
        EAdditiveItem(
            code = "E440",
            name = "Пектин",
            danger = SafetyLevel.SAFE,
            purpose = "Натуральный желеобразователь",
            description = "Растительное волокно из яблок и цитрусовых. Очищает кишечник.",
            sideEffects = "Очень полезен для пищеварения."
        ),
        EAdditiveItem(
            code = "E471",
            name = "Моно- и диглицериды жирных кислот",
            danger = SafetyLevel.SAFE,
            purpose = "Эмульгатор",
            description = "Обеспечивает однородность текстуры майонезов, соусов, мороженого.",
            sideEffects = "Усваивается организмом как обычный жир."
        ),

        // Усилители вкуса (E600 - E699)
        EAdditiveItem(
            code = "E621",
            name = "Глутамат натрия (MSG)",
            danger = SafetyLevel.CAUTION,
            purpose = "Усилитель вкуса «умами»",
            description = "Натриевая соль глутаминовой кислоты. Усиливает мясной и соленый вкус снеков и консервов.",
            sideEffects = "Сам по себе не токсичен, но стимулирует переедание вредных продуктов. Редко вызывает головную боль."
        ),

        // Подсластители (E900 - E999)
        EAdditiveItem(
            code = "E950",
            name = "Ацесульфам калия",
            danger = SafetyLevel.CAUTION,
            purpose = "Искусственный подсластитель",
            description = "В 200 раз слаще сахара, не содержит калорий. Часто сочетается с аспартамом.",
            sideEffects = "Рекомендуется не превышать суточную норму."
        ),
        EAdditiveItem(
            code = "E951",
            name = "Аспартам",
            danger = SafetyLevel.CAUTION,
            purpose = "Низкокалорийный подсластитель",
            description = "Широко применяется в диетических напитках. Содержит источник фенилаланина.",
            sideEffects = "Противопоказан при фенилкетонурии. При нагревании разрушается."
        ),
        EAdditiveItem(
            code = "E952",
            name = "Цикламат натрия",
            danger = SafetyLevel.DANGER,
            purpose = "Синтетический подсластитель",
            description = "В 30 раз слаще сахара. Запрещен в США и некоторых других странах.",
            sideEffects = "Подозрение на негативное влияние на репродуктивную систему и почки."
        ),
        EAdditiveItem(
            code = "E955",
            name = "Сукралоза",
            danger = SafetyLevel.SAFE,
            purpose = "Подсластитель из обычного сахара",
            description = "В 600 раз слаще сахара, термостабильна, не повышает уровень сахара в крови.",
            sideEffects = "Считается одним из наиболее безопасных некалорийных подсластителей."
        ),
        EAdditiveItem(
            code = "E960",
            name = "Стевиозид (Стевия)",
            danger = SafetyLevel.SAFE,
            purpose = "Натуральный подсластитель",
            description = "Экстракт листьев растения стевия. 0 калорий.",
            sideEffects = "Натурален и безопасен, идеален при диабете."
        )
    )

    fun findByCode(code: String): EAdditiveItem? {
        val cleanCode = code.uppercase().trim().replace("Е", "E")
        return items.firstOrNull { it.code.equals(cleanCode, ignoreCase = true) }
    }
}
