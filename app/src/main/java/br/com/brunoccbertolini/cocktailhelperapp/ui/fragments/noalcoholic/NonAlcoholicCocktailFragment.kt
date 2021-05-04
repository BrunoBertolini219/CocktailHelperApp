package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.NonAlcoholicCocktailFragmentBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import retrofit2.Response


class NonAlcoholicCocktailFragment : Fragment() {

    private val TAG = "NonAlcoholicCocktail"
    private var _viewBinding: NonAlcoholicCocktailFragmentBinding? = null
    private val viewBinding: NonAlcoholicCocktailFragmentBinding get() = _viewBinding!!
    private lateinit var connectionLivedata: ConnectionLiveData
    private lateinit var viewModel: NonAlcoholicCocktailViewModel
    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Non-Alcoholic Drinks"
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _viewBinding = NonAlcoholicCocktailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val repository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val viewModelFactory = NonAlcoholicCocktailViewModelProviderFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(NonAlcoholicCocktailViewModel::class.java)

        connectionLivedata = ConnectionLiveData(this.requireContext())
        connectionLivedata.observe(viewLifecycleOwner, { isAvailable ->
            if (isAvailable) {
                viewModel.getNonAlcoholicCocktails()

                setupObservers()
            } else {
                Toast.makeText(this.requireContext(), "No Internet Connection Available!", Toast.LENGTH_SHORT).show()
            }
        })


        setupRecyclerView()
        setupOnClick()
    }

    private fun setupOnClick() {
        cocktailListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(
                R.id.action_cocktailFragment_to_detailFragment, bundle
            )
        }
    }

    private fun setupObservers() {
        viewModel.cocktailNoAlcoholic.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { list ->
                        cocktailListAdapter.differ.submitList(list.drinks)
                        hideProgressBar()
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e(TAG, "An Error Occured $message")
                        hideProgressBar()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        cocktailListAdapter = CocktailListAdapter()
        viewBinding.rvCocktailList.apply {
            adapter = cocktailListAdapter
            layoutManager =
                GridLayoutManager(this@NonAlcoholicCocktailFragment.requireContext(), 2)
            hasFixedSize()
        }
    }


    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}