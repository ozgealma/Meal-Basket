package com.example.mealbasket.model.bringall


// Bu veri sınıfı, yemeklerin listesini içeren bir yanıtı temsil eder.
data class Meal(
    val success: Int, // İstek sonucunda başarılı olup olmadığını belirten değer (0: Başarısız, 1: Başarılı).
    val yemekler: List<Yemekler> // Yemeklerin listesini içeren koleksiyon.
)
