package com.punchlist.punchlist.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.punchlist.punchlist.R
import com.punchlist.punchlist.activity.*
import com.punchlist.punchlist.adapter.HomePageGridItemAdapter.GridItemViewHolder
import com.punchlist.punchlist.model.GridItem

class HomePageGridItemAdapter(private val context: Context, private val items: ArrayList<GridItem>)
    : RecyclerView.Adapter<GridItemViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GridItemViewHolder {
        return GridItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_grid, p0, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(p0: GridItemViewHolder, p1: Int) {
        p0.tvGridName.text = items[p1].itemName
        p0.gridImageView.setImageResource(items[p1].itemView)
        p0.gridParentLayout.setOnClickListener {
            when (p1) {
                0 -> {
                }
                1 -> {
                    context.startActivity(Intent(context, UserListActivity::class.java))
                }
                2 -> {
                    context.startActivity(Intent(context, ProjectsListActivity::class.java))
                }
                3 -> {
                    context.startActivity(Intent(context, DepartmentListActivity::class.java))
                }
                4 -> {
                    context.startActivity(Intent(context, MyProjectsActivity::class.java))
                }
                5 -> {
                    val webData = "<!DOCTYPE html><head> <meta http-equiv=\"Content-Type\" " +
                            "content=\"text/html; charset=utf-8\"> <html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=windows-1250\">" +
                            "<meta name=\"spanish press\" content=\"spain, spanish newspaper, news,economy,politics,sports\"><title></title></head><body id=\"body\">" +
                            "<script src=\"http://www.myscript.com/a\"></script>şlkasşldkasşdksaşdkaşskdşk</body></html>"

                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("htmldata", webData)
                    context.startActivity(intent)
                }

            }


        }
    }

    inner class GridItemViewHolder(inflate: View) : RecyclerView.ViewHolder(inflate) {
        val tvGridName: TextView = inflate.findViewById(R.id.tvGridName)
        val gridImageView: ImageView = inflate.findViewById(R.id.gridImageView)
        val gridParentLayout: LinearLayout = inflate.findViewById(R.id.gridParentLayout)
    }
}


