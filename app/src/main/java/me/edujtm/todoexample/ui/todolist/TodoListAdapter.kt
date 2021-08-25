package me.edujtm.todoexample.ui.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.edujtm.todoexample.R
import me.edujtm.todoexample.domain.model.Todo

typealias OnItemClick<T> = (item: T) -> Unit

class TodoListAdapter: RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private val _todos: ArrayList<Todo> = arrayListOf();
    val todos: List<Todo> = _todos

    var onItemClickListener: OnItemClick<Todo>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val todoView = inflater.inflate(R.layout.todo_list_item, parent, false)
        return ViewHolder(todoView, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = _todos[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int = _todos.size

    fun replaceTodos(newTodos: List<Todo>) {
        _todos.clear()
        _todos.addAll(newTodos)
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        itemView: View,
        val onItemClick: OnItemClick<Todo>? = null
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val todoNameView = itemView.findViewById<TextView>(R.id.todo_name)
        private val todoDescriptionView = itemView.findViewById<TextView>(R.id.todo_description)

        var todoItem: Todo? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            todoItem?.let { todo ->
                onItemClick?.invoke(todo)
            }
        }

        fun bind(item: Todo) {
            todoItem = item

            todoNameView.text = item.name
            todoDescriptionView.text = item.description
        }
    }
}