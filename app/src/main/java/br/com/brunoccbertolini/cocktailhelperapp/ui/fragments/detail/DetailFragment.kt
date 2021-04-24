package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentDetailBinding
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.model.Drink
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class DetailFragment : Fragment() {

    private val TAG = "DetailFragment"
    private var _viewBinding: FragmentDetailBinding? = null
    private val viewBinding: FragmentDetailBinding get() = _viewBinding!!

    private lateinit var viewModel: DetailViewModel

    val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drink = args.drink
        val repository = CocktailRepository(CocktailDatabase(this.requireContext()))
        val viewModelFactory = DetailViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        viewBinding.tvTitleDetail.text = drink.strDrink
        viewModel.getDrinkDetail(drink.idDrink)
        Glide.with(this)
            .load(drink.strDrinkThumb)
            .into(viewBinding.ivDrinkDetail)
        setupDetailSearch()

        viewBinding.floatingActionButton.setOnClickListener {
            viewModel.saveCocktail(drink)
            Snackbar.make(view, "Drink saved successfully", Snackbar.LENGTH_SHORT).show()
        }

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


    fun bindItemsDetail(drinkResponse: Drink) {

        viewBinding.apply {
            tvTitleDetail.text = drinkResponse.strDrink
            tvIngredients.text = checkIngredients(drinkResponse)
            tvInstructions.text = drinkResponse.strInstructions
        }
        Glide.with(this)
            .load(drinkResponse.strDrinkThumb)
            .into(viewBinding.ivDrinkDetail)

    }

    fun checkIngredients(drink: Drink): String{
        var drinksIngredients =""

        drink.apply {
            if (!strIngredient1.isNullOrBlank()) drinksIngredients += "\n $strMeasure1  $strIngredient1"
            if (!strIngredient2.isNullOrBlank()) drinksIngredients += "\n $strMeasure2  $strIngredient2"
            if (!strIngredient3.isNullOrBlank()) drinksIngredients += "\n $strMeasure3  $strIngredient3"
            if (!strIngredient4.isNullOrBlank()) drinksIngredients += "\n $strMeasure4  $strIngredient4"
            if (!strIngredient5.isNullOrBlank()) drinksIngredients += "\n $strMeasure5 $strIngredient5"
            if (!strIngredient6.isNullOrBlank()) drinksIngredients += "\n $strMeasure6 $strIngredient6"
            if (!strIngredient7.isNullOrBlank()) drinksIngredients += "\n $strMeasure7 $strIngredient7"
            if (!strIngredient8.isNullOrBlank()) drinksIngredients += "\n $strMeasure8 $strIngredient8"
            if (!strIngredient9.isNullOrBlank()) drinksIngredients += "\n $strMeasure9 $strIngredient9"
            if (!strIngredient10.isNullOrBlank()) drinksIngredients += "\n $strMeasure10 $strIngredient10"
            if (!strIngredient11.isNullOrBlank()) drinksIngredients += "\n $strMeasure11 $strIngredient11"
            if (!strIngredient12.isNullOrBlank()) drinksIngredients += "\n $strMeasure12 $strIngredient12"
            return drinksIngredients
        }
    }



    private fun hideProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        viewBinding.paginationProgressBar.visibility = View.VISIBLE
    }
}