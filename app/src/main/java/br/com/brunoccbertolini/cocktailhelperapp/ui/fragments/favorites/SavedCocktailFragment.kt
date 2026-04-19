package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.adapter.SavedCocktailAdapter
import br.com.brunoccbertolini.cocktailhelperapp.databinding.SavedCocktailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedCocktailFragment : Fragment() {

    private val viewModel: SavedCocktailViewModel by viewModels()
    private lateinit var adapterCocktail: SavedCocktailAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Favorite Drinks"

        setupRecyclerView()

        adapterCocktail.setOnDeleteItemClickListener { drink ->
            AlertDialog.Builder(context).apply {
                setTitle("Discard Drink")
                setMessage("This will remove ${drink.strDrink} from your favorite drinks.")
                setPositiveButton("Remove") { _, _ ->
                    viewModel.deleteSavedCocktail(drink)
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedCocktails.collectLatest { cocktails ->
                    adapterCocktail.differ.submitList(cocktails)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapterCocktail = SavedCocktailAdapter()
        viewBinding.rvCocktailList.apply {
            adapter = adapterCocktail
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
