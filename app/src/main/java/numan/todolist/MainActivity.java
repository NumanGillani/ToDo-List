package numan.todolist;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import numan.todolist.Adapter.CustomAdapter;
import numan.todolist.Adapter.RowItem;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import static numan.todolist.Adapter.CustomAdapter.myCreatedOn;
import static numan.todolist.Adapter.CustomAdapter.myPriority;
import static numan.todolist.Adapter.CustomAdapter.myStatus;
import static numan.todolist.Adapter.CustomAdapter.myTaskDescription;
import static numan.todolist.Adapter.CustomAdapter.myTaskID;
import static numan.todolist.Adapter.CustomAdapter.myTaskTitle;

public class MainActivity extends AppCompatActivity {

    Typeface font;
    public static List<RowItem> rowItems;
    public static ListView taskList;

    public static DatabaseHelper mDBHelper;
    public static RowItem item;
    public static LinearLayout empty_layout, completeList;
    public static Context mContext;

    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    LoginButton loginButton;
    TextView userName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_main);

            Initialize();
            LoadTasks();
            FacebookLogin();

        } catch(Exception ex)
        {
            Log.e("CreateTask Exception" , ex + "");
        }
    }

    private void Initialize() {
        empty_layout = (LinearLayout) findViewById(R.id.empty_layout);
        completeList = (LinearLayout) findViewById(R.id.completeList);
        taskList = (ListView) findViewById(R.id.taskList);
        rowItems = new ArrayList<RowItem>();
        mContext = MainActivity.this;

        mDBHelper = new DatabaseHelper(this);
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            mDBHelper.getReadableDatabase();

            if (!copyDatabase(this)) {
                Toast.makeText(this, "SQLite Error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        }catch (Exception e) {
            Log.e("Catch " , "Exception");
            return false;
        }
    }

    public static void LoadTasks() {
        try {
            rowItems.clear();
            myTaskTitle.clear();
            myTaskDescription.clear();
            myCreatedOn.clear();
            myPriority.clear();
            myStatus.clear();
            myTaskID.clear();

            Cursor data = mDBHelper.getAllTasks();
            data.moveToFirst();
            Log.e("Count" , data.getCount() + "");
            if(data.getCount() == 0)
            {
                empty_layout.setVisibility(View.VISIBLE);
                completeList.setVisibility(View.GONE);
            } else
            {
                empty_layout.setVisibility(View.GONE);
                completeList.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < data.getCount(); i++) {
                item = new RowItem(data.getInt(0), data.getString(1), data.getString(2), data.getString(3), data.getString(4), data.getString(5));
                rowItems.add(item);
                data.moveToNext();
            }
            CustomAdapter adapter = new CustomAdapter(mContext, rowItems);
            taskList.setAdapter(adapter);

            if (data != null)
                data.close();
        } catch(Exception ex)
        {
            Log.e("CreateTask Exception" , ex + "");
        }
    }

    public void FacebookLogin() {
        try{
            AppEventsLogger.activateApp(this);
            callbackManager = CallbackManager.Factory.create();

            userName = (TextView) findViewById(R.id.userName);
            loginButton = (LoginButton) findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList(EMAIL));
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    userName.setText(Profile.getCurrentProfile().getName());
                }

                @Override
                public void onCancel() {
                    userName.setText("Login Cancelled !");
                }

                @Override
                public void onError(FacebookException exception) {
                    userName.setText("Login Error !");
                }
            });


            callbackManager = CallbackManager.Factory.create();
            userName.setText(Profile.getCurrentProfile().getName());
        } catch(Exception ex) { }

    }

    public void CreateNewTask(View v) {
        Intent addIntent = new Intent(this, CreateTask.class);
        startActivity(addIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            LoadTasks();
        } else if(id == R.id.action_delete){
            showUpdateStatus();
        } else if(id == R.id.action_instructions){
            Intent instructions = new Intent(this, InstructionsScreen.class);
            startActivity(instructions);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUpdateStatus() {
        final Dialog dialog  = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.complete_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        wmlp.windowAnimations = R.style.DialogAnimation;
        TextView complete = (TextView) dialog.findViewById(R.id.complete);
        complete.setText("Delete All");
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBHelper.deleteAllTask();
                TastyToast.makeText(MainActivity.this, "All Completed Tasks has been Completed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                MainActivity.LoadTasks();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showInstructions() {
        final Dialog dialog  = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.complete_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        wmlp.windowAnimations = R.style.DialogAnimation;
        TextView complete = (TextView) dialog.findViewById(R.id.complete);
        complete.setText("Delete All");
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBHelper.deleteAllTask();
                TastyToast.makeText(MainActivity.this, "All Completed Tasks has been Completed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                MainActivity.LoadTasks();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        try{
            userName.setText(Profile.getCurrentProfile().getName());
        } catch(Exception ex) { }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        Log.e("On Resume" , "Executed !");
        LoadTasks();
        super.onResume();
    }
}