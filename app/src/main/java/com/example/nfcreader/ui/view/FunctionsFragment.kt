package com.example.nfcreader.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nfcreader.databinding.FragmentFunctionsBinding
import com.example.nfcreader.ui.viewModel.FunctionsViewModel

class FunctionsFragment : Fragment() {

    private var _binding: FragmentFunctionsBinding? = null
    private val binding get() = _binding!!
    private val functionsViewModel: FunctionsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFunctionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        functionsViewModel.connectToCardReader()
        binding.initializeButton.setOnClickListener {
            functionsViewModel.increaseCardLevel()
        }
    }
}