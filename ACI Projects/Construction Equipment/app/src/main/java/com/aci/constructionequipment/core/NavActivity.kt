package com.aci.constructionequipment.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aci.constructionequipment.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class NavActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        toolbar.setupWithNavController(navController, appBarConfiguration)




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            if (!hasNetworkAvailable())
                showsnackbar()

            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager?.let {


                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        //take action when network connection is gained
                        snackbar?.let {
                            if (it.isShown)
                                it.dismiss()
                        }

                    }

                    override fun onLost(network: Network?) {
                        //take action when network connection is lost
                        showsnackbar()
                    }
                })
            }
        }
    }


    fun showsnackbar() {
        if (snackbar == null) {
            snackbar = Snackbar.make(rootViewNav, "ইন্টারনেট সংযোগ নেই", Snackbar.LENGTH_INDEFINITE)
                .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                .setBackgroundTint(
                    ContextCompat.getColor(
                        this,
                        R.color.colorPrimary
                    )
                )

        }

        if (!snackbar!!.isShown) {
            snackbar!!.show()
        }
    }

    fun hasNetworkAvailable(): Boolean {
        val service = Context.CONNECTIVITY_SERVICE
        val manager = getSystemService(service) as ConnectivityManager?
        val network = manager?.activeNetworkInfo
        return (network != null)
    }
}
