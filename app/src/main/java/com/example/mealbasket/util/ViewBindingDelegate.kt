package com.example.mealbasket.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

//Extension fonk. kodu daha temiz bir şekilde yazmamızı sağlar.

// Bu extension fonksiyon, AppCompatActivity sınıfına bir viewBinding özelliği ekler.
// ViewBinding öğesini başlatmak için kullanılır.
inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline factory: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        factory(layoutInflater)
    }

// Bu extension fonksiyon, Fragment sınıfına bir viewBinding özelliği ekler.
// ViewBinding öğesini başlatmak için kullanılır ve fragment yok edildiğinde temizlenir.
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        // ViewBinding öğesini döndüren ve fragment yaşam döngüsü boyunca tutulan değeri sağlar.
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: factory(requireView()).also {
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }

        // Fragment yok edildiğinde çağrılır ve ViewBinding öğesini temizler.
        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }
    }

// Bu extension fonksiyon, DialogFragment sınıfına bir viewBinding özelliği ekler.
// ViewBinding öğesini başlatmak için kullanılır.
inline fun <T : ViewBinding> DialogFragment.viewBinding(crossinline factory: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        factory(layoutInflater)
    }

// Bu extension fonksiyon, ViewGroup sınıfına bir viewBinding özelliği ekler.
// ViewBinding öğesini başlatmak için kullanılır.
inline fun <T : ViewBinding> ViewGroup.viewBinding(factory: (LayoutInflater, ViewGroup, Boolean) -> T) =
    factory(LayoutInflater.from(context), this, false)
