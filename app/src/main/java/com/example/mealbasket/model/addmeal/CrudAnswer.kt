package com.example.mealbasket.model.addmeal

// Bu veri sınıfı, CRUD (Create, Read, Update, Delete) işlemleri sonucunda dönen yanıtı temsil eder.
data class CrudAnswer(
    val message: String, // İşlemle ilgili bilgi içeren mesaj.
    val success: Int // İşlemin başarılı olup olmadığını belirten değer (0: Başarısız, 1: Başarılı).
)
