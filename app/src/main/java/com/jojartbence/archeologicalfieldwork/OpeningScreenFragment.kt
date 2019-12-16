package com.jojartbence.archeologicalfieldwork


import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_opening_screen.*

/**
 * A simple [Fragment] subclass.
 */
class OpeningScreenFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_opening_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val timer = object: CountDownTimer(2000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                fadeImage(0.05f)
            }

            override fun onFinish() {
                changeToLoginFragment()
            }
        }
        timer.start()
    }

    fun changeToLoginFragment() {
        navController.navigate(R.id.action_openingScreenFragment_to_loginFragment)
    }

    fun fadeImage(rate: Float) {
        imageView.alpha -= rate
    }
}
