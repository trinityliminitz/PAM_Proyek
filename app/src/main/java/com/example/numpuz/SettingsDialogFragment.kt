package com.example.numpuz

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SettingsDialogFragment(var size: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Set the dialog title
        builder.setTitle("Tentukan level permainan")
            .setSingleChoiceItems(
                R.array.size_options, size - 2
            ) { dialog, which -> size = which + 2 }
            .setPositiveButton(
                "Ubah"
            ) { dialog, id ->
                (getActivity() as MainActivity)
                    .changeSize(size)
                (getActivity() as MainActivity)
                    .reset()
            }
            .setNegativeButton(
                "Kembali"
            ) { dialog, id -> dialog.cancel() }
        val settingsDialog =  builder.create()
        settingsDialog.show()
        return  settingsDialog
    }
}