package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.example.myapplication.adapters.MovieAdapter
import com.example.myapplication.databinding.FragmentFirstBinding
import com.example.myapplication.viewmodels.MoviesViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()
    lateinit var adapter: MovieAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
      return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPopularMovies(pageNo = 1)
        adapter = MovieAdapter(onItemClick = {
            // todo @RITESH MOVE TO DETAILS FRAGEMNT WITH NECCESSARY ARGS
        })
        binding.rvMovies.adapter = adapter
        binding.rvMovies.layoutManager= LinearLayoutManager(requireContext())
        viewModel.movies.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.pbLoading.visibility = View.GONE
        }

    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}