package com.example

import android.app.Application
import com.example.data.local.AppDatabase
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LabelScanApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { ProductRepository(database.productDao()) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            repository.prepopulateSamplesIfEmpty()
        }
    }
}
