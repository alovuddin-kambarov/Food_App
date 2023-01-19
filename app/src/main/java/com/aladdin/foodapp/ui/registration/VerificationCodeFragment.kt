package com.aladdin.foodapp.ui.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aladdin.foodapp.R
import com.aladdin.foodapp.databinding.FragmentVerificationCodeBinding
import com.wynsbin.vciv.VerificationCodeInputView.OnInputListener


class VerificationCodeFragment : Fragment(R.layout.fragment_verification_code) {

    private var _binding: FragmentVerificationCodeBinding? = null

    private val binding: FragmentVerificationCodeBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVerificationCodeBinding.bind(view)



        binding.vcivCode.setOnInputListener(object : OnInputListener {
            override fun onComplete(code: String) {
                Toast.makeText(binding.root.context, code, Toast.LENGTH_SHORT).show()
            }

            override fun onInput() {}
        })

        binding.vcivCode.clearCode()


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}