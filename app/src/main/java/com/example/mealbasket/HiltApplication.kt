package com.example.mealbasket

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication : Application() {

    // Hilt kullanılarak bağımlılıkların yönetildiği uygulama sınıfı.
    // Bu sınıf, Hilt'i başlatmak ve uygulama düzeyinde bağımlılıkları sağlamak için kullanılır.

}
