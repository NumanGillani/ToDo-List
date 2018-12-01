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

public class InstructionsScreen extends AppCompatActivity {

    public DatabaseHelper mDBHelper;
    Spinner taskStatus, taskPriority;
    EditText taskTitle, taskDescription;
    Button footerButton;
    Bundle bundle;
    Boolean update = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
    }
}
