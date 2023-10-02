package com.example.loltube.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.loltube.data.SharedPrefInstance
import com.example.loltube.databinding.FragmentVideoDetailBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.util.Constants.Companion.EXTRA_ITEM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoDetailFragment : Fragment() {

    companion object {
        fun newInstance(item: LOLModel?) : Fragment{
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_ITEM, item)

            val fragment = VideoDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var item : LOLModel? = null

    private var _binding : FragmentVideoDetailBinding? = null
    private val binding : FragmentVideoDetailBinding
        get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoDetailBinding.inflate(inflater, container, false)
        val bundle = arguments
        item = bundle?.getParcelable(EXTRA_ITEM, LOLModel::class.java)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initView() {

        binding.detailTitle.text = item?.title
        binding.detailDescription.text = item?.description

        item?.thumbnail?.let { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.detailThumbnail)
        }

        val sharedPrefInstance = SharedPrefInstance.getInstance()
        lifecycleScope.launch(Dispatchers.Main) {
            val isBookmarked = withContext(Dispatchers.IO) {
                sharedPrefInstance.isBookmark(item!!)
            }
            updateButtonState(isBookmarked)
        }


        binding.likeButton.setOnClickListener {

            lifecycleScope.launch {
                item?.let {
                    val isBookmarked = SharedPrefInstance.getInstance().isBookmark(it)

                    if (!isBookmarked) {
                        SharedPrefInstance.getInstance().addBookmarkPref(it)
                        showToast("myvideo에 추가되었습니다.")
                        updateButtonState(true)

                    }
                }
            }
        }

        binding.unlikeButton.setOnClickListener {

            lifecycleScope.launch {
                item?.let {
                    val isBookmarked = SharedPrefInstance.getInstance().isBookmark(it)

                    if (isBookmarked) {
                        SharedPrefInstance.getInstance().deleteBookmarkPref(it)
                        showToast("myvideo에서 삭제됩니다.")
                        updateButtonState(false)

                    }

                }
            }
        }



        binding.shareButton.setOnClickListener {
            val videoUrl = item?.url

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, videoUrl)

            startActivity(Intent.createChooser(shareIntent, "비디오 공유"))
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateButtonState(isLiked: Boolean) {
        if (isLiked) {
            binding.likeButton.visibility = View.INVISIBLE
            binding.unlikeButton.visibility = View.VISIBLE
        } else {
            binding.likeButton.visibility = View.VISIBLE
            binding.unlikeButton.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}