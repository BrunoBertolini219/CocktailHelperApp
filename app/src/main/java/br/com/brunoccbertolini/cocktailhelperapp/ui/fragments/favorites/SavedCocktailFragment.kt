package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.SavedCocktailAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.SavedCocktailFragmentBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedCocktailFragment : Fragment() {

    private val viewModel: SavedCocktailViewModel by viewModels()
    lateinit var adapterCocktail: SavedCocktailAdapter

    private var _viewBinding: SavedCocktailFragmentBinding? = null
    private val viewBinding: SavedCocktailFragmentBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)
        _viewBinding = SavedCocktailFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().title = "Favorite Drinks"

        setupRecyclerView()
        setupObservers()

        adapterCocktail.setOnDeleteItemClickListener { drink ->
            AlertDialog.Builder(context).apply {
                setTitle("Discard Drink")
                setMessage("This will remove ${drink.strDrink} from your favorite drinks.")
                setPositiveButton("Remove") { char, dialog ->
                    viewModel.deleteSavedCocktail(drink)
                }
                setNegativeButton("Cancel") { text, listener ->
                    text.cancel()
                }
            }.show()


        }

        adapterCocktail.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("drink", it)
            }
            findNavController().navigate(
                R.id.action_savedCocktailFragment_to_detailFragment,
                bundle
            )
        }


    }

    private fun setupObservers() {
        viewModel.getSavedCocktails().observe(viewLifecycleOwner, { cocktail ->
            adapterCocktail.differ.submitList(cocktail)
        })
    }


    private fun setupRecyclerView() {
        adapterCocktail = SavedCocktailAdapter()
        viewBinding.rvCocktailList.apply {
            adapter = adapterCocktail
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@SavedCocktailFragment.requireContext())
        }
    }

}