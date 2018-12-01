package numan.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
