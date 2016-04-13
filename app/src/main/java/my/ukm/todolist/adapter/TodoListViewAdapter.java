package my.ukm.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import my.ukm.todolist.R;
import my.ukm.todolist.model.Todo;
import my.ukm.todolist.ui.TodoDetailActivity;

/**
 * Created by Makmal PTM on 13/04/2016.
 */
public class TodoListViewAdapter extends ArrayAdapter<Todo> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public TodoListViewAdapter(Context context, int resource) {
        super(context, resource);

        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.todo_list_view_item,parent,false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Todo todo = (Todo) v.findViewById(R.id.etTitle).getTag();
                    Intent i = new Intent(mContext, TodoDetailActivity.class);
                    i.putExtra("todo",todo);
                    mContext.startActivity(i);
                }
            });
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Todo todo = getItem(position);
        vh.tvTitle.setText(todo.getTitle());
        vh.tvNote.setText(todo.getNote());
        vh.tvTitle.setTag(todo);

        return convertView;
    }

    public static class ViewHolder{

        @Bind(R.id.etTitle) TextView tvTitle;
        @Bind(R.id.etNote) TextView tvNote;

        ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
