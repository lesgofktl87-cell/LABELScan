package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ProductScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<ProductScanEntity>>

    @Query("SELECT * FROM product_scans WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteScans(): Flow<List<ProductScanEntity>>

    @Query("SELECT * FROM product_scans WHERE id = :id LIMIT 1")
    fun getScanById(id: Long): Flow<ProductScanEntity?>

    @Query("SELECT * FROM product_scans WHERE id = :id LIMIT 1")
    suspend fun getScanByIdSync(id: Long): ProductScanEntity?

    @Query("SELECT * FROM product_scans WHERE productName LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%' OR originalIngredientsRaw LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchScans(query: String): Flow<List<ProductScanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ProductScanEntity): Long

    @Update
    suspend fun updateScan(scan: ProductScanEntity)

    @Query("UPDATE product_scans SET isFavorite = :isFav WHERE id = :id")
    suspend fun setFavorite(id: Long, isFav: Boolean)

    @Query("DELETE FROM product_scans WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM product_scans")
    suspend fun clearAll()
}
