package com.lopezalejandro.llistadispositiusbluetooth

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recycler : RecyclerView
    private lateinit var btnAddDevice : Button

    private var devices : ArrayList<Device> = ArrayList<Device>()
    private var customAdapter = CustomAdapter(devices) { position -> showDetails(position) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recycler = findViewById<RecyclerView>(R.id.bluetoothDeviceList)
        btnAddDevice = findViewById<Button>(R.id.button)


        btnAddDevice.setOnClickListener {
            devices.add(Device())
            customAdapter.notifyItemInserted(devices.size - 1)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = customAdapter
    }

    private fun showDetails(position : Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(devices.get(position).getName())
            .setPositiveButton("Tancar") { dialog, which ->
                dialog.dismiss()
            }
            .setMessage("Mac Address: " + devices.get(position).getMacAddress())
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}