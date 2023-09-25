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
import com.example.loltube.data.RetrofitInstance.api
import com.example.loltube.databinding.FragmentSearchBinding
import com.example.loltube.model.SearchItemModel
import com.example.loltube.model.YoutubeVideo
import com.example.loltube.ui.adapter.SearchAdapter
import com.example.loltube.util.Constants.Companion.AUTH_HEADER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private lateinit var mConText: Context
    private lateinit var adapter: SearchAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager

    private val resItems:ArrayList<SearchItemModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mConText = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        //어댑터 연결
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.fragmentSearchRecyclerView.layoutManager = gridmanager

        adapter = SearchAdapter(mConText)
        binding.fragmentSearchRecyclerView.adapter = adapter

        //리스너 설정

        binding.searchBtn.setOnClickListener {
            val query = binding.keyWord.text.toString()

            if (query.isNotEmpty()) {
                adapter.itemClear()

                GlobalScope.launch(Dispatchers.Main) {
                    val query = query
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

    //api 연결하는 부분
    // 코루틴 스코프 내에서 호출할 비동기 함수를 정의합니다.
    private suspend fun fetchItemResults(query: String) {
        try {
            val response: Response<YoutubeVideo> = withContext(Dispatchers.IO) {
                // Retrofit의 API 호출을 suspend 함수로 래핑하고, await() 확장 함수를 사용하여 비동기 응답을 기다립니다.
                api.getYouTubeVideos(AUTH_HEADER, query, "videoOrder", "video", 10, "", "snippet")
            }

            // 응답을 처리합니다.
            if (response.isSuccessful) {
                val youtubeVideo = response.body()
                youtubeVideo?.items?.forEach { snippet ->
                    val title = snippet.snippet.title
                    val url = snippet.snippet.thumbnails.medium.url
                    resItems.add(SearchItemModel(url, title))
                }
            }

            // UI 갱신
            adapter.items = resItems
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            // 오류 처리
            Log.e("#error check", "Error: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}