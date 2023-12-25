package com.example.mealbasket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealbasket.model.bringall.Yemekler
import com.example.mealbasket.model.bringbasket.Basket
import com.example.mealbasket.room.repo.MealsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repo: MealsRepo) : ViewModel() {

    // Belirli bir yemek için alışveriş sepetine yeni bir öğe eklemek için kullanılan fonksiyon.
    fun addFood(mealName: String, mealImage: String, mealPrice: Int, mealQuantity: Int){
        viewModelScope.launch {
            // Belirli bir yemeği alışveriş sepetine ekler.
            repo.addFood(mealName = mealName, mealImage = mealImage, mealPrice = mealPrice, mealQuantity = mealQuantity)
        }
    }

    // Favorilere ekleme işlemi için kullanılan fonksiyon.
    fun addToFavorites(meal: Yemekler){
        viewModelScope.launch(Dispatchers.IO) {
            // Belirli bir yemeği favorilere ekler.
            repo.insertMeal(meal)
            // Eğer UI güncellemesi yapacaksanız, Handler veya runOnUiThread kullanmanız gerekir.
        }
    }

    // Belirli bir öğenin favori olup olmadığını kontrol etmek için kullanılan fonksiyon.
    fun isItemFavorited(itemId: Int): LiveData<Yemekler?> {
        // Veritabanında belirli bir yemeği arar ve LiveData olarak döndürür.
        return repo.getMealById(itemId)
    }

    // Favorilerden bir öğe silme işlemi için kullanılan fonksiyon.
    fun deleteFavorites(meal: Yemekler){
        viewModelScope.launch(Dispatchers.IO) {
            // Belirli bir yemeği favorilerden siler.
            repo.deleteMeal(meal)
        }
    }

}
