package com.example.numpuz

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private lateinit var mainView: ViewGroup
    private lateinit var startView:ViewGroup
    private  var board:Board?=null
    private  var boardView:BoardView?=null
    private lateinit var moves: TextView
    private lateinit var lineBorder: View
//    private lateinit var usernameEdit: TextInputEditText
    private lateinit var start: Button
    private var boardSize = 2

//    var id=0
//    lateinit var username:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainView = findViewById(R.id.mainView)
        startView = findViewById(R.id.startView)
        moves = findViewById(R.id.moves)
        lineBorder = findViewById(R.id.lineBorder)
//        usernameEdit = findViewById(R.id.username)
        start = findViewById(R.id.Start)
        moves.setTextColor(Color.RED)
        moves.textSize = 22f

        mainView.setVisibility(View.GONE)
        getSupportActionBar()?.hide();

        start.setOnClickListener(){
//            if (usernameEdit.equals(""))
//                username = "Misterius"
//            else
//                username = usernameEdit.toString()
            startView.setVisibility(View.GONE)
            mainView.setVisibility(View.VISIBLE)
            getSupportActionBar()?.show();
            newGame()
        }
    }

    fun reset() {
        startView.setVisibility(View.VISIBLE)
        mainView.setVisibility(View.GONE)
        getSupportActionBar()?.hide();
    }

    fun newGame() {
        board = Board(boardSize)
        board!!.addBoardChangeListener(boardChangeListener)
        board!!.rearrange()
        mainView!!.removeView(boardView)
        boardView = BoardView(this,board!!)
        mainView!!.addView(boardView)
        moves.text = "Jumlah pergerakan : 0"
        Toast.makeText(this,"Selamat bermain!", Toast.LENGTH_SHORT).show()
    }

    fun changeSize(newSize: Int) {
        if (newSize != boardSize) {
            boardSize = newSize
            boardView!!.invalidate()
        }
    }

    private val boardChangeListener :BoardChangeListener = object :BoardChangeListener{
        override fun tileSlid(from: Place?, to: Place?, numOfMoves: Int) {
            moves.text = "Jumlah pergerakan : ${numOfMoves}"
        }

        override fun solved(numOfMoves: Int) {

            moves.text = "Selesai dalam ${numOfMoves} langkah"

            AlertDialog.Builder(this@MainActivity)
                .setTitle("Anda menang..!!")
                .setIcon(R.drawable.ic_celebration)
                .setMessage("Anda menang dalam $numOfMoves langkah. \nMulai permainan baru?")
                .setPositiveButton("Ya"){
                        dialog,_->
                    board!!.rearrange()
                    moves.text = "Jumlah pergerakan : 0"
                    boardView!!.invalidate()
                    dialog.dismiss()
                }
                .setNegativeButton("Tidak"){
                        dialog, _->
                    dialog.dismiss()
                }
                .create()
                .show()

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val settings = SettingsDialogFragment(boardSize)
                settings.show(supportFragmentManager, "fragment_settings")
            }
            R.id.action_new_game ->
            {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Permainan Baru")
                    .setMessage("Apakah anda yakin untuk memulai permainan baru?")
                    .setPositiveButton(
                        "Ya"
                    ) { dialog, which ->
                        board!!.rearrange()
                        moves.text = "Jumlah pergerakan: 0"
                        boardView!!.invalidate()
                    }
                    .setNegativeButton(
                        "Kembali"
                    ) { dialog, which ->
                        // do nothing
                    }.setIcon(R.drawable.ic_new_game)
                    .show()
            }
            R.id.action_leaderboard -> {
                Toast.makeText(this,"to Leaderboard", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }
            R.id.action_help -> {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Petunjuk")
                    .setMessage(
                        "Tujuan dari permainan ini adalah untuk menempatkan ubin angka secara berurutan dengan cara menggeser ubin pada ruang yang kosong."
                    )
                    .setPositiveButton(
                        "Mengerti"
                    ) { dialog, which -> dialog.dismiss() }.show()
            }
            R.id.teammates -> {
                android.app.AlertDialog.Builder(this)
                    .setTitle("Anggota Tim")
                    .setMessage(
                        "11S19016 | Timothy Sipahutar\n11S19019 | Edrei Siregar\n11S19027 | Darel Pinem\n11S19037 | Rio Simanjuntak\n11S19040 | Judah Sitorus\n11S19044 | Kevin Sihaloho\n11S19047 | Andreas Pakpahan"
                    )
                    .setPositiveButton(
                        "Terima kasih"
                    ) { dialog, which -> dialog.dismiss() }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}