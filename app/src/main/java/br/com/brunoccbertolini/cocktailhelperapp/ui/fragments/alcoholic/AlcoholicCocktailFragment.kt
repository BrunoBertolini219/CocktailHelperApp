package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

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
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentAlcoholicCocktailBinding
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlcoholicCocktailFragment : Fragment() {

    private var _viewBinding: FragmentAlcoholicCocktailBinding? = null
    private val viewBinding: FragmentAlcoholicCocktailBinding get() = _viewBinding!!

    private val viewModelAlcoholic: AlcoholicCocktailViewModel by viewModels()

    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var adapterCocktail: CocktailListAdapter

    val TAG = "CocktailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = "Alcoholic Drinks"
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _viewBinding = FragmentAlcoholicCocktailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        adapterCocktail.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(
                R.id.action_cocktailFragment_to_detailFragment,
                bundle
            )
        }

        connectionLiveData = ConnectionLiveData(requireContext())
        connectionLiveData.observe(viewLifecycleOwner) { isAvailable ->
            if (isAvailable) {
                viewModelAlcoholic.getAlcoholicCocktails()
            } else {
                Toast.makeText(requireContext(), "No Internet Connection Available!", Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelAlcoholic.cocktailAlcoholic.collectLatest { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data?.let { cocktailResponse ->
                                adapterCocktail.differ.submitList(cocktailResponse.drinks)
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

    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        adapterCocktail = CocktailListAdapter()
        viewBinding.rvCocktailList.apply {
            adapter = adapterCocktail
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
