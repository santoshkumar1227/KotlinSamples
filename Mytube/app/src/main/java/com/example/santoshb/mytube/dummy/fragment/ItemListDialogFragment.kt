package com.example.santoshb.mytube.dummy.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.example.santoshb.mytube.R
import com.example.santoshb.mytube.dummy.model.ItemWithCheck
import kotlinx.android.synthetic.main.fragment_item_list_dialog.*
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.view.*

// TODO: Customize parameter argument names
const val ARG = "items"

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ItemListDialogFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 *
 * You activity (or fragment) needs to implement [ItemListDialogFragment.Listener].
 */
class ItemListDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null
    private var listForCheck = ArrayList<ItemWithCheck>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.layoutManager = LinearLayoutManager(context)
        listForCheck = arguments?.getSerializable(ARG) as ArrayList<ItemWithCheck>
        btnApply.setOnClickListener {
            mListener?.let {
                it.onItemClicked(listForCheck)
                dismiss()
            }
        }

        list.adapter = ItemAdapter(listForCheck)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            mListener = parent as Listener
        } else {
            mListener = context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onItemClicked(list: ArrayList<ItemWithCheck>)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false)) {

        internal val text: TextView = itemView.text
        internal val checkBox: CheckBox = itemView.checkBox

        /*init {
            text.setOnClickListener {
                mListener?.let {
                    it.onItemClicked(adapterPosition)
                    dismiss()
                }
            }
        }*/
    }

    private inner class ItemAdapter internal constructor(private val listForCheckBoxes: ArrayList<ItemWithCheck>) :
            RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = listForCheckBoxes[position].title
            holder.checkBox.isChecked = listForCheckBoxes[position].isChecked
            holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                listForCheckBoxes[position].isChecked = isChecked
            }
        }

        override fun getItemCount(): Int {
            return listForCheckBoxes.size
        }
    }

    companion object {

        // TODO: Customize parameters
        fun newInstance(listForCheckBoxes: ArrayList<ItemWithCheck>): ItemListDialogFragment =
                ItemListDialogFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG, listForCheckBoxes)
                    }
                }
    }
}
