package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.random

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentDetailBinding
import br.com.brunoccbertolini.cocktailhelperapp.model.Drink
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail.DetailViewModel
import br.com.brunoccbertolini.cocktailhelperapp.util.ConnectionLiveData
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomDrinkFragment() : Fragment() {

    private var _viewBinding: FragmentDetailBinding? = null
    private val viewBinding: FragmentDetailBinding get() = _viewBinding!!
    private lateinit var drinkPreview: DrinkPreview
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var connection: ConnectionLiveData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().title = "Drink Suggestion"

        (activity as AppCompatActivity)
            .supportActionBar?.setDisplayHomeAsUpEnabled(false)

        _viewBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        drinkPreview = DrinkPreview("", "", "")



        connection = ConnectionLiveData(this.requireContext())
        connection.observe(viewLifecycleOwner, { isAvailable ->
            if (isAvailable) {
                viewModel.getRandomDrink()

                viewModel.drinkLiveData.observe(viewLifecycleOwner, { response ->
                    response.peekContent().data?.let { drink ->
                        bindItemsDetail(drink.drinks[0])
                        drink.drinks[0].apply {
                            drinkPreview = DrinkPreview(
                                idDrink.toString(),
                                strDrink.toString(),
                                strDrinkThumb
                            )
                        }
                    }
                })

                viewBinding.floatingActionButton.setOnClickListener {
                    if (drinkPreview.strDrink.isNotBlank()) {
                        viewModel.saveCocktail(drink = drinkPreview)
                        Snackbar.make(view, "Drink saved successfully", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "No Internet Connection Available!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId = R.menu.detail_menu
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        connection.observe(viewLifecycleOwner, { isAvailable ->
            if (isAvailable) {
                when (item.itemId) {
                    R.id.refresh_item -> viewModel.getRandomDrink()
                    else -> super.onOptionsItemSelected(item)
                }
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "No Internet Connection Available!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return true
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

            if (!strMeasure1.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure1" else if (!strIngredient1.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure2.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure2" else if (!strIngredient2.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure3.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure3" else if (!strIngredient3.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure4.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure4" else if (!strIngredient4.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure5.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure5" else if (!strIngredient5.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure6.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure6" else if (!strIngredient6.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure7.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure7" else if (!strIngredient7.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure8.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure8" else if (!strIngredient8.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure9.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure9" else if (!strIngredient9.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strMeasure10.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure10" else if (!strIngredient10.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strIngredient11.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure11" else if (!strIngredient11.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
            if (!strIngredient12.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet} $strMeasure12" else if (!strIngredient12.isNullOrBlank()) drinksMeasure += "\n ${Typography.bullet}  -"
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

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

}