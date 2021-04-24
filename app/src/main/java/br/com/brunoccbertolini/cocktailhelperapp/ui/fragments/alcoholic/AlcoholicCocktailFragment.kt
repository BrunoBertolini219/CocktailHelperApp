package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentAlcoholicCocktailBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource


class AlcoholicCocktailFragment : Fragment() {


    private var _bindingView: FragmentAlcoholicCocktailBinding? = null
    private val bindingView: FragmentAlcoholicCocktailBinding get() = _bindingView!!

    private lateinit var viewModelAlcoholic: AlcoholicCocktailViewModel
    private lateinit var adapterCocktail: CocktailListAdapter

    val TAG = "CocktailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingView = FragmentAlcoholicCocktailBinding.inflate(inflater, container, false)
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cockTailRepository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val viewModelProviderFactory = AlcoholicCocktailViewModelProviderFactory(cockTailRepository)
        viewModelAlcoholic = ViewModelProvider(this, viewModelProviderFactory).get(
            AlcoholicCocktailViewModel::class.java)
        setupRecyclerView()
        setupObservers()

        adapterCocktail.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(R.id.action_alcoholicCocktailFragment_to_detailFragment,
            bundle)
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
                        Log.e(TAG, "An Error Occured $message", )
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
        bindingView.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        bindingView.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        adapterCocktail = CocktailListAdapter()
        bindingView.rvCocktailList.apply {
            adapter = adapterCocktail
            hasFixedSize()
            layoutManager = GridLayoutManager(this@AlcoholicCocktailFragment.requireContext(), 2)
        }
    }
}
