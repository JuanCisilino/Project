package com.frost.project_wm.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frost.project_wm.Base64Helper
import com.frost.project_wm.R
import com.frost.project_wm.model.Product
import kotlinx.android.synthetic.main.item_product.view.*
import java.util.ArrayList

class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.MyViewHolder>() {

    private var prodList = ArrayList<Product>()
    var onProductClickCallback : ((product: Product) -> Unit)? = null
    private lateinit var helper: Base64Helper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        helper = Base64Helper(parent.context)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(prodList[position])
    }

    override fun getItemCount():Int = prodList.size

    inner class MyViewHolder(private val view: View): RecyclerView.ViewHolder(view){

        fun bind(product: Product) {
            view.nameLabel.text = product.title
            view.descriptionLabel.text = product.description
            view.availableLabel.text = "Stock: ${product.stock}"
            view.costLabel.text = "$ ${product.cost}"
            product.image?.let { if (it != "") view.iv_image.setImageBitmap(helper.decode(it)) }
            view.setOnClickListener { onProductClickCallback?.invoke(product) }
        }
    }

    fun setList(list: List<Product>){
        prodList = list as ArrayList<Product>
        this.notifyDataSetChanged()
    }
}