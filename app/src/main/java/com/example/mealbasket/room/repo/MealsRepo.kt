package com.example.mealbasket.room.repo

import androidx.lifecycle.LiveData
import com.example.mealbasket.api.RetrofitApi
import com.example.mealbasket.model.bringall.Yemekler
import com.example.mealbasket.room.MealDatabase
import com.example.mealbasket.util.Constants
import javax.inject.Inject

// Bu sınıf, yemek verileri üzerinde mantıksal işlemleri gerçekleştiren bir repository (veri depo) sınıfıdır.
class MealsRepo @Inject constructor(
    val api: RetrofitApi, // RetrofitApi bağımlılığını enjekte etmek için kullanılan değişken.
    val db: MealDatabase // MealDatabase bağımlılığını enjekte etmek için kullanılan değişken.
) {

    // Tüm yemekleri getiren API çağrısını gerçekleştiren suspend fonksiyon.
    suspend fun getAllMeals() = api.getAllMeals()

    // Yeni bir yemek eklemek için API çağrısını gerçekleştiren suspend fonksiyon.
    suspend fun addFood(mealName: String, mealImage: String, mealPrice: Int, mealQuantity: Int, nickname: String = Constants.NICKNAME) =
        api.addFood(mealName = mealName, mealImage = mealImage, mealPrice = mealPrice, mealQuantity = mealQuantity, nickname = Constants.NICKNAME)

    // Kullanıcının sepetindeki yemekleri getiren API çağrısını gerçekleştiren suspend fonksiyon.
    suspend fun bringBasket(nickname: String) = api.bringBasket(nickname = nickname)

    // Belirli bir yemeği sepetten silmek için API çağrısını gerçekleştiren suspend fonksiyon.
    suspend fun deleteMeal(id: Int, nickname: String) = api.deleteMeal(id, nickname)

    // Yemek verisini veritabanına eklemek için kullanılan fonksiyon.
    fun insertMeal(meal: Yemekler) = db.mealDao().insertMeal(meal)

    // Belirli bir yemeği veritabanından silmek için kullanılan fonksiyon.
    fun deleteMeal(meal: Yemekler) = db.mealDao().deleteMeal(meal)

    // Tüm yemekleri veritabanından almak için kullanılan fonksiyon.
    fun getAll() = db.mealDao().getAll()

    // Belirli bir yemeği güncellemek için kullanılan fonksiyon.
    fun updateMeal(meal: Yemekler) = db.mealDao().updateMeal(meal)

    // Belirli bir yemeğin favori olup olmadığını kontrol etmek için kullanılan LiveData fonksiyonu.
    fun getMealById(id: Int): LiveData<Yemekler?> = db.mealDao().isItemFavorited(id)
}
