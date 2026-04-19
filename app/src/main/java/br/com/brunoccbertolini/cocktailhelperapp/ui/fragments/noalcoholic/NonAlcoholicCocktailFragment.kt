package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.NonAlcoholicCocktailFragmentBinding
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NonAlcoholicCocktailFragment : Fragment() {

    private val TAG = "NonAlcoholicCocktail"
    private var _viewBinding: NonAlcoholicCocktailFragmentBinding? = null
    private val viewBinding: NonAlcoholicCocktailFragmentBinding get() = _viewBinding!!
    private lateinit var connectionLivedata: ConnectionLiveData
    private val viewModel: NonAlcoholicCocktailViewModel by viewModels()
    private lateinit var cocktailListAdapter: CocktailListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = "Non-Alcoholic Drinks"
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _viewBinding = NonAlcoholicCocktailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupOnClick()

        connectionLivedata = ConnectionLiveData(requireContext())
        connectionLivedata.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable) {
                viewModel.getNonAlcoholicCocktails()
            } else {
                Toast.makeText(requireContext(), "No Internet Connection Available!", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cocktailNoAlcoholic.collectLatest { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let { list ->
                                cocktailListAdapter.differ.submitList(list.drinks)
                                hideProgressBar()
                            }
                        }
                        is Resource.Error -> {
                            response.message?.let { message ->
                                Log.e(TAG, "An Error Occurred $message")
                                hideProgressBar()
                            }
                        }
                        is Resource.Loading -> showProgressBar()
                    }
                }
            }
        }
    }

    private fun setupOnClick() {
        cocktailListAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(R.id.action_cocktailFragment_to_detailFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        cocktailListAdapter = CocktailListAdapter()
        viewBinding.rvCocktailList.apply {
            adapter = cocktailListAdapter
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
