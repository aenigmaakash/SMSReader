package com.akash.smsreader

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import me.everything.providers.android.telephony.TelephonyProvider
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var smsArray = ArrayList<SMS>()
    private lateinit var smsListAdapter: SmsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewCharts.visibility = View.GONE
        smsList.visibility = View.GONE
        progressBar.visibility = View.GONE
        pieChartLayout.visibility = View.INVISIBLE
        analyseSMS.setOnClickListener{
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), 100)
            }
            else{
                getMessages().execute()
            }
        }

        viewCharts.setOnClickListener {
            mainLayout.visibility = View.INVISIBLE
            pieChartLayout.visibility = View.VISIBLE
        }

        smsList.setOnItemClickListener { parent, view, position, id ->
            val acc = view.findViewById<TextView>(R.id.accName).text
            val amount = view.findViewById<TextView>(R.id.amount).text
            try {
                val dialog = EditTextDialog.newInstance("Add Tags", "$acc, $amount")
                dialog.onOk = {
                    if(dialog.tags.text.toString()!=""){
                        smsArray[position].setTags(dialog.tags.text.toString())
                        smsListAdapter.notifyDataSetChanged()
                    }
                }
                dialog.onDelete = {
                    smsArray.remove(smsArray[position])
                    smsListAdapter.notifyDataSetChanged()
                }
                dialog.show(supportFragmentManager, "MainActivity")
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }

    override fun onBackPressed() {
        if (pieChartLayout.visibility == View.VISIBLE){
            pieChartLayout.visibility = View.INVISIBLE
            mainLayout.visibility = View.VISIBLE
        }
        else
            super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getMessages().execute()
            }
            else
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show()
        }
    }



    inner class getMessages(): AsyncTask<Void, Void, SmsListAdapter?>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void?): SmsListAdapter? {
            val provider = TelephonyProvider(this@MainActivity)
            val smsList = provider.getSms(TelephonyProvider.Filter.INBOX).list
            for (sms in smsList) {
                if (sms.body.contains("debit", true)){
                    val smsObj = SMS(sms, true)
                    smsObj.analyseMsg()
                    smsArray.add(smsObj)
                }
                else if (sms.body.contains("credit", true)) {
                    val smsObj = SMS(sms, false)
                    smsObj.analyseMsg()
                    smsArray.add(smsObj)
                }
            }
            smsListAdapter =  SmsListAdapter(this@MainActivity, R.layout.sms_list_view, smsArray)
            return smsListAdapter
        }

        override fun onPostExecute(result: SmsListAdapter?) {
            super.onPostExecute(result)
            smsList.adapter = smsListAdapter
            progressBar.visibility = View.GONE
            smsList.visibility = View.VISIBLE
            makePieChart(smsListAdapter.totalDebit, smsListAdapter.totalCredit)
            viewCharts.visibility = View.VISIBLE
        }
    }

    private fun makePieChart(debit: Double, income: Double){
        val pieData = ArrayList<SliceValue>()
        pieData.add(SliceValue(debit.toFloat(), Color.RED).setLabel("Debit: $debit"))
        pieData.add(SliceValue(income.toFloat(), Color.BLUE).setLabel("Income: $income"))
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasLabels(true).valueLabelTextSize = 15
        pieChart.pieChartData = pieChartData
    }
}