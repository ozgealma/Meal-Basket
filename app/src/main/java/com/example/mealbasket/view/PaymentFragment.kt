package com.example.mealbasket.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mealbasket.R
import com.example.mealbasket.databinding.FragmentPaymentBinding
import com.example.mealbasket.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    // ViewBinding özelliği için bağlama referansı.
    private val binding by viewBinding(FragmentPaymentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar'ın geri butonuna tıklanıldığında fragment'tan çıkış yap.
        with(binding) {
            toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        // Ödeme verilerini alma işlemini gerçekleştir.
        getData()
    }

    // Ödeme verilerini alma işlemini gerçekleştiren fonksiyon.
    private fun getData() {
        // Ay listesi
        val monthList = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        var selectedMonth = ""

        // Yıl listesi
        val yearList = (2023..2038).toList()
        var selectedYear = 0

        // Spinner için adapter'lar
        val yearAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            yearList
        )
        val monthAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            monthList
        )

        // Binding içerisindeki arayüz elemanlarına adapter'ları ayarla.
        with(binding) {
            actMonth.setAdapter(monthAdapter)
            actYear.setAdapter(yearAdapter)

            // Spinner'ların seçilen öğelerini takip et.
            actMonth.setOnItemClickListener { _, _, position, _ ->
                selectedMonth = monthList[position]
            }

            actYear.setOnItemClickListener { _, _, position, _ ->
                selectedYear = yearList[position]
            }

            // Ödeme butonuna tıklanıldığında yapılacak işlemler.
            buttonPayment.setOnClickListener {
                val owner = etCardName.text.toString()
                val number = etCardNumber.text.toString()
                val cvv = etCvv.text.toString()
                val addressName = etCardAdressName.text.toString()
                val addressDesc = etCardAdress.text.toString()

                // Girilen bilgilerin eksiksiz olup olmadığını kontrol et.
                if (checkInfos(
                        owner,
                        number,
                        selectedMonth,
                        selectedYear.toString(),
                        cvv,
                        addressName,
                        addressDesc
                    )
                ) {
                    // Eğer bilgiler eksiksizse başarılı bir ödeme mesajı göster ve SuccessFragment'e geçiş yap.
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToSuccessFragment())
                } else {
                    // Eksik bilgi varsa kullanıcıyı uyar.
                    Toast.makeText(requireContext(), "Missing Fields !", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Girilen bilgilerin eksiksiz olup olmadığını kontrol eden fonksiyon.
    private fun checkInfos(
        owner: String,
        number: String,
        selectedMonth: String,
        selectedYear: String,
        cvv: String,
        addressName: String,
        addressDesc: String
    ): Boolean {
        // Bilgilerin eksik olup olmadığını kontrol et ve sonucu döndür.
        val checkInfos = when {
            owner.isNullOrEmpty() -> false
            number.isNullOrEmpty() -> false
            selectedMonth.isNullOrEmpty() -> false
            selectedYear.isNullOrEmpty() -> false
            cvv.isNullOrEmpty() -> false
            addressName.isNullOrEmpty() -> false
            addressDesc.isNullOrEmpty() -> false
            else -> true
        }
        return checkInfos
    }
}
