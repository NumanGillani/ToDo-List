package numan.todolist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTask extends AppCompatActivity {

    public DatabaseHelper mDBHelper;
    Spinner taskStatus, taskPriority;
    EditText taskTitle, taskDescription;
    Button footerButton;
    Bundle bundle;
    Boolean update = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.create_task);
            mDBHelper = new DatabaseHelper(this);

            Initialize();
            footerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyTask();
                }
            });
        } catch(Exception ex)
        {
            Log.e("CreateTask Exception" , ex + "");
        }
    }

    private void Initialize() {

        taskTitle = (EditText) findViewById(R.id.input_title);
        taskDescription = (EditText) findViewById(R.id.input_details);
        taskStatus = (Spinner) findViewById(R.id.input_status);
        taskPriority = (Spinner) findViewById(R.id.input_priority);
        footerButton = (Button) findViewById(R.id.footerButton);

        try{
            bundle = getIntent().getExtras();
            taskTitle.setText(bundle.getString("TaskTitle"));
            taskDescription.setText(bundle.getString("Description"));
            for (int i=0;i<taskStatus.getCount();i++){
                if (taskStatus.getItemAtPosition(0).toString().equalsIgnoreCase(bundle.getString("TaskStatus")))
                    taskStatus.setSelection(0);
            }
            for (int i=0;i<taskPriority.getCount();i++){
                if (taskPriority.getItemAtPosition(0).toString().equalsIgnoreCase(bundle.getString("TaskPriority")))
                    taskPriority.setSelection(0);
            }
            if(!bundle.getString("TaskTitle").equals(null))
            {
                footerButton.setText("Update Task");
                update = true;
            }

        } catch(Exception ex)
        {
            Log.e("Update Exception" , ex + "");
        }

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

    public Boolean MyTask() {
        String Title = taskTitle.getText().toString();
        String Description = taskDescription.getText().toString();
        String Status = taskStatus.getSelectedItem().toString();
        String Priority = taskPriority.getSelectedItem().toString();
        if(Title.equals("")||Title.equals(null))
        {
            taskTitle.setError("Title can't be Empty !");
            taskTitle.requestFocus();
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy - HH:mm");
        String currentDateTime = sdf.format(new Date());
        Boolean insert = null;
        if(!update)
        {
            insert = mDBHelper.InsertTask(Title, Description, Status, Priority, currentDateTime);
            if(insert)
            {
                TastyToast.makeText(getApplicationContext(), "Task has been Added", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                setDefaults();
                return true;
            } else
            {
                TastyToast.makeText(getApplicationContext(), "Sorry, Something went wrong", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                return false;
            }
        }
        else
        {
            Integer update1 = mDBHelper.updateTask(bundle.getInt("TaskID"), Title, Description, Status, Priority, currentDateTime);
            if(update1 != 0)
            {
                TastyToast.makeText(getApplicationContext(), "Task has been Updated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                update = false;
                footerButton.setText("Submit Task");

                setDefaults();
                return true;
            } else
            {
                TastyToast.makeText(getApplicationContext(), "Sorry, Something went wrong", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                return false;
            }
        }

    }

    public void setDefaults()
    {
        taskTitle.setText("");
        taskDescription.setText("");
        taskStatus.setSelection(0);
        taskPriority.setSelection(0);

    }

}
