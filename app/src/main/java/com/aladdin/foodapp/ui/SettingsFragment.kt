package com.aladdin.foodapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.aladdin.foodapp.BuildConfig
import com.aladdin.foodapp.R
import com.aladdin.foodapp.databinding.FragmentSettingsBinding
import com.aladdin.foodapp.utils.MySharedPreference
import com.vicmikhailau.maskededittext.MaskedEditText


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        if (MySharedPreference.phoneNumber == null) {
            binding.edit.visibility = View.GONE
        } else {
            binding.edit.visibility = View.VISIBLE
        }

        binding.number.text = MySharedPreference.phoneNumber

        binding.edit.setOnClickListener {
            phone()
        }

        binding.click.setOnClickListener {
            findNavController().navigate(
                R.id.aboutBurgerFragment,
                Bundle(),
                setAnimation().build()
            )
        }

        binding.click2.setOnClickListener {
            language()
        }

        binding.click3.setOnClickListener {
            openLink()
        }
        binding.click4.setOnClickListener {
            share()
        }

        binding.click5.setOnClickListener {
            about()
        }



        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun about() {
        val dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.about_dialog, null, false)
        dialog.setView(view)



        view.findViewById<TextView>(R.id.version_tv).text = "Version code: " + BuildConfig.VERSION_CODE
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()


    }

    private fun language() {

        val dialog = AlertDialog.Builder(binding.root.context).create()
        val view = LayoutInflater.from(binding.root.context)
            .inflate(R.layout.language_dialog, null, false)
        dialog.setView(view)


        /*      val button12 = view.findViewById<RadioButton>(MySharedPreference.language!!)

              button12.isChecked = true*/

        val radioButton = view.findViewById<RadioGroup>(R.id.radio)

//            val button = view.findViewById<RadioButton>(R.id.radio)
        val button1 = view.findViewById<RadioButton>(R.id.uzbek)
        val button2 = view.findViewById<RadioButton>(R.id.ru)

        when (MySharedPreference.languageId) {
            1 ->  button1.isChecked = true
            2 -> button2.isChecked = true
        }

        button1.setOnCheckedChangeListener { _, p1 ->

            if (p1) {
                MySharedPreference.languageId = 1
                MySharedPreference.language = "uz"
             //   binding.hdText.text = MySharedPreference.language
            }

        }
        button2.setOnCheckedChangeListener { _, p1 ->

            if (p1) {
                MySharedPreference.languageId = 2
                MySharedPreference.language = "ru"
               //    binding.hdText.text = MySharedPreference.language
            }

        }


        dialog.setOnCancelListener {


            val checkedRadioButtonId = radioButton.checkedRadioButtonId


            //


            println("alovuddin: $checkedRadioButtonId")


        }
        view.findViewById<View>(R.id.cancel_btn).setOnClickListener {

            dialog.cancel()

        }
        dialog.setContentView(view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun phone() {
        val dialog = AlertDialog.Builder(binding.root.context).create()
        val view =
            LayoutInflater.from(binding.root.context).inflate(R.layout.phone_dialog, null, false)
        dialog.setView(view)
        dialog.setContentView(view)

        view.findViewById<MaskedEditText>(R.id.phone2)
        view.findViewById<View>(R.id.btn_card2).setOnClickListener {


            if (view.findViewById<MaskedEditText>(R.id.phone2).text.toString().length == 19) {

                MySharedPreference.phoneNumber =
                    view.findViewById<MaskedEditText>(R.id.phone2).text.toString()

                binding.number.text = MySharedPreference.phoneNumber
                dialog.dismiss()
                Toast.makeText(
                    binding.root.context,
                    "Raqam saqlandi!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()

            } else {
                Toast.makeText(
                    binding.root.context,
                    "Iltimos, raqamni to'g'ri kiriting!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }

        }

        dialog.dismiss()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun setAnimation(): NavOptions.Builder {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.exit_anim)
            .setExitAnim(R.anim.pop_enter_anim)
            .setPopEnterAnim(R.anim.enter_anim)
            .setPopExitAnim(R.anim.pop_exit_anim)
    }

    private fun share() {


        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nDo'slaringizga ham osh bo'lsin :)\n\n"
            shareMessage =
                """
                ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                
                
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }

    }

    private fun openLink(){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/oshbolsinsupportbot"))
        startActivity(browserIntent)
    }

}