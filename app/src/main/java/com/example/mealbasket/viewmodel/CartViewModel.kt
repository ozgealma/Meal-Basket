package com.example.mealbasket.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealbasket.model.bringbasket.Basket
import com.example.mealbasket.model.bringbasket.Bringbasket
import com.example.mealbasket.room.repo.MealsRepo
import com.example.mealbasket.util.Constants.NICKNAME
import com.example.mealbasket.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val repo: MealsRepo): ViewModel() {

    // Yemek listesini takip etmek için MutableLiveData kullanılır.
    private val mealList = MutableLiveData<Resource<Bringbasket>>()

    // Toplam fiyatı takip etmek için kullanılan LiveData.
    private val _totalPrice = MutableLiveData<Double>()
    // Dışarıdan erişim için LiveData getter'ı.
    val totalPrice: LiveData<Double> get() = _totalPrice

    // ViewModel oluşturulduğunda çalışan init bloğu.
    init {
        // Başlangıçta yemek listesini almak için fetchMeals() çağrılır.
        fetchMeals()
    }

    // Sepet güncellendikçe toplam fiyatı güncelleyen fonksiyon.
    private fun updateTotalPrice(baskets: List<Basket>?) {
        if (!baskets.isNullOrEmpty()) {
            val total = baskets.sumByDouble { (it.yemek_siparis_adet * it.yemek_fiyat).toDouble() }
            _totalPrice.value = total
        } else {
            _totalPrice.value = 0.0
        }
    }

    // API'den yemek verilerini almak için kullanılan fonksiyon.
    fun fetchMeals(){
        mealList.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val response = repo.bringBasket(NICKNAME)
                Log.d("CartViewModel", "API Response: ${response.body()}")
                if(response.isSuccessful){
                    response.body()?.let {
                        // Başarılı bir cevap alındığında Resource.Success durumunu günceller.
                        mealList.value = Resource.Success(it)
                        // Mevcut listeye göre toplam fiyatı güncelle.
                        updateTotalPrice(it.sepet_yemekler)
                    }
                }else{
                    // Hata durumunda Resource.Error durumunu günceller.
                    mealList.value = Resource.Error(response.message())
                }
            } catch (e : Exception){
                // Hata durumunda Resource.Error durumunu günceller.
                mealList.value = Resource.Error(e.message.toString())
            }
        }
    }

    // Yeni bir yemek eklemek için kullanılan fonksiyon.
    fun addFood(mealName: String, mealImage: String, mealPrice: Int, mealQuantity: Int){
        viewModelScope.launch {
            // Yemek eklemeyi gerçekleştirir.
            repo.addFood(mealName = mealName,mealImage = mealImage,mealPrice = mealPrice,mealQuantity = mealQuantity)
            // Yemek ekledikten sonra yemek listesini günceller.
            fetchMeals()
        }
    }

    // Bir yemeği sepetten silmek için kullanılan fonksiyon.
    fun deleteMeal(id : Int){
        viewModelScope.launch {
            try {
                // API üzerinden yemeği siler.
                val response = repo.deleteMeal(id, nickname = NICKNAME)
                Log.d("CartViewModel", "API Response: ${response.body()}")
                if (response.isSuccessful) {
                    // Yemek başarıyla silindikten sonra yemek listesini günceller.
                    fetchMeals()
                } else {
                    // Hata durumunda hata mesajını loglar.
                    Log.e("CartViewModel", "Failed to delete meal: ${response.message()}")
                }
            } catch (e: Exception) {
                // İstisna durumunda hata mesajını loglar.
                Log.e("CartViewModel", "Exception when deleting meal", e)
            }
        }
    }

    // Yemek listesini gözlemlemek için kullanılan fonksiyon.
    fun observeMealList() : LiveData<Resource<Bringbasket>> {
        return mealList
    }
}
