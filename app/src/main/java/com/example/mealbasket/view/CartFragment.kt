package com.example.mealbasket.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mealbasket.R
import com.example.mealbasket.adapter.CartAdapter
import com.example.mealbasket.databinding.FragmentCartBinding
import com.example.mealbasket.util.Resource
import com.example.mealbasket.util.TotalPriceListener
import com.example.mealbasket.util.viewBinding
import com.example.mealbasket.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    // ViewBinding özelliği için bağlama referansı.
    private val binding by viewBinding(FragmentCartBinding::bind)

    // CartViewModel özelliği için paylaşılan ViewModel referansı.
    private val viewModel: CartViewModel by activityViewModels()

    // CartAdapter özelliği için adaptör referansı.
    private lateinit var adapter: CartAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // CartAdapter'ı oluşturun ve bağlamaya ayarlayın.
        adapter = CartAdapter(viewModel)
        binding.recyclerView.adapter = adapter

        // Verileri gözlemleyin.
        observeData()

        // Toplam fiyatı gözlemleyin.
        observeTotalPrice()

        // Sepete ekle butonuna tıklanıldığında ödeme fragment'ına geçiş yapın.
        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_paymentFragment)
        }

        // Adaptör tarafından sağlanan bir TotalPriceListener'ı kullanarak toplam fiyatı güncelleyin.
        adapter.listener = object : TotalPriceListener {
            override fun onPriceUpdated(totalPrice: Double) {
                binding.priceTV.text = "$totalPrice TL"
            }
        }
    }

    // Verileri gözlemleyen fonksiyon.
    private fun observeData() {
        viewModel.observeMealList().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        hideProgressBar()
                        // Adaptöre yemek listesini gönderin.
                        adapter.submitList(it.sepet_yemekler)

                        if (it.sepet_yemekler.isEmpty()) {
                            // Eğer sepet boşsa gerekli görüntüleri gösterin.
                            binding.imgEmpty.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                            binding.imgError.visibility = View.GONE
                        } else {
                            // Sepet doluysa gerekli görüntüleri gösterin.
                            binding.imgEmpty.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.linearLayout.visibility = View.VISIBLE
                        }
                    }
                }
                is Resource.Loading -> {
                    // Yükleniyor durumundayken ilgili görüntüleri gösterin.
                    showProgressBar()
                }
                is Resource.Error -> {
                    // Hata durumunda ilgili işlemleri gerçekleştirin ve hata mesajını günlüğe ekleyin.
                    Log.e("Cart Fragment", response.message.toString())
                    hideProgressBar()
                    binding.recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.imgError.visibility = View.VISIBLE
                    binding.linearLayout.visibility = View.GONE

                    if (response.message.toString() == "End of input at line 6 column 1 path $") {
                        // Belirli bir hatanın durumu için gerekli görüntüleri gösterin.
                        binding.recyclerView.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.imgError.visibility = View.GONE
                        binding.imgEmpty.visibility = View.VISIBLE
                        binding.linearLayout.visibility = View.GONE
                    }
                }
            }
        }
    }

    // Toplam fiyatı gözlemleyen fonksiyon.
    private fun observeTotalPrice() {
        viewModel.totalPrice.observe(viewLifecycleOwner) { totalPrice ->
            binding.priceTV.text = "$totalPrice TL"
        }
    }

    // Fragment aktif olduğunda yemek verilerini yeniden çekin.
    override fun onResume() {
        super.onResume()
        viewModel.fetchMeals()
    }

    // ProgressBar'ı gösteren yardımcı fonksiyon.
    private fun showProgressBar() {
        binding.imgError.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    // ProgressBar'ı gizleyen yardımcı fonksiyon.
    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            imgError.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
