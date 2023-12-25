package com.example.mealbasket.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mealbasket.model.bringall.Yemekler

//Room database favoriler eklenecek ürünleri tutar.

// Bu DAO (Data Access Object) arayüzü, yemek verilerine erişim için kullanılan SQL sorgularını içerir.
@Dao
interface MealDao {

    // Veritabanındaki tüm favori yemekleri getiren sorgu.
    @Query("SELECT * From favorite")
    fun getAll(): List<Yemekler>

    // Yemek verisini veritabanına eklemek için kullanılan sorgu.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal: Yemekler)

    // Belirli bir yemeği veritabanından silmek için kullanılan sorgu.
    @Delete
    fun deleteMeal(meal: Yemekler)

    // Belirli bir yemeği veritabanında güncellemek için kullanılan sorgu.
    @Update
    fun updateMeal(meal: Yemekler)

    // Belirli bir yemeğin favori olup olmadığını kontrol etmek için kullanılan sorgu.
    @Query("SELECT * FROM favorite WHERE yemek_id = :itemId LIMIT 1")
    fun isItemFavorited(itemId: Int): LiveData<Yemekler?>
}

