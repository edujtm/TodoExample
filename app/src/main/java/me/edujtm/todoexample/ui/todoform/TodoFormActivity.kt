package me.edujtm.todoexample.ui.todoform

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.edujtm.todoexample.R
import me.edujtm.todoexample.domain.model.Todo
import me.edujtm.todoexample.ui.model.TaskState
import me.edujtm.todoexample.domain.utils.Result
import me.edujtm.todoexample.domain.utils.startActivity
import me.edujtm.todoexample.ui.todolist.TodoListActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodoFormActivity : AppCompatActivity() {

    private val todoFormViewModel: TodoFormViewModel by viewModel()

    private lateinit var clTodoForm : ConstraintLayout
    private lateinit var todoNameTextInput: TextInputLayout
    private lateinit var todoDescriptionTextInput: TextInputLayout
    private lateinit var todoStatusToggle : SwitchCompat
    private lateinit var saveButton: Button

    private var todoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_form)

        clTodoForm = findViewById(R.id.cl_todo_form)
        todoNameTextInput = findViewById(R.id.til_todo_name)
        todoDescriptionTextInput = findViewById(R.id.til_todo_description)
        todoStatusToggle = findViewById(R.id.switch_todo_status)

        saveButton = findViewById(R.id.btn_save)
        saveButton.setOnClickListener { submitForm() }

        todoId = intent.extras!![TODO_ITEM_ID] as Int

        prefillForm()
        todoFormViewModel.getTodoById(todoId)
    }

    private fun prefillForm() {
        todoFormViewModel.todoState
            .onEach { state ->
                when (state) {
                    is TaskState.Loading -> {}
                    is TaskState.Success -> fillForm(state.data)
                    is TaskState.Failure ->
                        Snackbar.make(
                            clTodoForm,
                            "Erro ao obter todo: ${state.error.message}",
                            Snackbar.LENGTH_LONG
                        ).show()
                }

            }.launchIn(lifecycleScope)

    }

    private fun submitForm() {
        val name = todoNameTextInput.editText?.text.toString()
        val description = todoDescriptionTextInput.editText?.text.toString()
        val status = todoStatusToggle.isChecked

        val updatedTodo = Todo(todoId, name, description, status)

        lifecycleScope.launch {
            val result = todoFormViewModel.updateTodo(updatedTodo)
            when (result) {
                is Result.Success -> startActivity<TodoListActivity>()
                is Result.Failure -> {
                    Snackbar.make(
                        clTodoForm,
                        "Erro ao salvar tarefa: ${result.error.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun fillForm(todo: Todo) {
        todoNameTextInput.editText?.setText(todo.name)

        todoDescriptionTextInput.editText?.setText(todo.description)

        todoStatusToggle.isChecked = todo.status
    }

    companion object {
        const val TODO_ITEM_ID = "TODO_ITEM_ID"
    }
}