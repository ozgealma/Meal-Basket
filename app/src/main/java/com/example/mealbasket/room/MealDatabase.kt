package com.example.mealbasket.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mealbasket.model.bringall.Yemekler

//database oluşturma

// Bu sınıf, uygulamanın Room veritabanına erişim sağlamak için kullanılan RoomDatabase sınıfını temsil eder.
@Database(entities = [Yemekler::class], version = 2)
abstract class MealDatabase : RoomDatabase() {

    // Veritabanı işlemleri için kullanılan DAO (Data Access Object) arayüzünü döndüren soyut fonksiyon.
    abstract fun mealDao(): MealDao
}


