package com.example.mealbasket.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealbasket.model.bringall.Meal
import com.example.mealbasket.util.Resource
import com.example.mealbasket.room.repo.MealsRepo
import com.example.mealbasket.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: MealsRepo) : ViewModel() {

    // Yemek listesini takip etmek için kullanılan MutableLiveData.
    private val mealList = MutableLiveData<Resource<Meal>>()

    // ViewModel oluşturulduğunda çalışan init bloğu.
    init {
        // Yemek listesini başlangıçta almak için fetchMeals() fonksiyonu çağrılır.
        fetchMeals()
    }

    // Tüm yemekleri almak için kullanılan özel fonksiyon.
    private fun fetchMeals() {
        // Yemek listesini almak üzere Loading durumunu ayarlar.
        mealList.value = Resource.Loading()
        try {
            // Coroutine kullanarak asenkron olarak verileri alma işlemi başlatılır.
            viewModelScope.launch {
                // Repo üzerinden tüm yemekleri alır.
                val response = repo.getAllMeals()
                if (response.isSuccessful) {
                    // Başarılı bir cevap alındığında Resource.Success durumunu günceller.
                    response.body()?.let {
                        mealList.value = Resource.Success(it)
                    }
                } else {
                    // Hata durumunda Resource.Error durumunu günceller.
                    mealList.value = Resource.Error(response.message())
                }
            }
        } catch (e: Exception) {
            // İstisna durumunda Resource.Error durumunu günceller.
            mealList.value = Resource.Error(e.message.toString())
        }
    }

    // Yemek listesini gözlemlemek için kullanılan fonksiyon.
    fun observeMealList(): MutableLiveData<Resource<Meal>> {
        return mealList
    }
}
