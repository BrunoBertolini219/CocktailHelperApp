package br.com.brunoccbertolini.cocktailhelperapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import br.com.brunoccbertolini.cocktailhelperapp.databinding.SavedItemBinding

import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import com.bumptech.glide.Glide

class SavedCocktailAdapter : RecyclerView.Adapter<SavedCocktailAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<DrinkPreview>() {
        override fun areItemsTheSame(oldItem: DrinkPreview, newItem: DrinkPreview): Boolean {
            return oldItem.idDrink == newItem.idDrink
        }

        override fun areContentsTheSame(oldItem: DrinkPreview, newItem: DrinkPreview): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SavedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int = differ.currentList.size

    private var onItemClickListener: ((DrinkPreview) -> Unit)? = null
    private var onItemDeleteClickListener: ((DrinkPreview) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drink = differ.currentList[position]
        holder.bindView(drink)
    }

    inner class ViewHolder(view: SavedItemBinding) : RecyclerView.ViewHolder(view.root) {
        private val drinkImage: ImageView = view.ivDrinkImg
        private val drinkTittle = view.tvDrinkTitle
        private val drinkDelete = view.ivDrinkDelete
        // private val drinkStyle = view.tvDrinkStyle

        fun bindView(drink: DrinkPreview) {
            drink.apply {
                drinkTittle.text = strDrink
                Glide.with(itemView.context)
                    .load(strDrinkThumb)
                    .centerCrop()
                    .into(drinkImage)

                drinkDelete.setOnClickListener {
                    onItemDeleteClickListener?.let { it(drink) }
                }

                itemView.setOnClickListener {
                    onItemClickListener?.let { it(drink) }
                }

            }
        }
    }


    fun setOnItemClickListener(listener: (DrinkPreview) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnDeleteItemClickListener(listener: (DrinkPreview) -> Unit) {
        onItemDeleteClickListener = listener
    }

}

