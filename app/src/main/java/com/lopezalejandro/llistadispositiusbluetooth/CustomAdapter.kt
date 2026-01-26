package com.lopezalejandro.llistadispositiusbluetooth

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val dataSet: ArrayList<BluetoothDevice>, private val onItemClick: (BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val textName: TextView
        val textMacAddress: TextView

        init {
            textName = view.findViewById<TextView>(R.id.frameTextName)
            textMacAddress = view.findViewById<TextView>(R.id.frameTextMacAddress)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_layout, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textName.text = dataSet[position].name
        viewHolder.textMacAddress.text = dataSet[position].address

        viewHolder.itemView.setOnClickListener {
            onItemClick(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size
}
