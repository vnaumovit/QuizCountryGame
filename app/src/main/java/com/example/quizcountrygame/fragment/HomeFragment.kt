package com.example.quizcountrygame.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizcountrygame.configuration.Database
import com.example.quizcountrygame.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    lateinit var homeFragmentBinding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createDatabase()
        homeFragmentBinding = HomeFragmentBinding.inflate(inflater, container, false)
        val spinner = homeFragmentBinding.spinner
        val numberCountries = arrayOf(14, 24, 34)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, numberCountries
        )
        spinner.adapter = adapter
        homeFragmentBinding.btnStart.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToQuestionFragment()
            direction.setNumberCountries(homeFragmentBinding.spinner.selectedItem as Int)
            this.findNavController().navigate(direction)
        }

        return homeFragmentBinding.root
    }

    private fun createDatabase() {
        val database = Database(requireActivity())
        database.createDataBase()
        database.openDataBase()
    }

}