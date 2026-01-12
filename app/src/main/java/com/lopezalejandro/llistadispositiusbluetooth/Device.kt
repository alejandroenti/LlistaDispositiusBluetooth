package com.lopezalejandro.llistadispositiusbluetooth

class Device {

    private var name : String
    private var macAddress : String

    constructor(name: String, macAddress: String) {
        this.name = name
        this.macAddress = macAddress
    }

    constructor() {
        this.name = getRandomName()
        this.macAddress = getRandomMacAddress()
    }

    fun getName() : String {
        return name
    }

    fun setName(name : String) {
        this.name = name
    }

    fun getMacAddress() : String {
        return macAddress
    }

    fun setMacAddress(macAddress : String) {
        this.macAddress = macAddress
    }

    private fun getRandomName() : String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..8)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun getRandomMacAddress() : String {
        val allowedChars = ('A'..'F') + ('0'..'9')
        return (1..12)
            .map { allowedChars.random() }
            .joinToString("")
    }
}