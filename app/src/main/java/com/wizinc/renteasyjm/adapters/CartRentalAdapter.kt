package com.wizinc.renteasyjm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.data.CartRental
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.databinding.CartRentalItemBinding
import com.wizinc.renteasyjm.helper.getRentalPrice

class CartRentalAdapter : RecyclerView.Adapter<CartRentalAdapter.CartRentalsHolder>() {

    inner class CartRentalsHolder(val binding: CartRentalItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cartRental: CartRental) {
            binding.apply {
                // Handle image loading
                if (cartRental.rental.imageUrls.isNotEmpty()) {
                    val imageUrl = cartRental.rental.imageUrls[0]
                    Log.d("SpecialRentalsAdapter", "Loading image: $imageUrl")

                    Glide.with(itemView)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)  // Shown while loading
                        .error(R.drawable.error_image)              // Shown on load failure
                        .into(imageCartProduct)
                } else {
                    Log.d("SpecialRentalsAdapter", "No image available for: ${cartRental.rental.name}")
                    Glide.with(itemView)
                        .load(R.drawable.placeholder_image)
                        .into(imageCartProduct)
                }

                // Set name and formatted price
                tvProductCartName.text = cartRental.rental.name
                tvProductCartPrice.text = "$${String.format("%.2f", cartRental.rental.price ?: 0f)}"


                val priceAfterPercentage = cartRental.rental.price?.let {
                    cartRental.rental.offerPercentage.getRentalPrice(
                        it
                    )
                }

                tvProductCartPrice.text = "$${String.format("%.2f", priceAfterPercentage)}"
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartRental>() {
        override fun areItemsTheSame(oldItem: CartRental, newItem: CartRental): Boolean {
            return oldItem.rental.id == newItem.rental.id
        }

        override fun areContentsTheSame(oldItem: CartRental, newItem: CartRental): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartRentalsHolder {
        return CartRentalsHolder(
            CartRentalItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CartRentalsHolder, position: Int) {
        val cartRental= differ.currentList[position]
        holder.bind(cartRental)

        holder.itemView.setOnClickListener {
            onRentalClick?.invoke(cartRental)
        }
        holder.binding.imagePlus.setOnClickListener() {
            onPlusClick?.invoke(cartRental)
        }
        holder.binding.imageMinus.setOnClickListener() {
            onMinusClick?.invoke(cartRental)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<CartRental>) {
        differ.submitList(list)
    }

    var onRentalClick: ((CartRental) -> Unit)? = null
    var onPlusClick: ((CartRental) -> Unit)? = null
    var onMinusClick: ((CartRental) -> Unit)? = null
}