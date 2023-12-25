package com.example.mealbasket.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mealbasket.util.Resource
import com.example.mealbasket.util.viewBinding
import com.example.mealbasket.R
import com.example.mealbasket.adapter.HomeAdapter
import com.example.mealbasket.databinding.FragmentHomeBinding
import com.example.mealbasket.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    // ViewBinding özelliği için bağlama referansı.
    private val binding by viewBinding(FragmentHomeBinding::bind)

    // HomeViewModel özelliği için ViewModel referansı.
    private val viewModel: HomeViewModel by viewModels()

    // Dagger-Hilt tarafından sağlanan HomeAdapter özelliği.
    @Inject
    lateinit var adapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Veri gözlemlemek için observeData() fonksiyonunu çağırın.
        observeData()
    }

    // Veri gözlemleme işlemlerini gerçekleştiren fonksiyon.
    private fun observeData() {
        viewModel.observeMealList().observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { meal ->
                        // Başarılı durumda, gelen yemek verilerini adaptöre gönderin ve RecyclerView'a ayarlayın.
                        hideProgressBar()
                        adapter.submitList(meal.yemekler)
                        binding.rvMeals.adapter = adapter

                        // Adaptördeki öğelere tıklanıldığında yapılacak işlemleri belirtin.
                        adapter.onItemClick = {
                            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
                            val navController = findNavController()

                            // Eğer mevcut destinasyon HomeFragment ise, DetailsFragment'a geçiş yapın.
                            if (navController.currentDestination?.id == R.id.homeFragment) {
                                navController.navigate(action)
                            }
                        }
                    }
                }
                is Resource.Loading -> {
                    // Yükleniyor durumunda ilgili görüntüleri gösterin.
                    showProgressBar()
                }
                is Resource.Error -> {
                    // Hata durumunda log kaydı yapın ve ilgili görüntüleri gösterin.
                    Log.e("Home Fragment", response.message.toString())
                    hideProgressBar()
                    binding.rvMeals.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.imgError.visibility = View.VISIBLE
                }

                else -> {}
            }
        }
    }

    // ProgressBar'ı gösteren yardımcı fonksiyon.
    private fun showProgressBar() {
        binding.imgError.visibility = View.GONE
        binding.rvMeals.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    // ProgressBar'ı gizleyen yardımcı fonksiyon.
    private fun hideProgressBar() {
        binding.apply {
            progressBar.visibility = View.GONE
            imgError.visibility = View.GONE
            rvMeals.visibility = View.VISIBLE
        }
    }
}



