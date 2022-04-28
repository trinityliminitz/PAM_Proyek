package com.example.numpuz

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LeaderboardActivity : AppCompatActivity() {
    private var listLeaderboard = ArrayList<Leaderboard>()
    private lateinit var lvLeaderboard : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        lvLeaderboard = findViewById(R.id.lvLeaderboard)

        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.leaderboard_menu, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    @SuppressLint("Range")
    private fun loadData() {
        var dbAdapter = DBAdapter(this)
        var cursor = dbAdapter.allQuery()

        listLeaderboard.clear()
        if (cursor.moveToFirst()){
            do {
                val nama = cursor.getString(cursor.getColumnIndex("NamaPemain"))
                val level = "Level permainan : " + cursor.getInt(cursor.getColumnIndex("LevelPermainan"))
                val langkah = "Jumlah langkah : " + cursor.getInt(cursor.getColumnIndex("LangkahPemain"))
                listLeaderboard.add(Leaderboard(nama, level, langkah))
            }while (cursor.moveToNext())
        }

        var barangAdapter = BarangAdapter(this, listLeaderboard)
        lvLeaderboard.adapter = barangAdapter
    }

    inner class BarangAdapter: BaseAdapter{

        private var leaderboardList = ArrayList<Leaderboard>()
        private var context: Context? = null

        constructor(context: Context, leaderboardList: ArrayList<Leaderboard>) : super(){
            this.leaderboardList = leaderboardList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            val view: View?
            val vh: ViewHolder

            if (convertView == null){
                view = layoutInflater.inflate(R.layout.leaderboard, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("db", "set tag for ViewHolder, position: " + position)
            }else{
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mLeaderboard = leaderboardList[position]

            vh.tvNama.text = mLeaderboard.namaPemain
            vh.tvLevel.text = mLeaderboard.levelPermainan.toString()
            vh.tvLangkah.text = mLeaderboard.langkahPemain.toString()

            return view
        }

        override fun getItem(position: Int): Any {
            return leaderboardList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return leaderboardList.size
        }

    }

    private fun updateLeaderboard(leaderboard: Leaderboard) {
        var  intent = Intent(this, LeaderboardActivity::class.java)
        intent.putExtra("MainActNama", leaderboard.namaPemain)
        intent.putExtra("MainActLevel", leaderboard.levelPermainan)
        intent.putExtra("MainActLangkah", leaderboard.langkahPemain)
        startActivity(intent)
    }

    private class ViewHolder(view: View?){
        val tvNama: TextView
        val tvLevel: TextView
        val tvLangkah: TextView

        init {
            this.tvNama = view?.findViewById(R.id.tvNamaPemain) as TextView
            this.tvLevel = view?.findViewById(R.id.tvLevelPermainan) as TextView
            this.tvLangkah = view?.findViewById(R.id.tvLangkahPemain) as TextView
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_return -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.action_delete ->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Hapus rekaman")
                builder.setMessage("Apakah anda yakin untuk menghapus semua rekaman?")

                builder.setPositiveButton("Ya") { dialog: DialogInterface?, which: Int ->
                    var dbAdapter = DBAdapter(this)
                    dbAdapter.delete()
                    Toast.makeText(this, "Berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
                builder.setNegativeButton("Kembali"){ dialog: DialogInterface?, which: Int ->  }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
