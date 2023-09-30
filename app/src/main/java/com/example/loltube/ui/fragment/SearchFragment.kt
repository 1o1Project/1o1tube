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
import com.example.loltube.R
import com.example.loltube.data.RetrofitInstance
import com.example.loltube.databinding.FragmentSearchBinding
import com.example.loltube.model.LOLModel
import com.example.loltube.ui.adapter.SearchAdapter
import com.example.loltube.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), SearchAdapter.OnItemClickListener {

    // lateinit 대신 by lazy 로 변경
    private var _binding: FragmentSearchBinding? = null
    private val binding:FragmentSearchBinding get() = _binding!!
    private val adapter: SearchAdapter by lazy {
        SearchAdapter(requireContext())
    }
    private val gridmanager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val resItems: ArrayList<LOLModel> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        //그리드뷰 배치
        binding.fragmentSearchRecyclerView.layoutManager = gridmanager

        //어댑터 연결
        binding.fragmentSearchRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)
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
                Toast.makeText(requireContext(), "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            //키보드 숨기기
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.keyWord.windowToken, 0)
        }

    }


    //유튜브 검색 API
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
                    val description =snippet.snippet.description
                    resItems.add(LOLModel(title = title, thumbnail = url, description =  description, url = url))
                }
            }

            adapter.items = resItems
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            //네트워크 오류 예외처리
            Log.e("#error check", "Error: ${e.message}")
        }
    }

    // 아이템 클릭 이벤트 처리
    override fun onItemClick(item: LOLModel) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.EXTRA_ITEM, item)// 아이템 데이터를 번들에 담음

        val destinationFragment = VideoDetailFragment() // 전환할 Fragment
        destinationFragment.arguments = bundle

        // Fragment 전환
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment_frame, destinationFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}