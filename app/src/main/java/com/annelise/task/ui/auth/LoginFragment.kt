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
import com.annelise.task.databinding.FragmentLoginBinding
import com.annelise.task.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initListener()
    }

    private fun initListener(){
        binding.buttonLogin.setOnClickListener {
            valideData()
        }
        binding.textViewCriarConta.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.textViewRecuperarConta.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }

    private fun valideData(){
        val email = binding.editTextEmail.text.toString().trim()
        val senha = binding.editTextSenha.text.toString().trim()
        if(email.isNotBlank()) {
            if (senha.isNotBlank()) {

                binding.progressbar.isVisible=true
                loginUser(email, senha)

                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                showBottomSheet(message = getString(R.string.password_empty))
            }
        }else{
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    private fun loginUser(email:String, senha:String){
        try {
            auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener {  task ->
                    if (task.isSuccessful){
                        findNavController().navigate(R.id.action_global_homeFragment)
                    }else{
                        binding.progressbar.isVisible=false
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