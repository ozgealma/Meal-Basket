package com.example.mealbasket.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.mealbasket.databinding.ItemCartBinding
import com.example.mealbasket.databinding.ItemFavoriteBinding
import com.example.mealbasket.model.bringall.Yemekler
import com.example.mealbasket.model.bringbasket.Basket
import com.example.mealbasket.util.Constants
import com.example.mealbasket.viewmodel.FavoriteViewModel
import javax.inject.Singleton

// Bu sınıf, favori yemekleri göstermek için kullanılan RecyclerView adaptörüdür.
class FavoritesAdapter(val viewModel : FavoriteViewModel) : androidx.recyclerview.widget.ListAdapter<Yemekler, FavoritesAdapter.FavoritesHolder>(NoteDiffCallback) {

    // DiffUtil sınıfı, iki öğenin karşılaştırılması için kullanılır.
    object NoteDiffCallback : DiffUtil.ItemCallback<Yemekler>() {
        override fun areItemsTheSame(oldItem: Yemekler, newItem: Yemekler): Boolean {
            // İki öğe aynı mı kontrolü için kullanılır.
            return oldItem.yemek_id == newItem.yemek_id
        }

        override fun areContentsTheSame(oldItem: Yemekler, newItem: Yemekler): Boolean {
            // İki öğenin içerikleri aynı mı kontrolü için kullanılır.
            return oldItem == newItem
        }
    }

    // RecyclerView için ViewHolder sınıfıdır.
    class FavoritesHolder(val binding : ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder oluşturulduğunda çağrılan metoddur.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesHolder {
        // ViewHolder'ı oluşturup döndüren kısım.
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesHolder(binding)
    }

    // ViewHolder'ın içeriğini bağlamak için kullanılan metoddur.
    override fun onBindViewHolder(holder: FavoritesHolder, position: Int) {
        // Belirli bir konumdaki öğeyi alır.
        val item = currentList[position]

        // Glide kütüphanesi ile resmi yükleyip gösterme kısmıdır.
        Glide.with(holder.binding.imgFood).load(item.yemek_resim_adi).into(holder.binding.imgFood)

        // Yemek adını ve fiyatını gösterme kısmıdır.
        holder.binding.foodNameTV.text = item.yemek_adi
        holder.binding.priceTV.text = item.yemek_fiyat + " TL"

        // Fiyatın üzerine çizgi çekme kısmıdır.
        holder.binding.fakePriceTV.paintFlags = holder.binding.fakePriceTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.binding.fakePriceTV.text = (item.yemek_fiyat.toDouble() * 110 / 100).toString() + " TL"

        // Favori yemek silme butonuna tıklandığında çağrılan metoddur.
        holder.binding.deleteMeal.setOnClickListener {
            viewModel.deleteFavorites(item)
        }
    }
}
