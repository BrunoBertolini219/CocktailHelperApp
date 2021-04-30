package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.SearchCocktailFragmentBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.SEARCH_COCKTAIL_TIME_DELAY
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchCocktailFragment : Fragment() {

    val TAG = "SearchCocktailFragment"

    private lateinit var viewModel: SearchCocktailViewModel
    private lateinit var cocktailAdapter: CocktailListAdapter

    private var _viewBinding: SearchCocktailFragmentBinding? = null
    private val viewBinding: SearchCocktailFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _viewBinding = SearchCocktailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().title = "Search Drinks"
        val repository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val searchViewModelFactory = SearchProviderViewModelFactory(repository)
        viewModel =
            ViewModelProvider(this, searchViewModelFactory).get(SearchCocktailViewModel::class.java)
        setupRecyclerView()
        setupObservers()

        cocktailAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(R.id.action_searchCocktailFragment_to_detailFragment, bundle)
        }

        var job: Job? = null
        viewBinding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_COCKTAIL_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchCocktail(editable.toString())
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.searchCocktail.observe(viewLifecycleOwner, { response ->
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
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
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
            layoutManager = GridLayoutManager(this@SearchCocktailFragment.requireContext(), 2)
            adapter = cocktailAdapter
            hasFixedSize()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

}