package com.example.mealbasket.model.bringbasket

import androidx.room.Entity

// Bu veri sınıfı, kullanıcının sepetindeki yemeklerin listesini ve işlemin başarılı olup olmadığını temsil eder.
data class Bringbasket(
    val sepet_yemekler: List<Basket>, // Kullanıcının sepetindeki yemekleri içeren liste.
    val success: Int // İşlemin başarılı olup olmadığını belirten değer (0: Başarısız, 1: Başarılı).
)
