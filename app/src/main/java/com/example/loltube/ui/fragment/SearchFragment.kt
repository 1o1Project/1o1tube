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
import androidx.recyclerview.widget.RecyclerView
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
    private val binding: FragmentSearchBinding get() = _binding!!
    private val adapter: SearchAdapter by lazy {
        SearchAdapter(requireContext())
    }
    private val gridmanager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val resItems: ArrayList<LOLModel> = ArrayList()
    private var query:String =""

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
        setupScrollListener()
    }

    private fun initView() {
        //그리드뷰 배치
        binding.fragmentSearchRecyclerView.layoutManager = gridmanager

        //어댑터 연결
        binding.fragmentSearchRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(this)

        //리스너 설정
        binding.searchBtn.setOnClickListener {
            query = binding.keyWord.text.toString()

            if (query.isNotEmpty()) {
                GlobalScope.launch(Dispatchers.Main) {

                    adapter.itemClear()
                    fetchItemResults()
                }
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            //키보드 숨기기
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.keyWord.windowToken, 0)
        }

        //카테고리 Top 리스너 설정
        binding.topBtn.setOnClickListener{

            GlobalScope.launch(Dispatchers.Main) {
                query = "탑-롤"
  //              binding.fragmentSearchRecyclerView.removeOnScrollListener(listnenr)

                adapter.itemClear()
                fetchItemResults()
            }
        }
        binding.jgBtn.setOnClickListener{

            GlobalScope.launch(Dispatchers.Main) {
                query = "정글-롤"
                adapter.itemClear()
                fetchItemResults()
            }
        }
        binding.midBtn.setOnClickListener{

            GlobalScope.launch(Dispatchers.Main) {
                query = "미드-롤"
                adapter.itemClear()
                fetchItemResults()
            }
        }
        binding.adBtn.setOnClickListener{

            GlobalScope.launch(Dispatchers.Main) {
                query = "원딜-롤"
                adapter.itemClear()
                fetchItemResults()
            }
        }
        binding.supBtn.setOnClickListener{

            GlobalScope.launch(Dispatchers.Main) {
                query = "서폿-롤"
                adapter.itemClear()
                fetchItemResults()
            }
        }
    }

    private lateinit var currenttoken: String

    //유튜브 검색
    private suspend fun fetchItemResults() {
        try {
            val response = RetrofitInstance.api.getYouTubeVideos(
                query = query,
                maxResults = 10,
                videoOrder = "relevance"
            )

            if (response.isSuccessful) {
                val youtubeVideo = response.body()!!
                youtubeVideo?.items?.forEach { snippet ->
                    val title = snippet.snippet.title
                    val url = snippet.snippet.thumbnails.medium.url
                    val description =snippet.snippet.description
                    resItems.add(LOLModel(title = title, thumbnail = url, description =  description, url = "https://www.youtube.com/watch?v=${snippet.id}"))
                }
            }

            currenttoken = response.body()!!.nextPageToken

            adapter.items = resItems
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            //네트워크 오류 예외처리
            Log.e("#error check", "Error: ${e.message}")
        }
    }

    /*private suspend fun fetchCategoryResults(category:String){
        try {
            val response = RetrofitInstance.api.getCategory(
                videoCategoryId = category, maxResults = 20
            )

            if (response.isSuccessful){
                val youtubeVideo = response.body()!!
                youtubeVideo?.items?.forEach { snippet ->
                    val title = snippet.snippet.title
                    val url = snippet.snippet.thumbnails.medium.url
                    val description = snippet.snippet.description
                    resItems.add(
                        LOLModel(
                            title = title,
                            thumbnail = url,
                            description = description
                        )
                    )
                }
            }
        }catch (e: Exception) {
            //네트워크 오류 예외처리
            Log.e("#error check", "Error: ${e.message}")
        }
    }*/

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

    // 무한 스크롤 구현
    private var isLoading = false // 추가 데이터 로드 중인지 여부를 나타내는 플래그
    private val visibleThreshold = 10 // 스크롤 끝까지 내려갔다고 판단할 아이템 개수
    private var lastVisibleItem = 0
    private var totalItemCount = 0
    private var visibleItemCount = 0

    private lateinit var listnenr : RecyclerView.OnScrollListener

    private fun setupScrollListener() {
        listnenr = object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = gridmanager.itemCount
                visibleItemCount = gridmanager.childCount

                val firstVisibleItemPositions = gridmanager.findFirstVisibleItemPositions(null)

                if (firstVisibleItemPositions != null && firstVisibleItemPositions.isNotEmpty()) {
                    lastVisibleItem = firstVisibleItemPositions[0]
                }

                // 추가 데이터 로드 중이 아니고, 스크롤이 끝까지 내려갔을 때
                if (!isLoading && totalItemCount <= (visibleItemCount + lastVisibleItem + visibleThreshold)) {
                    // 추가 데이터 로드를 위한 함수
                    fetchMoreData()
                    Log.d("query",query)
                    isLoading = true
                }
            }
        }
        binding.fragmentSearchRecyclerView.addOnScrollListener(listnenr)
    }

    private fun fetchMoreData() {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = RetrofitInstance.api.getYouTubeMoreVideos(
                    query = query,
                    maxResults = 10,
                    videoOrder = "relevance",
                    nextPageToken = currenttoken
                )

                if (response.isSuccessful) {
                    val youtubeVideo = response.body()!!
                    youtubeVideo?.items?.forEach { snippet ->
                        val title = snippet.snippet.title
                        val url = snippet.snippet.thumbnails.medium.url
                        val description = snippet.snippet.description
                        resItems.add(
                            LOLModel(
                                title = title,
                                thumbnail = url,
                                url = "https://www.youtube.com/watch?v=${snippet.id}",
                                description = description))
                    }

                    adapter.items=resItems
                    // 새로운 데이터를 RecyclerView에 추가
                    adapter.notifyDataSetChanged()

                    currenttoken = response.body()!!.nextPageToken

                    isLoading = false
                }
            } catch (e: Exception) {
                // 네트워크 오류 예외처리
                Log.e("#error check", "Error: ${e.message}")
                isLoading = false // 오류 발생 시 isLoading 플래그를 false로 설정하여 다시 시도할 수 있도록 함
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}