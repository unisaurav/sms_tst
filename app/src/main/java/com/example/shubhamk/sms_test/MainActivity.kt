package com.example.shubhamk.sms_test

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.shubhamk.sms_test.Constants.tid
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            val user = username.text
            val pass = password.text
            val url = "https://unribbed-headers.000webhostapp.com/shubham/user.php?username=" + user + "&password=" + pass
            val que = Volley.newRequestQueue(this@MainActivity)
            val req = StringRequest(Request.Method.GET,url,Response.Listener {
                response ->
                val a = response.toString()
                if(a != "Wrong Username or Password"){
                    val i = Intent(this,subject::class.java)
                    i.putExtra(tid,a)
                    startActivity(i)
                }
            },Response.ErrorListener {
                Toast.makeText(this,"T_T",Toast.LENGTH_SHORT).show()
            })
            que.add(req)
        }
    }
}
