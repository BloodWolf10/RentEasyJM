package com.wizinc.renteasyjm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wizinc.renteasyjm.data.Rental
import com.wizinc.renteasyjm.databinding.BestDealsRvItemBinding

class BestDealsAdapter: RecyclerView.Adapter<BestDealsAdapter.BestDealsViewHolder>(){

    inner class BestDealsViewHolder(private val binding: BestDealsRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rental: Rental){
            binding.apply {
                Glide.with(itemView).load(rental.imageUrls[0]).into(imgBestDeal)
                rental.offerPercentage?.let {
                    val remainingPricePercentage = 1f - it
                    val priceAfterOffer = remainingPricePercentage * rental.price!!
                    tvNewPrice.text = "${String.format("%.2f",priceAfterOffer)}"
                }
                tvOldPrice.text = "${rental.price}"
                tvDealProductName.text = rental.name
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsViewHolder {
        return BestDealsViewHolder(
            BestDealsRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestDealsViewHolder, position: Int){
        val rental = differ.currentList[position]
        holder.bind(rental)
    }

    override fun getItemCount(): Int{
        return differ.currentList.size
    }
}