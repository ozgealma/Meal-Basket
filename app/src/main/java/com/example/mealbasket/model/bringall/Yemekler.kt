package com.example.mealbasket.model.bringall

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//dataclasslar verileri tutar
// Bu veri sınıfı, yemekleri temsil eden ve veritabanında saklanan bir varlık (entity) sınıfıdır.
@Entity("favorite")
data class Yemekler(
    val yemek_adi: String, // Yemeğin adını temsil eden özellik.
    val yemek_fiyat: String, // Yemeğin fiyatını temsil eden özellik.
    @PrimaryKey
    val yemek_id: String, // Yemeğin benzersiz kimliğini temsil eden özellik (Primary Key).
    val yemek_resim_adi: String, // Yemeğin resim dosyasının adını temsil eden özellik.
    var isFavorite: Boolean = false // Yemeğin favori olup olmadığını belirten özellik.
) : Serializable

