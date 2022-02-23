package com.frost.project_wm.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.frost.project_wm.R
import com.frost.project_wm.model.Product
import com.frost.project_wm.model.User
import kotlinx.android.synthetic.main.item_product.view.*
import java.util.ArrayList

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    private var userList = ArrayList<User>()
    var onUserClickCallback : ((user: User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount():Int = userList.size

    inner class MyViewHolder(private val view: View): RecyclerView.ViewHolder(view){

        fun bind(user: User) {
            view.nameLabel.text = user.nombre
            view.descriptionLabel.text = user.email
            view.availableLabel.text = user.rol
            view.costLabel.text = user.empresa
            view.setOnClickListener { onUserClickCallback?.invoke(user) }
        }

        private fun glideImage(imageView: ImageView, url: String){
            Glide.with(imageView)
                .load(url)
                .circleCrop()
                .into(imageView)
        }
    }

    fun setList(list: List<User>){
        userList = list as ArrayList<User>
        this.notifyDataSetChanged()
    }
}