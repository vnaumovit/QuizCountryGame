package com.example.quizcountrygame.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.quizcountrygame.configuration.Database
import com.example.quizcountrygame.dao.CountryDao
import com.example.quizcountrygame.databinding.QuestionFragmentBinding
import com.example.quizcountrygame.model.Country
import kotlin.random.Random

class QuestionFragment : Fragment() {

    private lateinit var fragmentBinding: QuestionFragmentBinding
    private var empty = 0
    private var correct = 0
    private var wrong = 0
    private var selectedButtonId: String? = null
    private var question = 0
    private lateinit var countryDao: CountryDao
    private var correctAnswer: String? = null
    private var lastChangeAct: String? = null
    private var isBackActive: Boolean = true
    private var numberCountries = 14

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = QuestionFragmentBinding.inflate(inflater, container, false)
        numberCountries = requireArguments().getInt("numberCountries")
        countryDao = CountryDao(Database(requireActivity()).writableDatabase)
        val randomCountries: List<Country> = countryDao.getRandomCountries(numberCountries)
        showData(randomCountries)
        handleButtons()
        handleNextButton(randomCountries)
        handleBackButton(randomCountries)
        return fragmentBinding.root
    }

    private fun handleButtons() {
        fragmentBinding.btnA.setOnClickListener {
            selectedButtonId = "btnA"
            val button = getButtonById(selectedButtonId!!)
            setBackgroundAndTextColor(button!!)
        }

        fragmentBinding.btnB.setOnClickListener {
            selectedButtonId = "btnB"
            val button = getButtonById(selectedButtonId!!)
            setBackgroundAndTextColor(button!!)
        }

        fragmentBinding.btnC.setOnClickListener {
            selectedButtonId = "btnC"
            val button = getButtonById(selectedButtonId!!)
            setBackgroundAndTextColor(button!!)
        }

        fragmentBinding.btnD.setOnClickListener {
            selectedButtonId = "btnD"
            val button = getButtonById(selectedButtonId!!)
            setBackgroundAndTextColor(button!!)
        }
    }

    private fun setBackgroundAndTextColor(button: Button) {
        resetBackgroundColor()
        button.setBackgroundColor(Color.RED)
        button.setTextColor(Color.WHITE)
    }

    private fun handleNextButton(randomCountries: List<Country>) {
        fragmentBinding.btnNext.setOnClickListener {
            checkCorrectAnswer()
            resetBackgroundColor()
            if (isBackActive) {
                isBackActive = false
                fragmentBinding.btnBack.isInvisible = false
            }
            question++
            if (question == numberCountries) {
                val direction = QuestionFragmentDirections.actionQuestionFragmentToResultFragment()
                direction.setEmpty(empty)
                direction.setWrong(wrong)
                direction.setCorrect(correct)
                this.findNavController().navigate(direction)
            } else {
                showData(randomCountries)
            }
        }
    }

    private fun handleBackButton(randomCountries: List<Country>) {
        fragmentBinding.btnBack.setOnClickListener {
            resetBackgroundColor()
            resetAnswer()
            question--
            showData(randomCountries)
            isBackActive = true
            fragmentBinding.btnBack.isInvisible = true
        }
    }

    private fun resetBackgroundColor() {
        fragmentBinding.btnA.setBackgroundColor(Color.WHITE)
        fragmentBinding.btnA.setTextColor(Color.BLACK)
        fragmentBinding.btnC.setBackgroundColor(Color.WHITE)
        fragmentBinding.btnC.setTextColor(Color.BLACK)
        fragmentBinding.btnB.setBackgroundColor(Color.WHITE)
        fragmentBinding.btnB.setTextColor(Color.BLACK)
        fragmentBinding.btnD.setBackgroundColor(Color.WHITE)
        fragmentBinding.btnD.setTextColor(Color.BLACK)
    }

    private fun resetAnswer() {
        when (lastChangeAct) {
            "correct" -> correct--
            "wrong" -> wrong--
            "empty" -> empty--
        }
    }

    private fun checkCorrectAnswer() {
        if (correctAnswer == selectedButtonId) {
            correct++
            lastChangeAct = "correct"
        } else if (selectedButtonId == null) {
            empty++
            lastChangeAct = "empty"
        } else {
            wrong++
            lastChangeAct = "wrong"
        }
        selectedButtonId = null
    }

    private fun getButtonById(id: String): Button? {
        return when (id) {
            "btnA" -> fragmentBinding.btnA
            "btnB" -> fragmentBinding.btnB
            "btnC" -> fragmentBinding.btnC
            "btnD" -> fragmentBinding.btnD
            else -> {
                return null
            }
        }
    }

    private fun showData(randomCountries: List<Country>) {
        val randomCountry = randomCountries.get(question)
        val randomCountriesNames: MutableList<String> =
            countryDao.getRandomCountriesNames(randomCountry.id.toString())
                .toMutableList()
        setImageCountry(randomCountry)
        setTexts()
        randomCountriesNames.add(Random.nextInt(0, 3), randomCountry.name)
        setTextButtonsAndCorrectAnswer(randomCountriesNames, randomCountry)
    }

    private fun setImageCountry(randomCountry: Country) {
        fragmentBinding.imageCountry
            .setImageResource(
                resources.getIdentifier(
                    randomCountry.image, "drawable",
                    requireActivity().packageName
                )
            )
    }

    private fun setTextButtonsAndCorrectAnswer(randomCountriesNames: MutableList<String>,
                                               randomCountry: Country) {
        val randomIndexes = listOf(0, 1, 2, 3).shuffled()
        val answer1 = randomCountriesNames[randomIndexes[0]]
        setCorrectAnswer(answer1, randomCountry, "btnA")
        fragmentBinding.btnA.text = answer1
        val answer2 = randomCountriesNames[randomIndexes[1]]
        setCorrectAnswer(answer2, randomCountry, "btnB")
        fragmentBinding.btnB.text = answer2
        val answer3 = randomCountriesNames[randomIndexes[2]]
        setCorrectAnswer(answer3, randomCountry, "btnC")
        fragmentBinding.btnC.text = answer3
        val answer4 = randomCountriesNames[randomIndexes[3]]
        setCorrectAnswer(answer4, randomCountry, "btnD")
        fragmentBinding.btnD.text = answer4
    }

    private fun setCorrectAnswer(answer: String, randomCountry: Country, btnId: String) {
        if (answer == randomCountry.name) {
            correctAnswer = btnId
        }
    }

    private fun setTexts() {
        fragmentBinding.correctCounter.text = correct.toString()
        fragmentBinding.wrongCounter.text = wrong.toString()
        fragmentBinding.emptyCounter.text = empty.toString()
        fragmentBinding.questionCounter.text = (question + 1).toString()
    }
}