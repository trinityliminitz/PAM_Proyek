package com.example.numpuz

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
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
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val level = cursor.getInt(cursor.getColumnIndex("Level"))
                val langkah = cursor.getInt(cursor.getColumnIndex("Langkah"))

                listLeaderboard.add(Leaderboard(id, nama, level, langkah))
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
        intent.putExtra("MainActId", leaderboard.id)
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
        }
        return super.onOptionsItemSelected(item)
    }
}
