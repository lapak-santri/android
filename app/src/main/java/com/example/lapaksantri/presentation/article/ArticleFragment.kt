package com.example.lapaksantri.presentation.article

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.lapaksantri.databinding.FragmentArticleBinding
import com.example.lapaksantri.utils.formatDate

class ArticleFragment : Fragment() {
    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            Glide.with(requireContext())
                .load(args.article.imagePath)
                .into(ivThumbnail)
            tvTitle.text = args.article.title
            tvDate.text = formatDate(args.article.publishedAt)
            tvDesc.text = args.article.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}