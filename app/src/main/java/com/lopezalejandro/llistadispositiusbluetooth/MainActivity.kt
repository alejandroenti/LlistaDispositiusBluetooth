package com.lopezalejandro.llistadispositiusbluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_BLUETOOTH = 100 // es pot posar un nombre aleatori no emprat en cap altre lloc

    private lateinit var recycler : RecyclerView
    private lateinit var btnRefreshDevices : Button

    private var devices : ArrayList<Device> = ArrayList<Device>()
    private var customAdapter = CustomAdapter(devices) { position -> showDetails(position) }
    private var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

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
        btnRefreshDevices = findViewById<Button>(R.id.button)


        btnRefreshDevices.setOnClickListener {
            requestBluetoothPermissionAndUpdate()
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

    private fun updatePairedDevices() {
        customAdapter.notifyItemRangeRemoved(0, devices.size)
        devices.clear()

        for (elem in bluetoothAdapter.bondedDevices) {
            // afegim element al dataset
            devices.add( Device(elem.name, elem.address))
            customAdapter.notifyItemInserted(devices.size - 1)
        }
    }

    private fun requestBluetoothPermissionAndUpdate() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ requereix BLUETOOTH_CONNECT
            Manifest.permission.BLUETOOTH_CONNECT
        } else {
            // Versions anteriors
            Manifest.permission.BLUETOOTH
        }

        if (ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED) {

            // Demanar el permís
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_CODE_BLUETOOTH
            )
        } else {
            // Permís ja concedit - llegir dispositius
            updatePairedDevices()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_BLUETOOTH) {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                // Permís concedit - llegir dispositius
                updatePairedDevices()
            } else {
                // Permís denegat
                Toast.makeText(this, "Permís necessari per a llegir dispositius Bluetooth", Toast.LENGTH_SHORT).show()
            }
        }
    }
}