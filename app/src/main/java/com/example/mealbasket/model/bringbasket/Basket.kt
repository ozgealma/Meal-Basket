package com.example.mealbasket.model.bringbasket

// Bu veri sınıfı, kullanıcının sepetinde bulunan bir yemek öğesini temsil eder.
data class Basket(
    val sepet_yemek_id: Int, // Sepet içindeki yemeğin benzersiz kimliğini temsil eden özellik.
    val yemek_adi: String, // Yemeğin adını temsil eden özellik.
    val yemek_resim_adi: String, // Yemeğin resim dosyasının adını temsil eden özellik.
    val yemek_fiyat: Int, // Yemeğin fiyatını temsil eden özellik.
    val yemek_siparis_adet: Int, // Yemeğin sipariş adedini temsil eden özellik.
    val kullanici_adi: String // Yemeğin eklenmiş olduğu kullanıcı adını temsil eden özellik.
)
