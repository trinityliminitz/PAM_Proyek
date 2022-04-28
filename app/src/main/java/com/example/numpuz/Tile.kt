package com.example.numpuz

class Tile(private var number:Int? = null) {
    fun number():Int{
        return number!!
    }
}