package com.example.loltube.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.loltube.databinding.FragmentShortsBinding
import com.example.loltube.ui.adapter.ShortsListAdapter
import com.example.loltube.ui.viewmodel.ShortsViewModel
import com.example.loltube.ui.viewmodel.ShortsViewModelFactory
import com.example.loltube.ui.viewmodel.UiState

class ShortsFragment : Fragment() {

    private var _binding: FragmentShortsBinding? = null
    private val binding: FragmentShortsBinding
        get() = _binding!!

    private val viewModel: ShortsViewModel by viewModels { ShortsViewModelFactory() }

    private val adapter by lazy {
        ShortsListAdapter(lifecycle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShortsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.shortsRv.adapter = adapter
        binding.shortsRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.shortsRv.apply {
            itemAnimator = null
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount
                    if (lastVisibleItemPosition >= itemTotalCount - 1) {
                        viewModel.getNextShorts()
                    }
                }
            })
        }
        PagerSnapHelper().attachToRecyclerView(binding.shortsRv)

        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {}
                is UiState.Success -> {
                    val recyclerViewState = binding.shortsRv.layoutManager?.onSaveInstanceState()
                    adapter.setList(it.data)
                    binding.shortsRv.layoutManager?.onRestoreInstanceState(recyclerViewState)
                }

                is UiState.Error -> {}
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}