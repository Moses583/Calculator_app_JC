package com.example.calculatorappjc


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class CalculatorViewModel: ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onActions(actions: CalculatorActions){
        when(actions){
            is CalculatorActions.Number->enterNumber(actions.number)
            is CalculatorActions.Decimal->enterDecimal()
            is CalculatorActions.Clear-> state = CalculatorState()
            is CalculatorActions.Operation->enterOperation(actions.operation)
            is CalculatorActions.Calculate->performCalculation()
            is CalculatorActions.Delete->performDeletion()

        }

    }

    private fun performDeletion() {
        when{
           state.number2.isNotBlank() -> state = state.copy(
               number2 = state.number2.dropLast(1)
           )
            state.operation != null -> state = state.copy(
                operation = null
            )
            state.number.isNotBlank() -> state = state.copy(
                number = state.number.dropLast(1)
            )

        }
    }

    private fun performCalculation() {
        val number = state.number.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number != null && number2 != null){
            val result = when(state.operation){
                is CalculatorOperation.Add -> number + number2
                is CalculatorOperation.Substract -> number - number2
                is CalculatorOperation.Multiply -> number * number2
                is CalculatorOperation.Divide -> number / number2
                null -> return
            }
            state = state.copy(
                number = result.toString().take(15),
                number2 = "",
                operation = null
            )
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.number.isNotBlank() ){
          state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if (state.operation == null
            && !state.number.contains(".")
            && state.number.isNotBlank()){
            state = state.copy(
                number = state.number + "."
            )
            return
        }
        if (!state.number2.contains(".")
            && state.number2.isNotBlank()){
            state = state.copy(
                number = state.number2 + "."
            )
        }

    }

    private fun enterNumber(number: Int) {
        if (state.operation == null){
            if (state.number.length >= MAX_NUM_LENGTH){
                return
            }
            state = state.copy(
                number = state.number + number
            )
            return
        }
        if (state.number2.length >= MAX_NUM_LENGTH ){
            return
        }
        state = state.copy(
            number2 = state.number2 + number
        )

    }

    companion object{
        private const val MAX_NUM_LENGTH = 8
    }
}


