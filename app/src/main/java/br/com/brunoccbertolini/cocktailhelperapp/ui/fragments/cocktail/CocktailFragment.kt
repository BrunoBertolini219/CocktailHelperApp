package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.cocktail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.databinding.FragmentCocktailBinding
import br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic.AlcoholicCocktailFragment
import br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic.NonAlcoholicCocktailFragment
import com.google.android.material.tabs.TabLayoutMediator

class CocktailFragment : Fragment() {

    var _viewBinding: FragmentCocktailBinding? = null
    val viewBinding: FragmentCocktailBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentCocktailBinding.inflate(inflater, container, false)
        return viewBinding.root

    }

    val listTab = arrayListOf("Alcoholic", "Non-Alcoholic")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ViewPager2>(R.id.viewPager).adapter = FragmentTypeAdapter(this)

        TabLayoutMediator(
            view.findViewById(R.id.tabs),
            view.findViewById(R.id.viewPager)
        ) { tab, position ->
            tab.text = listTab[position]
        }.attach()
    }

    class FragmentTypeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            if (position == 0) {
                return AlcoholicCocktailFragment()
            } else {
                return NonAlcoholicCocktailFragment()
            }
        }
    }
}