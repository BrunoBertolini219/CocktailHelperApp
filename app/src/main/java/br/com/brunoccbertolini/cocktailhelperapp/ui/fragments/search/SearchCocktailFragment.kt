package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.SearchCocktailFragmentBinding
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.SEARCH_COCKTAIL_TIME_DELAY
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.searchName
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchCocktailFragment : Fragment() {

    val TAG = "SearchCocktailFragment"

    private lateinit var connectionLivedata: ConnectionLiveData
    private val viewModel: SearchCocktailViewModel by viewModels()
    private lateinit var cocktailAdapter: CocktailListAdapter

    private var _viewBinding: SearchCocktailFragmentBinding? = null
    private val viewBinding: SearchCocktailFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _viewBinding = SearchCocktailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search Drinks"

        setupRecyclerView()

        cocktailAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(
                R.id.action_searchCocktailFragment_to_detailFragment,
                bundle
            )
        }

        connectionLivedata = ConnectionLiveData(requireContext())
        connectionLivedata.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable) {
                var job: Job? = null
                viewBinding.etSearch.addTextChangedListener { editable ->
                    job?.cancel()
                    job = viewLifecycleOwner.lifecycleScope.launch {
                        delay(SEARCH_COCKTAIL_TIME_DELAY)
                        editable?.let {
                            if (it.toString().isNotEmpty()) {
                                viewModel.searchCocktail(it.toString(), searchName)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "No Internet Connection Available!", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchCocktail.collectLatest { response ->
                    response ?: return@collectLatest
                    when (response) {
                        is Resource.Success -> {
                            hideProgressBar()
                            response.data?.let {
                                cocktailAdapter.differ.submitList(it.drinks)
                            }
                        }
                        is Resource.Error -> {
                            hideProgressBar()
                            response.message?.let { message ->
                                Log.e(TAG, "An error occurred: $message")
                            }
                        }
                        is Resource.Loading -> showProgressBar()
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        cocktailAdapter = CocktailListAdapter()
        viewBinding.rvSearchDrink.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = cocktailAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
