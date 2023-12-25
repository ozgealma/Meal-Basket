package com.example.mealbasket.util

// Bu arayüz, sepete eklenen yemeklerin toplam fiyatının güncellendiğini dinlemek için kullanılır.
interface TotalPriceListener {

    // Toplam fiyat güncellendiğinde çağrılan fonksiyon.
    // @param totalPrice: Yemeklerin toplam fiyatı.
    fun onPriceUpdated(totalPrice: Double)
}
