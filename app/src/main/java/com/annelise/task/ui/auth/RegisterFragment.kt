package com.annelise.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.annelise.task.R
import com.annelise.task.databinding.FragmentRegisterBinding
import com.annelise.task.util.initToolbar
import com.annelise.task.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
    }

    private fun initListener(){
        binding.buttonCriarconta.setOnClickListener{
            valideData()
        }
    }

    private fun valideData(){
        val email = binding.editTextEmail.text.toString().trim()
        val senha = binding.editTextSenha.text.toString().trim()
        if(email.isNotBlank()) {
            if (senha.isNotBlank()) {
                binding.progressbar.isVisible=true
                registerUser(email, senha)
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                showBottomSheet(message = getString(R.string.password_empty_register_fragment))
            }
        }else{
            showBottomSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }


    private fun registerUser(email: String, password: String){
        try {
            val auth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        //mensagem de sucesso para cadastro do usuário
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }else{
                        //mensagem de erro para cadastro do usuário
                        Toast.makeText(requireContext(), task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

        }catch (e: Exception){
            Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}