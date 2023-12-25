package com.example.mealbasket.view

import android.graphics.Color
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mealbasket.R
import com.example.mealbasket.databinding.FragmentDetailsBinding
import com.example.mealbasket.model.bringall.Meal
import com.example.mealbasket.model.bringall.Yemekler
import com.example.mealbasket.model.bringbasket.Basket
import com.example.mealbasket.util.Constants.IMAGE_URL
import com.example.mealbasket.util.viewBinding
import com.example.mealbasket.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    // ViewBinding kütüphanesi ile fragment'ın layout'u bağlanır.
    private val binding by viewBinding(FragmentDetailsBinding::bind)

    // DetailsViewModel sınıfından bir örnek oluşturulur.
    private val viewModel: DetailsViewModel by viewModels()

    // Navigation Component tarafından geçirilen argümanları almak için kullanılır.
    private val args: DetailsFragmentArgs by navArgs()

    // Ürün miktarını tutan değişken.
    private var quantity = 1

    // Favori butonunun rengini takip eden değişken.
    private var isRed = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar öğeleri ayarlanır.
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Görsel öğeler ve veriler yerleştirilir.
        val image = IMAGE_URL + args.meal.yemek_resim_adi
        binding.totalPriceTV.text = args.meal.yemek_fiyat + " TL"
        binding.priceTV.text = args.meal.yemek_fiyat + " TL"
        Glide.with(binding.imgFood).load(image).into(binding.imgFood)
        binding.nameTV.text = args.meal.yemek_adi

        // Favori butonuna tıklanma durumu kontrol edilir.
        binding.addToFavorite.setOnClickListener {
            if (isRed) {
                // Favorilerden silme işlemi gerçekleşir.
                viewModel.deleteFavorites(
                    Yemekler(
                        yemek_id = args.meal.yemek_id,
                        yemek_adi = args.meal.yemek_adi,
                        yemek_fiyat = args.meal.yemek_fiyat,
                        yemek_resim_adi = image
                    )
                )
                Toast.makeText(it.context, "${args.meal.yemek_adi} deleted from favorites.", Toast.LENGTH_SHORT)
                    .show()
                isRed = false
                binding.addToFavorite.setColorFilter(Color.GRAY)
            } else {
                // Favorilere ekleme işlemi gerçekleşir.
                viewModel.addToFavorites(
                    Yemekler(
                        yemek_id = args.meal.yemek_id,
                        yemek_adi = args.meal.yemek_adi,
                        yemek_fiyat = args.meal.yemek_fiyat,
                        yemek_resim_adi = image
                    )
                )
                Toast.makeText(it.context, "${args.meal.yemek_adi} added to favorites.", Toast.LENGTH_SHORT).show()
                binding.addToFavorite.setColorFilter(ContextCompat.getColor(binding.addToFavorite.context, R.color.red))
                isRed = true
            }
        }

        // Ürün miktarını artıran butona tıklama durumu kontrol edilir.
        binding.addIV.setOnClickListener {
            quantity++
            binding.pieceTV.text = quantity.toString()
            binding.totalPriceTV.text = (quantity * args.meal.yemek_fiyat.toInt()).toString() + " TL"
        }

        // Ürün miktarını azaltan butona tıklama durumu kontrol edilir.
        binding.minusIV.setOnClickListener {
            quantity--
            if (quantity == 0) {
                quantity = 1
                Toast.makeText(requireContext(), "Quantity can't be 0", Toast.LENGTH_SHORT).show()
            }
            binding.pieceTV.text = quantity.toString()
            binding.totalPriceTV.text = (quantity * args.meal.yemek_fiyat.toInt()).toString() + " TL"
        }

        // Sepete ekleme butonuna tıklanma durumu kontrol edilir.
        binding.addBtn.setOnClickListener {
            viewModel.addFood(
                mealName = args.meal.yemek_adi,
                mealImage = image,
                mealPrice = args.meal.yemek_fiyat.toInt(),
                mealQuantity = quantity
            )
            Toast.makeText(it.context, "${args.meal.yemek_adi} added to cart", Toast.LENGTH_SHORT).show()
        }

        // Favori butonunun durumu gözlemlenir.
        observeData(args.meal.yemek_id.toInt())
    }

    // Favori butonunun durumunu gözlemlemek için kullanılır.
    private fun observeData(id: Int) {
        viewModel.isItemFavorited(id).observe(viewLifecycleOwner) { favoriteItem ->
            if (favoriteItem != null) {
                binding.addToFavorite.setColorFilter(Color.RED)
            } else {
                binding.addToFavorite.setColorFilter(Color.GRAY)
            }
        }
    }
}
