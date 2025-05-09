package com.wizinc.renteasyjm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.data.CartRental
import com.wizinc.renteasyjm.databinding.BillingRentalRvItemBinding


class BillingRentalsAdapter : RecyclerView.Adapter<BillingRentalsAdapter.BillingRentalsViewHolder>(){

    inner class BillingRentalsViewHolder(val binding: BillingRentalRvItemBinding): ViewHolder(binding.root){

        fun bind(billingRental: CartRental){
            binding.apply {

                // Handle image loading
                if (billingRental.rental.imageUrls.isNotEmpty()) {
                    val imageUrl = billingRental.rental.imageUrls[0]
                    Log.d("BillingRentalsAdapter", "Loading image: $imageUrl")

                    Glide.with(itemView)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)  // Shown while loading
                        .error(R.drawable.error_image)              // Shown on load failure
                        .into(imageCartRental)
                } else {
                    Log.d("BillingRentalsAdapter", "No image available for: ${billingRental.rental.name}")
                    Glide.with(itemView)
                        .load(R.drawable.placeholder_image)
                        .into(imageCartRental)
                }

                // Set name and formatted price
                tvRentalCartName.text = billingRental.rental.name
                tvRentalCartPrice. text = "$${String.format("%.2f", billingRental.rental.price ?: 0f)}"

            }
        }

    }


    private val diffUtil = object : DiffUtil.ItemCallback<CartRental>(){
        override fun areItemsTheSame(oldItem: CartRental, newItem: CartRental): Boolean {
            return oldItem.rental.id == newItem.rental.id
        }

        override fun areContentsTheSame(oldItem: CartRental, newItem: CartRental): Boolean {
           return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingRentalsViewHolder {
        return BillingRentalsViewHolder(
            BillingRentalRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: BillingRentalsViewHolder, position: Int) {
        val billingRental = differ.currentList[position]
        holder.bind(billingRental)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }
















}
