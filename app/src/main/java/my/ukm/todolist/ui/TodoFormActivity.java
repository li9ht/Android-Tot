package my.ukm.todolist.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.ukm.todolist.R;
import my.ukm.todolist.model.Todo;
import my.ukm.todolist.net.TodoApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoFormActivity extends AppCompatActivity
    implements View.OnClickListener
{
    @Bind(R.id.etTitle) EditText etTitle;
    @Bind(R.id.etNote) EditText etNote;
    @Bind(R.id.btnSimpan) Button btnSimpan;
    @Bind(R.id.btnBatal) Button btnBatal;

    public String mode = "new";
    public Todo myTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_form);
        ButterKnife.bind(this);
        btnSimpan.setOnClickListener(this);
        btnBatal.setOnClickListener(this);

        mode = getIntent().getStringExtra("mode");
        if(mode.equals("edit")){
            myTodo = getIntent().getParcelableExtra("todo");
            etTitle.setText(myTodo.getTitle());
            etNote.setText(myTodo.getNote());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSimpan:
                if(mode.equals("new")){
                    simpanNewTodo();
                }else{
                    kemaskiniTodo();
                }
                break;
            case R.id.btnBatal:
                batalForm();
                break;
        }
    }

    public void kemaskiniTodo(){
        String title = etTitle.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading Data")
                .content("Please Wait")
                .progress(true,0)
                .build();
        dialog.show();

        TodoApi todoApi = TodoApi.Creator.newTodoApiService(this);
        Call<String> callNew = todoApi.updateTodo(myTodo.getId().toString(),title,note);
        callNew.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("test",t.getLocalizedMessage());
                dialog.dismiss();
            }
        });
    }

    public void simpanNewTodo(){

        String title = etTitle.getText().toString().trim();
        String note = etNote.getText().toString().trim();

        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Loading Data")
                .content("Please Wait")
                .progress(true,0)
                .build();
        dialog.show();

        TodoApi todoApi = TodoApi.Creator.newTodoApiService(this);
        Call<String> callNew = todoApi.newTodo("K018531",title,note);
        callNew.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                String result = response.body().trim();
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("test",t.getLocalizedMessage());
                dialog.dismiss();
            }
        });


    }
    public void batalForm(){
        finish();
    }
}
