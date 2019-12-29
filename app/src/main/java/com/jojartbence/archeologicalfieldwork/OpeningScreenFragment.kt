package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_opening_screen.*

/**
 * A simple [Fragment] subclass.
 */
class OpeningScreenFragment : Fragment() {

    lateinit var navController: NavController

    private val viewModel by lazy { ViewModelProviders.of(this)[OpeningScreenViewModel::class.java] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isTimerExpiredObserver = Observer<Boolean> {
            if (it){
                changeToLoginFragment()
            }
        }

        viewModel.isTimerExpired.observe(this, isTimerExpiredObserver)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.startTimer(2000)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opening_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }


    private fun changeToLoginFragment() {
        navController.navigate(R.id.action_openingScreenFragment_to_loginFragment)
    }

}
