package com.example.lapaksantri.presentation.home

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.lapaksantri.R
import com.example.lapaksantri.databinding.FragmentHomeBinding
import com.example.lapaksantri.utils.gone
import com.example.lapaksantri.utils.showErrorSnackbar
import com.example.lapaksantri.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var dots: ArrayList<TextView>
    private var listSliderSize = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dots = ArrayList()

        sliderAdapter = SliderAdapter()
        articleAdapter = ArticleAdapter{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToArticleFragment(it))
        }

        binding.tvTime.text = viewModel.getTime()
        binding.btnCart.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
        }

        observeErrorSnackbar()
        observeName()
        setupViewPager()
        setupInformation()
    }

    private fun observeErrorSnackbar() {
        viewModel.errorSnackbar
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
                showErrorSnackbar(binding.root, it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeName() {
        viewModel.name
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutName.startShimmer()
                } else {
                    binding.shimmerLayoutName.stopShimmer()
                    binding.shimmerLayoutName.gone()
                    binding.tvName.text = state.data
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = sliderAdapter

        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(24))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.95f + r * 0.05f
        }

        binding.viewPager.setPageTransformer(compositePageTransformer)

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            var index = 0
            override fun run() {
                if (index == listSliderSize) {
                    index = 0
                }
                binding.viewPager.currentItem = index
                index++
                handler.postDelayed(this, 2000)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedDot(position)
                super.onPageSelected(position)
            }
        })

        viewModel.sliders
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutSlider.startShimmer()
                } else {
                    binding.shimmerLayoutSlider.stopShimmer()
                    binding.shimmerLayoutSlider.gone()
                    binding.viewPager.visibility = View.VISIBLE
                    sliderAdapter.submitList(state.data)
                    state.data?.size?.let { listSliderSize = it }
                    setIndicator()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun selectedDot(position: Int) {
        for (i in 0 until listSliderSize) {
            if (i == position) {
                dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_green))
            } else {
                dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey))
            }
        }
    }

    private fun setIndicator() {
        for (i in 0 until listSliderSize) {
            dots.add(TextView(context))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_COMPACT).toString()
            } else {
                dots[i].text = Html.fromHtml("&#9679").toString()
            }
            dots[i].textSize = 15f
            binding.linearLayoutDotsIndicator.addView(dots[i])
        }
    }

    private fun setupInformation() {
        binding.rvInformation.adapter = articleAdapter
        binding.rvInformation.layoutManager = LinearLayoutManager(context)
        viewModel.articles
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                if (state.isLoading) {
                    binding.shimmerLayoutArticle.startShimmer()
                } else {
                    binding.shimmerLayoutArticle.stopShimmer()
                    binding.shimmerLayoutArticle.gone()
                    binding.rvInformation.visible()
                    articleAdapter.submitList(state.data)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        handler.post(runnable)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}