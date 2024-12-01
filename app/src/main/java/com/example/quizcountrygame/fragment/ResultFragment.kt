package com.example.quizcountrygame.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.BLACK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quizcountrygame.MainActivity
import com.example.quizcountrygame.databinding.ResultFragmentBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

class ResultFragment : Fragment() {

    lateinit var resultFragmentBinding: ResultFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        resultFragmentBinding = ResultFragmentBinding.inflate(inflater, container, false)
        setChartBar()
        resultFragmentBinding.btnStart.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            requireActivity().startActivity(intent)
            requireActivity().finishAffinity()
        }
        resultFragmentBinding.btnExit.setOnClickListener {
            requireActivity().finishAffinity()
        }
        return resultFragmentBinding.root
    }

    private fun setChartBar() {
        val barChart = resultFragmentBinding.chartResult
        barChart.data = BarData(getBarChartData())
        barChart.data.setValueTextSize(30F)
        barChart.description.isEnabled = false
        barChart.legend.textSize = 30F
        barChart.legend.formSize = 30F

    }

    private fun getBarChartData(): MutableList<IBarDataSet> {
        val barEntriesList = mutableListOf<IBarDataSet>()
        val wrong = "wrong"
        val correct = "correct"
        val empty = "empty"
        val dataSetWrong =
            createBarDataSet(1f, requireArguments().getInt(wrong).toFloat(), wrong, Color.RED)
        val dataSetCorrect =
            createBarDataSet(2f, requireArguments().getInt(correct).toFloat(), correct, Color.GREEN)
        val datasetEmpty =
            createBarDataSet(3f, requireArguments().getInt(empty).toFloat(), empty, Color.GRAY)
        barEntriesList.add(dataSetCorrect)
        barEntriesList.add(dataSetWrong)
        barEntriesList.add(datasetEmpty)
        return barEntriesList

    }

    private fun createBarDataSet(x: Float, value: Float, name: String, colorRgb: Int): BarDataSet {
        return BarDataSet(
            listOf(
                BarEntry(
                    x,
                    value
                )
            ), name
        ).apply {
            color = colorRgb
            valueTextColor = BLACK
        }
    }
}