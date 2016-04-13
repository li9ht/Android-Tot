package my.ukm.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.ukm.todolist.adapter.TodoListViewAdapter;
import my.ukm.todolist.model.Todo;
import my.ukm.todolist.net.TodoApi;
import my.ukm.todolist.ui.TodoFormActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.listView) ListView listView;
    public TodoListViewAdapter todoListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent i = new Intent(getApplicationContext(), TodoFormActivity.class);
                i.putExtra("mode","new");
                startActivity(i);
            }
        });

        todoListViewAdapter = new TodoListViewAdapter(this,0);
        listView.setAdapter(todoListViewAdapter);
        //loadStaticData();
        loadDataFromApi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromApi();
    }

    public void loadDataFromApi(){

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading Data")
                .content("Please Wait")
                .progress(true,0)
                .build();
        dialog.show();

        TodoApi todoApi = TodoApi.Creator.newTodoApiService(this);
        Call<List<Todo>> todoList = todoApi.getList("K018531");
        todoList.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                List<Todo> todoList = response.body();
                todoListViewAdapter.clear();
                todoListViewAdapter.addAll(todoList);
                todoListViewAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {

                dialog.dismiss();
            }
        });
    }

    public void loadStaticData(){
        Todo todo1 = new Todo();
        todo1.setTitle("Test Title");
        todo1.setNote("test note");
        todoListViewAdapter.add(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Test Title2");
        todo2.setNote("test note2");
        todoListViewAdapter.add(todo2);

        todoListViewAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
