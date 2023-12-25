package com.example.mealbasket.di

import android.content.Context
import androidx.room.Room
import com.example.mealbasket.adapter.CartAdapter
import com.example.mealbasket.adapter.HomeAdapter
import com.example.mealbasket.util.Constants.BASE_URL
import com.example.mealbasket.api.RetrofitApi
import com.example.mealbasket.model.bringall.Meal
import com.example.mealbasket.room.repo.MealsRepo
import com.example.mealbasket.room.MealDatabase
import com.example.mealbasket.viewmodel.CartViewModel
import com.example.mealbasket.viewmodel.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Bu modül, Dependency Injection ile ilgili bağımlılıkları sağlamak için kullanılır.
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    // RetrofitApi bağımlılığını sağlamak için kullanılan fonksiyon.
    @Singleton
    @Provides
    fun injectRetrofitApi(): RetrofitApi {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL).build()
            .create(RetrofitApi::class.java)
    } // Retrofit nesnesini oluşturup, API ile iletişim kurmak için kullanılır.

    // MealsRepo bağımlılığını sağlamak için kullanılan fonksiyon.
    @Singleton
    @Provides
    fun injectMealsRepo(api: RetrofitApi, db: MealDatabase): MealsRepo {
        return MealsRepo(api, db)
    }

    // HomeViewModel bağımlılığını sağlamak için kullanılan fonksiyon.
    @Singleton
    @Provides
    fun injectHomeViewModel(repo: MealsRepo): HomeViewModel {
        return HomeViewModel(repo)
    }

    // CartViewModel bağımlılığını sağlamak için kullanılan fonksiyon.
    @Singleton
    @Provides
    fun injectCartViewModel(repo: MealsRepo): CartViewModel {
        return CartViewModel(repo)
    }

    // HomeAdapter bağımlılığını sağlamak için kullanılan fonksiyon.
    @Provides
    @Singleton
    fun provideHomeAdapter(viewModel: CartViewModel): HomeAdapter = HomeAdapter(viewModel)

    // RoomDatabase bağımlılığını sağlamak için kullanılan fonksiyon.
    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MealDatabase::class.java, "mealDb")
            .fallbackToDestructiveMigration().build()

    // Dao bağımlılığını sağlamak için kullanılan fonksiyon.
    @Singleton
    @Provides
    fun injectDao(database: MealDatabase) = database.mealDao()

}
