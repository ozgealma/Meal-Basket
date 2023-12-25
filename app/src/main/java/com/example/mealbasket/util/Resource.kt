package com.example.mealbasket.util

// Bu mühür sınıfı, bir veri kaynağından alınan sonuçları temsil ederken aynı zamanda hata durumları ve yükleme durumu gibi senaryoları da ele alır.
sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    // Başarılı bir sonuç durumunu temsil eden alt sınıf.
    class Success<T>(data: T) : Resource<T>(data)

    // Hata durumu temsil eden alt sınıf.
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Yükleme durumu temsil eden alt sınıf.
    class Loading<T> : Resource<T>()
}
