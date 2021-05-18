package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentDetailBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.model.Drink
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlin.text.Typography.bullet


class DetailFragment : Fragment() {

    private val TAG = "DetailFragment"
    private var _viewBinding: FragmentDetailBinding? = null
    private val viewBinding: FragmentDetailBinding get() = _viewBinding!!

    private lateinit var connectionLivedata: ConnectionLiveData
    private lateinit var viewModel: DetailViewModel

    val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().title = "Drink Recipe"

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }

        _viewBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drink = args.drink
        val repository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val viewModelFactory = DetailViewModelProviderFactory(repository)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        connectionLivedata = ConnectionLiveData(this.requireContext())
        connectionLivedata.observe(viewLifecycleOwner, { isAvailable ->
            if (isAvailable) {
                setupDetailSearch()
                viewModel.getDrinkDetail(drink.idDrink)
                Glide.with(this)
                    .load(drink.strDrinkThumb)
                    .into(viewBinding.ivDrinkDetail)

                viewBinding.floatingActionButton.setOnClickListener {
                    viewModel.saveCocktail(drink)
                    Snackbar.make(view, "Drink saved successfully", Snackbar.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(
                    this.requireContext(),
                    "No Internet Connection Available!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        viewBinding.tvTitleDetail.text = drink.strDrink
    }

    private fun setupDetailSearch() {
        viewModel.drinkLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { drinkResponse ->
                        bindItemsDetail(drinkResponse.drinks[0])
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun bindItemsDetail(drinkResponse: Drink) {

        viewBinding.apply {
            tvTitleDetail.text = drinkResponse.strDrink
            tvIngredients.text = checkIngredients(drinkResponse)
            tvMeasure.text = checkMeasure(drinkResponse)
            tvInstructions.text = drinkResponse.strInstructions
        }
        Glide.with(this)
            .load(drinkResponse.strDrinkThumb)
            .into(viewBinding.ivDrinkDetail)

    }


    private fun checkMeasure(drink: Drink): String {
        var drinksMeasure = ""

        drink.apply {

            if (!strMeasure1.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure1" else if (!strIngredient1.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure2.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure2" else if (!strIngredient2.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure3.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure3" else if (!strIngredient3.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure4.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure4" else if (!strIngredient4.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure5.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure5" else if (!strIngredient5.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure6.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure6" else if (!strIngredient6.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure7.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure7" else if (!strIngredient7.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure8.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure8" else if (!strIngredient8.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure9.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure9" else if (!strIngredient9.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strMeasure10.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure10" else if (!strIngredient10.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strIngredient11.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure11" else if (!strIngredient11.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            if (!strIngredient12.isNullOrBlank()) drinksMeasure += "\n $bullet $strMeasure12" else if (!strIngredient12.isNullOrBlank()) drinksMeasure += "\n $bullet  -"
            return drinksMeasure
        }

    }

    private fun checkIngredients(drink: Drink): String {
        var drinksIngredients = ""

        drink.apply {
            if (!strIngredient1.isNullOrBlank()) drinksIngredients += "\n $strIngredient1"
            if (!strIngredient2.isNullOrBlank()) drinksIngredients += "\n $strIngredient2"
            if (!strIngredient3.isNullOrBlank()) drinksIngredients += "\n $strIngredient3"
            if (!strIngredient4.isNullOrBlank()) drinksIngredients += "\n $strIngredient4"
            if (!strIngredient5.isNullOrBlank()) drinksIngredients += "\n $strIngredient5"
            if (!strIngredient6.isNullOrBlank()) drinksIngredients += "\n $strIngredient6"
            if (!strIngredient7.isNullOrBlank()) drinksIngredients += "\n $strIngredient7"
            if (!strIngredient8.isNullOrBlank()) drinksIngredients += "\n $strIngredient8"
            if (!strIngredient9.isNullOrBlank()) drinksIngredients += "\n $strIngredient9"
            if (!strIngredient10.isNullOrBlank()) drinksIngredients += "\n $strIngredient10"
            if (!strIngredient11.isNullOrBlank()) drinksIngredients += "\n $strIngredient11"
            if (!strIngredient12.isNullOrBlank()) drinksIngredients += "\n $strIngredient12"
            return drinksIngredients
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