package numan.todolist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    public static final String DBNAME = "ToDoList.sqlite";
    public static final String DBLOCATION = "/data/data/numan.todolist/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDatabase() {
        try {
            if (mDatabase != null && mDatabase.isOpen()) {
                return;
            }
            String dbPath = mContext.getDatabasePath(DBNAME).getPath();
            this.getWritableDatabase();
            mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(Exception ex)
        {
            Log.e("1 DBHelper Exception" , ex + "");
        }
    }

    public Boolean InsertTask(String Title , String Description, String Status , String Priority, String currentDateTime) {
        try {
            openDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Title", Title.trim());
            contentValues.put("Discription", Description);
            contentValues.put("Status", Status.trim());
            contentValues.put("Priority", Priority.trim());
            contentValues.put("CreatedOn", currentDateTime.trim());
            long insert = mDatabase.insert("RecordsHistory", null, contentValues);
            if (insert == -1)
                return false;
            else
                return true;
        }catch(Exception ex)
        {
            Log.e("2 DBHelper Exception" , ex + "");
        }
        return false;
    }


    public Integer updateTask(Integer TaskID, String Title , String Description, String Status , String Priority, String currentDateTime) {
        try {
            openDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("Title", Title.trim());
            contentValues.put("Discription", Description);
            contentValues.put("Status", Status.trim());
            contentValues.put("Priority", Priority.trim());
            contentValues.put("CreatedOn", currentDateTime.trim());
            return mDatabase.update("RecordsHistory", contentValues, "ID = " + TaskID, null);
        } catch(Exception ex)
        {
            Log.e("update DB Exception " , ex + "");
        }
        return 0;
    }

    public Cursor getAllTasks() {
        try {
            openDatabase();
            return mDatabase.rawQuery("SELECT ID, Title, Discription, Status, Priority, CreatedOn FROM RecordsHistory", null);
        } catch(Exception ex)
        {
            Log.e("3 DBHelper Exception" , ex + "");
        }
        return null;
    }

    public Cursor getSingleTask(int TaskID) {
        try {
            openDatabase();
            return mDatabase.rawQuery("SELECT * FROM RecordsHistory WHERE categoryId = " + TaskID + " ORDER BY DisplayOrder ASC", null);
        }catch(Exception ex)
        {
            Log.e("4 DBHelper Exception" , ex + "");
        }
        return null;
    }

    public void deleteTask(Integer ID) {
        try {
            openDatabase();
            mDatabase.execSQL("delete from RecordsHistory WHERE ID = " + ID);
        }catch(Exception ex)
        {
            Log.e("Delete DB Exception " , ex + "");
        }
    }

    public void deleteAllTask() {
        try {
            openDatabase();
            mDatabase.execSQL("delete from RecordsHistory WHERE Status = 'Complete'");
        }catch(Exception ex)
        {
            Log.e("Delete DB Exception " , ex + "");
        }
    }

}

