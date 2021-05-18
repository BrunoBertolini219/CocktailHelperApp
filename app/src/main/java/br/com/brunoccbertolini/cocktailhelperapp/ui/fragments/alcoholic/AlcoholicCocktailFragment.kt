package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentAlcoholicCocktailBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource


class AlcoholicCocktailFragment : Fragment() {


    private var _viewBinding: FragmentAlcoholicCocktailBinding? = null
    private val viewBinding: FragmentAlcoholicCocktailBinding get() = _viewBinding!!

    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var viewModelAlcoholic: AlcoholicCocktailViewModel
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
        val cockTailRepository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val viewModelProviderFactory = AlcoholicCocktailViewModelProviderFactory(cockTailRepository)
        viewModelAlcoholic = ViewModelProvider(this, viewModelProviderFactory).get(
            AlcoholicCocktailViewModel::class.java
        )

        connectionLiveData = ConnectionLiveData(this.requireContext())
        connectionLiveData.observe(viewLifecycleOwner, { isAvailable ->
            if (isAvailable) {
                viewModelAlcoholic.getAlcoholicCocktails()
                setupObservers()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No Internet Connection Available!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

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
    }

    private fun setupObservers() {
        viewModelAlcoholic.cocktailAlcoholic.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { cocktailResponse ->
                        adapterCocktail.differ.submitList(cocktailResponse.drinks)
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
            layoutManager = GridLayoutManager(this@AlcoholicCocktailFragment.requireContext(), 2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}
