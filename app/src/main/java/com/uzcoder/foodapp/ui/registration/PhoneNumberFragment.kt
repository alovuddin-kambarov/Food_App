package com.uzcoder.foodapp.ui.registration

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.ktx.Firebase
import com.uzcoder.foodapp.R
import com.uzcoder.foodapp.databinding.FragmentAboutBurgerBinding
import com.uzcoder.foodapp.databinding.FragmentPhoneNumberBinding
import com.uzcoder.foodapp.utils.MyData
import java.util.concurrent.TimeUnit


class PhoneNumberFragment : Fragment(R.layout.fragment_phone_number) {

    private lateinit var timer: CountDownTimer
    lateinit var auth: FirebaseAuth
    var isCodeSend = false
    lateinit var storedVerificationId: String
    lateinit var dialog: AlertDialog
    lateinit var number: String
    private var _binding:FragmentPhoneNumberBinding? = null

    private val binding: FragmentPhoneNumberBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPhoneNumberBinding.bind(view)


        auth = FirebaseAuth.getInstance()

        binding.btnCard.setOnClickListener {

            var phoneNumber = binding.phone.text.toString()
            phoneNumber = phoneNumber.replace("(", "", true)
            phoneNumber = phoneNumber.replace(")", "", true)
            phoneNumber = phoneNumber.replace(" ", "", true)
            phoneNumber = phoneNumber.replace("-", "", true)
            number = phoneNumber

      /*      sentVerificationCode(phoneNumber)
            closeKerBoard()
            setProgress()*/
            findNavController().navigate(R.id.verificationCodeFragment)
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            dialog.cancel()
            println("lavash: ${e.message}")
            if (e is FirebaseAuthInvalidCredentialsException) {
                dialog.cancel()
                println("lavash: ${e.message}")
            } else if (e is FirebaseTooManyRequestsException) {
                dialog.cancel()
                println("lavash2: ${e.message}")
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            MyData.resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information


                val bundle = Bundle()
                bundle.putString("number2", number)

                dialog.cancel()

                val name = arguments?.getString("name")
                val lastName = arguments?.getString("lastName")
                val email = arguments?.getString("email")
                val password = arguments?.getString("password")
/*
                val user = User(
                    name,
                    lastName,
                    number,
                    email, password,
                    MySharedPreference.region,
                    MySharedPreference.country,
                    MySharedPreference.streetNumber
                )


                val database = Firebase.database
                val myRef = database.getReference("users/$number")
                myRef.setValue(user)

                Handler(Looper.myLooper()!!).postDelayed({

                    startActivity(Intent(binding.root.context, MainActivity::class.java))
                    timer.cancel()
                    activity?.finish()

                }, 1000)*/

            } else {
                // Sign in failed, display a message and update the UI

                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Snackbar.make(binding.root, "Kiritilgan kod noto'g'ri!", Snackbar.LENGTH_LONG)
                        .show()
                }
                // Update UI
            }
        }
    }


    private fun sentVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }



    private fun setProgress() {
        dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.custom_progress, null, false)
        dialog.setView(view)
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun closeKerBoard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


}