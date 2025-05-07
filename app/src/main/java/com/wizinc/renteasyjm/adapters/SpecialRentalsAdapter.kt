package com.wizinc.renteasyjm.adapters
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.databinding.SpecialRvItemBinding

class SpecialRentalsAdapter : RecyclerView.Adapter<SpecialRentalsAdapter.SpecialRentalsHolder>() {

    inner class SpecialRentalsHolder(private val binding: SpecialRvItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(rental: Rental) {
            binding.apply {
                // Handle image loading
                if (rental.imageUrls.isNotEmpty()) {
                    val imageUrl = rental.imageUrls[0]
                    Log.d("SpecialRentalsAdapter", "Loading image: $imageUrl")

                    Glide.with(itemView)
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_image)  // Shown while loading
                        .error(R.drawable.error_image)              // Shown on load failure
                        .into(imageSpecialRvItem)
                } else {
                    Log.d("SpecialRentalsAdapter", "No image available for: ${rental.name}")
                    Glide.with(itemView)
                        .load(R.drawable.placeholder_image)
                        .into(imageSpecialRvItem)
                }

                // Set name and formatted price
                tvSpecialRentalName.text = rental.name
                tvSpecialRentalPrice.text = "$${String.format("%.2f", rental.price ?: 0f)}"
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Rental>() {
        override fun areItemsTheSame(oldItem: Rental, newItem: Rental): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rental, newItem: Rental): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialRentalsHolder {
        return SpecialRentalsHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialRentalsHolder, position: Int) {
        val rental = differ.currentList[position]
        holder.bind(rental)
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<Rental>) {
        differ.submitList(list)
    }
}