package com.example.mealbasket.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealbasket.model.bringall.Yemekler
import com.example.mealbasket.room.repo.MealsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(val repo: MealsRepo) : ViewModel() {

    // Favori yemekleri takip etmek için kullanılan MutableLiveData.
    private val favoritesList = MutableLiveData<List<Yemekler>>()

    // ViewModel oluşturulduğunda çalışan init bloğu.
    init {
        // Favori yemek listesini başlangıçta almak için getAll() fonksiyonu çağrılır.
        getAll()
    }

    // Favori bir yemeği silmek için kullanılan fonksiyon.
    fun deleteFavorites(meal: Yemekler) {
        viewModelScope.launch(Dispatchers.IO) {
            // Belirli bir yemeği favorilerden siler.
            repo.deleteMeal(meal)
            // Yemek silindiğinde favori yemek listesini günceller.
            getAll()
        }
    }

    // Tüm favori yemekleri almak için kullanılan fonksiyon.
    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            // Favori yemekleri veritabanından alır.
            val list = repo.getAll()
            // UI ile etkileşimde bulunmak için withContext kullanarak LiveData'yı günceller.
            withContext(Dispatchers.Main) {
                favoritesList.value = list
            }
        }
    }

    // Favori yemek listesini gözlemlemek için kullanılan fonksiyon.
    fun observeFavoritesList(): MutableLiveData<List<Yemekler>> {
        return favoritesList
    }

}
