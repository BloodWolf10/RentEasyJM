package com.wizinc.renteasyjm.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.databinding.RentalRvItemBinding
import com.wizinc.renteasyjm.helper.getRentalPrice

class BestRentalsAdapter: RecyclerView.Adapter<BestRentalsAdapter.BestRentalsViewHolder>() {
    inner class BestRentalsViewHolder(private val binding: RentalRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rental: Rental){
            binding.apply {
                Glide.with(itemView).load(rental.imageUrls[0]).into(imgRental)

                    val priceAfterOffer = rental.price?.let {
                        rental.offerPercentage.getRentalPrice(
                            it
                        )
                    }
                    tvNewPrice.text = "${String.format("%.2f",priceAfterOffer)}"
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                if(rental.offerPercentage == null)
                    tvPrice.visibility = View.INVISIBLE
                tvPrice.text = "${rental.price}"
                tvName.text = rental.name
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Rental>(){
        override fun areItemsTheSame(oldItem: Rental, newItem: Rental): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Rental, newItem: Rental): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestRentalsViewHolder {
        return BestRentalsViewHolder(
            RentalRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestRentalsViewHolder, position: Int){
        val rental = differ.currentList[position]
        holder.bind(rental)

        holder.itemView.setOnClickListener {
            onClick?.invoke(rental)
        }
    }

    override fun getItemCount(): Int{
        return differ.currentList.size
    }

    var onClick: ((Rental) -> Unit)? = null
}