package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.EAdditiveCatalog
import com.example.data.model.SafetyLevel
import com.example.data.repository.ProductRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("LabelScan", appName)
  }

  @Test
  fun `check e additive catalog has common additives`() {
    val e330 = EAdditiveCatalog.findByCode("E330")
    assertNotNull(e330)
    assertEquals(SafetyLevel.SAFE, e330?.danger)

    val e150d = EAdditiveCatalog.findByCode("E150d")
    assertNotNull(e150d)
    assertEquals(SafetyLevel.CAUTION, e150d?.danger)
  }

  @Test
  fun `preset samples include canned items`() {
    val samples = ProductRepository.getPresetSamples()
    assertTrue(samples.isNotEmpty())
    assertTrue(samples.any { it.category == "Консервы" })
    assertTrue(samples.any { it.category == "Напитки" })
  }
}
