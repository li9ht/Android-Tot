package my.ukm.todolist.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.ukm.todolist.R;
import my.ukm.todolist.model.Todo;
import my.ukm.todolist.net.TodoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoDetailActivity extends AppCompatActivity {

    @Bind(R.id.tvTitle) TextView tvTitle;
    @Bind(R.id.tvNote) TextView tvNote;
    @Bind(R.id.btnEdit) Button btnEdit;
    @Bind(R.id.btnDelete) Button btnDelete;

    Todo myTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);

        ButterKnife.bind(this);

        final Todo todo = getIntent().getParcelableExtra("todo");
        tvTitle.setText(todo.getTitle());
        tvNote.setText(todo.getNote());

        myTodo = todo;

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmHapus();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TodoFormActivity.class);
                i.putExtra("mode","edit");
                i.putExtra("todo",myTodo);
                startActivity(i);
            }
        });
    }

    public void confirmHapus(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("Padam Todo")
                .content("Anda Pasti?")
                .negativeText("Batal").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .positiveText("Padam").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        hapusTodo();
                    }
                });
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public void hapusTodo(){
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading Data")
                .content("Please Wait")
                .progress(true,0)
                .build();
        dialog.show();

        TodoApi todoApi = TodoApi.Creator.newTodoApiService(this);
        Call<String> todoCall = todoApi.deleteTodo(myTodo.getId().toString());
        todoCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

}
