package com.example.numpuz

class Leaderboard {
    var namaPemain: String? = null
    var levelPermainan: String? = null
    var langkahPemain: String? = null

    constructor(nama: String, level: String, langkah:String){
        this.namaPemain = nama
        this.levelPermainan = level
        this.langkahPemain = langkah
    }
}
