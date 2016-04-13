package my.ukm.todolist.net;

import android.content.Context;

import java.util.List;

import my.ukm.todolist.model.Todo;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Makmal PTM on 13/04/2016.
 */
public interface TodoApi {

    public static String ENDPOINT = "http://smkphp.ukm.my/test/todo/api/todo/";

    @GET("list")
    Call<List<Todo>> getList(
         @Query("owner") String owner
    );

    @FormUrlEncoded
    @POST("new")
    Call<String> newTodo(
        @Field("owner") String owner,
        @Field("title") String title,
        @Field("note") String note
    );

    @FormUrlEncoded
    @POST("update")
    Call<String> updateTodo(
        @Field("id") String id,
        @Field("title") String title,
        @Field("note") String note
    );

    @FormUrlEncoded
    @POST("delete")
    Call<String> deleteTodo(
        @Field("id") String id
    );

    class Creator{

        private static TodoApi mInstance = null;

        public static TodoApi newTodoApiService(Context context){
            if(mInstance == null){
                mInstance = Creator.newInstance(context);
            }
            return mInstance;
        }

        public static TodoApi newInstance(final Context context){
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return  retrofit.create(TodoApi.class);
        }
    }
}
