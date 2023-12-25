package com.example.mealbasket.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealbasket.databinding.ItemCartBinding
import com.example.mealbasket.model.bringbasket.Basket
import com.example.mealbasket.util.TotalPriceListener
import com.example.mealbasket.viewmodel.CartViewModel
import javax.inject.Inject
import javax.inject.Singleton

// Bu sınıfı bir Singleton olarak işaretler, yani yalnızca bir örneğinin olmasını sağlar.
@Singleton
class CartAdapter @Inject constructor(val viewModel: CartViewModel): ListAdapter<Basket,CartAdapter.CartHolder>(NoteDiffCallback) {

    // Toplam fiyat değişikliklerini dinlemek için bir arayüz referansı.
    var listener: TotalPriceListener? = null

    // Liste güncellemeleri için kullanılan DiffUtil sınıfının örneği.
    object NoteDiffCallback : DiffUtil.ItemCallback<Basket>() {
        override fun areItemsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            // İki öğe aynı mı kontrolü için kullanılır.
            return oldItem.sepet_yemek_id == newItem.sepet_yemek_id
        }

        override fun areContentsTheSame(oldItem: Basket, newItem: Basket): Boolean {
            // İki öğenin içerikleri aynı mı kontrolü için kullanılır.
            return oldItem == newItem
        }
    }

    // RecyclerView için ViewHolder sınıfı.
    class CartHolder(val binding : ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    // ViewHolder oluşturulduğunda çağrılan metod.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        // ViewHolder'ı oluşturup döndüren kısım.
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartHolder(binding)
    }

    // ViewHolder'ın içeriğini bağlamak için kullanılan metod.
    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        // Belirli bir konumdaki öğeyi al.
        val item = currentList[position]

        // ViewHolder'ın içindeki view elemanlarına erişim için apply bloğu.
        holder.binding.apply {
            foodNameTV.text = item.yemek_adi
            priceTV.text = item.yemek_fiyat.toString() + " TL"
            pieceTV.text = "Quantity :" + item.yemek_siparis_adet.toString()
            fakePriceTV.paintFlags = fakePriceTV.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            fakePriceTV.text = (item.yemek_fiyat.toDouble()*110/100).toString() + " TL"

            totalPriceTV.text =(item.yemek_siparis_adet * item.yemek_fiyat).toString() + " TL"

            // Silme butonuna tıklandığında çağrılan metod.
            deleteMeal.setOnClickListener {
                viewModel.deleteMeal(item.sepet_yemek_id)
                Toast.makeText(it.context,"${item.yemek_adi} deleted.",Toast.LENGTH_SHORT).show()
                updateTotalPrice()
            }
        }

        // Glide kütüphanesi ile resmi yükleyip gösterme.
        Glide.with(holder.binding.imgFood).load(item.yemek_resim_adi).into(holder.binding.imgFood)
    }

    // Toplam fiyatı güncelleyen özel bir metod.
    private fun updateTotalPrice() {
        val total = currentList.sumByDouble { (it.yemek_siparis_adet * it.yemek_fiyat).toDouble() }
        // Dinleyici aracılığıyla toplam fiyat değişikliğini ileten kısım.
        listener?.onPriceUpdated(total)
    }
}
