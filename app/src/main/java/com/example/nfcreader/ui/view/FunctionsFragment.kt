package com.example.nfcreader.ui.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nfcreader.databinding.FragmentFunctionsBinding
import com.example.nfcreader.ui.viewModel.FunctionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FunctionsFragment : Fragment() {

    private var _binding: FragmentFunctionsBinding? = null
    private val binding get() = _binding!!
    private val functionsViewModel: FunctionsViewModel by viewModels()

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

        initObservers()
        initViewListeners()
//        functionsViewModel.connectToCardReader()
    }

    private fun initObservers() {
        binding.transactionLogTextView.movementMethod = ScrollingMovementMethod()
        functionsViewModel.functionsResult.observe(viewLifecycleOwner) {
            binding.transactionLogTextView.text = it
        }
    }

    private fun initViewListeners() {
        binding.initializeButton.setOnClickListener {
            Log.i("initializeButton", "Pressed")
            functionsViewModel.increaseCardLevel()
        }
    }
}