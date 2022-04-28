package com.example.numpuz

class Leaderboard {
    var id: Int? = null
    var namaPemain: String? = null
    var levelPermainan: Int? = null
    var langkahPemain: Int? = null

    constructor(id: Int, nama: String, level: Int, langkah:Int){
        this.id = id
        this.namaPemain = nama
        this.levelPermainan = level
        this.langkahPemain = langkah
    }
}
