package com.example.loltube.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.data.RetrofitInstance.api
import com.example.loltube.databinding.FragmentSearchBinding
import com.example.loltube.model.SearchItemModel
import com.example.loltube.model.YoutubeVideo
import com.example.loltube.ui.adapter.SearchAdapter
import com.example.loltube.util.Constants.Companion.AUTH_HEADER
import com.example.loltube.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mConText: Context
    private lateinit var adapter: SearchAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager

    private val resItems: ArrayList<SearchItemModel> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mConText = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    private fun initView() {
        //그리드뷰 배치
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.fragmentSearchRecyclerView.layoutManager = gridmanager

        //어댑터 연결
        adapter = SearchAdapter(mConText)
        binding.fragmentSearchRecyclerView.adapter = adapter

        //리스너 설정
        binding.searchBtn.setOnClickListener {
            val query = binding.keyWord.text.toString()

            if (query.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {
                    val query = query
                    adapter.itemClear()
                    fetchItemResults(query)
                }

            } else {
                Toast.makeText(mConText, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            //키보드 숨기기
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.keyWord.windowToken, 0)
        }
    }


    private suspend fun fetchItemResults(query: String) {
        try {
            val response = RetrofitInstance.api.getYouTubeVideos(
                query = query,
                maxResults = 30,
                videoOrder = "relevance"
            )

            if (response.isSuccessful) {
                val youtubeVideo = response.body()!!
                youtubeVideo?.items?.forEach { snippet ->
                    val title = snippet.snippet.title
                    val url = snippet.snippet.thumbnails.medium.url
                    resItems.add(SearchItemModel(title, url))
                }
            }

            adapter.items = resItems
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("#error check", "Error: ${e.message}")
        }
    }
}