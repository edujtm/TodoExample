package me.edujtm.todoexample.ui.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.edujtm.todoexample.R
import me.edujtm.todoexample.domain.model.Todo
import me.edujtm.todoexample.domain.utils.startActivity
import me.edujtm.todoexample.ui.todoform.TodoFormActivity
import me.edujtm.todoexample.ui.model.TaskState
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodoListActivity : AppCompatActivity() {

    private val todoViewModel: TodoViewModel by viewModel()

    private lateinit var progressBarView: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var rvTodos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- Obtem as views ---

        val containerView = findViewById<ConstraintLayout>(R.id.todo_list_container)
        progressBarView = findViewById(R.id.todo_list_progress_bar)
        swipeRefreshLayout = findViewById(R.id.todo_list_swipe_refresh)

        // --- Configura a listagem ---
        rvTodos = findViewById(R.id.rvTodos)

        val todoAdapter = TodoListAdapter()
        todoAdapter.onItemClickListener = ::openTodoForm

        rvTodos.adapter = todoAdapter
        rvTodos.layoutManager = LinearLayoutManager(this)

        // --- monitora por mudanças de estado ---
        todoViewModel.todos
            .onEach { todoState ->
                when (todoState) {
                    is TaskState.Loading ->
                        swipeRefreshLayout.isRefreshing = true
                    is TaskState.Success -> {
                        swipeRefreshLayout.isRefreshing = false
                        todoAdapter.replaceTodos(todoState.data)
                    }
                    is TaskState.Failure -> {
                        swipeRefreshLayout.isRefreshing = false
                        Snackbar.make(
                            containerView,
                            "Erro ao carregar tarefas: ${todoState.error.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .launchIn(lifecycleScope)

        swipeRefreshLayout.setOnRefreshListener { todoViewModel.refreshTodos() }
        todoViewModel.getTodos()
    }


    private fun openTodoForm(todo: Todo) {
        // --- Abre a tela do form ---
        startActivity<TodoFormActivity> { intent ->
            intent.putExtra(TodoFormActivity.TODO_ITEM_ID, todo.id)
        }
    }
}