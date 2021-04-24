package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.CocktailListAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.SavedCocktailFragmentBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository

class SavedCocktailFragment : Fragment() {

    private lateinit var viewModel: SavedCocktailViewModel
    lateinit var adapterCocktail: CocktailListAdapter

    private var _viewBinding: SavedCocktailFragmentBinding? = null
    private val viewBinding: SavedCocktailFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = SavedCocktailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val repository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val savedFactory = SavedCocktailViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, savedFactory).get(SavedCocktailViewModel::class.java)
        setupRecyclerView()
        setupObservers()

        adapterCocktail.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(R.id.action_savedCocktailFragment_to_detailFragment, bundle)
        }



    }

    private fun setupObservers() {
        viewModel.getSavedCocktails().observe(viewLifecycleOwner, { cocktail ->
            adapterCocktail.differ.submitList(cocktail)
        })
    }


    private fun setupRecyclerView() {
        adapterCocktail = CocktailListAdapter()
        viewBinding.rvCocktailList.apply {
            adapter = adapterCocktail
            hasFixedSize()
            layoutManager = GridLayoutManager(this@SavedCocktailFragment.requireContext(), 2)
        }
    }

}