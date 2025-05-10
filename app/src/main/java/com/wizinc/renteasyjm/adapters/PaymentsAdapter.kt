package com.wizinc.renteasyjm.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wizinc.renteasyjm.R
import com.wizinc.renteasyjm.data.DownPayment
import com.wizinc.renteasyjm.data.DownPaymentStatus
import com.wizinc.renteasyjm.data.getDownPaymentStatus
import com.wizinc.renteasyjm.databinding.PaymentItemBinding


class PaymentsAdapter: RecyclerView.Adapter<PaymentsAdapter.PaymentsViewHolder>(){

    inner class PaymentsViewHolder(private val binding: PaymentItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(downPayment: DownPayment){
            binding.apply {
                tvOrderId.text = downPayment.paymentId.toString()
                tvOrderDate.text = downPayment.date

                val resources = itemView.resources

                val colorDrawable = when (getDownPaymentStatus(downPayment.downPaymentStatus)) {

                    is DownPaymentStatus.Pending -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }
                    is DownPaymentStatus.Paid -> {
                        ColorDrawable(resources.getColor(R.color.g_green))
                    }
                    is DownPaymentStatus.Refunded -> {
                        ColorDrawable(resources.getColor(R.color.g_red)) }

                    is DownPaymentStatus.Cancelled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }

                    is DownPaymentStatus.GracePeriod -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }

                    else -> ColorDrawable(resources.getColor(R.color.g_orange_yellow))

                }

                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<DownPayment>(){
        override fun areItemsTheSame(oldItem: DownPayment, newItem: DownPayment): Boolean {
            return oldItem.paymentId== newItem.paymentId
        }

        override fun areContentsTheSame(oldItem: DownPayment, newItem: DownPayment): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentsViewHolder {
        return PaymentsViewHolder(
            PaymentItemBinding.inflate(
                LayoutInflater.from(parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PaymentsViewHolder, position: Int){
        val payment = differ.currentList[position]
        holder.bind(payment)

        holder.itemView.setOnClickListener {
            onClick?.invoke(payment)
        }
    }

    override fun getItemCount(): Int{
        return differ.currentList.size
    }

    var onClick: ((DownPayment) -> Unit)? = null

}