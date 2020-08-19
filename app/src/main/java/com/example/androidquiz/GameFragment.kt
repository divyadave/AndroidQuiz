package com.example.androidquiz

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidquiz.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(val text: String, val answers: List<String>)

    private val questions: MutableList<Question> = mutableListOf(
        Question(
            text = "What is android jetpack",
            answers = listOf("tools", "documentation", "libraries", "all of these")
        ),
        Question(
            text = "Base class of layout?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")
        ),
        Question(
            text = "Layout for complex Screens",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")
        ),
        Question(
            text = "Pushing structured data into a Layout?",
            answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick")
        ),
        Question(
            text = "Inflate layout in fragments?",
            answers = listOf(
                "onCreateView",
                "onActivityCreated",
                "onCreateLayout",
                "onInflateLayout"
            )
        )
    )
    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private var numQuestion = Math.min((questions.size + 1), 3)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater, R.layout.fragment_game, container, false)
        randomizeQuestions()
        binding.game = this
        binding.submit.setOnClickListener {view : View ->
            val  checkedId = binding.firstGroup.checkedRadioButtonId
            if(-1 != checkedId) {
                var answerIndex = 0
                when(checkedId) {
                    R.id.secondAnswerButton -> answerIndex = 1
                    R.id.thirdAnswerButton -> answerIndex = 2
                    R.id.fourthAnswerButton -> answerIndex = 3
                }
                if(answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    if(questionIndex < numQuestion) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    }
                    else {
                        view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameWon(questionIndex, numQuestion))

                    }

                }
                else {
                    view.findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameOver())
                }


            }
        }
        val args = GameWonArgs.fromBundle(arguments!!)
        Toast.makeText(context, "Question attempted ${args.numCorrect}, TotalQuestions ${args.numQuestion}", Toast.LENGTH_LONG).show()
        return binding.root

}
    private fun randomizeQuestions() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }
    @SuppressLint("StringFormatInvalid")
    private fun setQuestion() {
        currentQuestion  = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name, questionIndex + 1, numQuestion)
    }
}