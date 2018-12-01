package numan.todolist.Adapter;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

import numan.todolist.CreateTask;
import numan.todolist.DatabaseHelper;
import numan.todolist.R;
import numan.todolist.MainActivity;

import static numan.todolist.MainActivity.mDBHelper;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItem> rowItems;
    public static List<String> myTaskTitle =  new ArrayList();
    public static List<String> myTaskDescription =  new ArrayList();
    public static List<String> myCreatedOn =  new ArrayList();
    public static  List<String> myStatus =  new ArrayList();
    public static  List<String> myPriority =  new ArrayList();
    public static List<Integer> myTaskID =  new ArrayList();
    public CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItems = rowItems;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {

        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }

    /* private view holder class */
    private class ViewHolder {
        TextView TaskTitle;
        TextView Description;
        TextView CurrentStatus;
        TextView CreatedOn;
        TextView CompleteText;
        Button Complete;
        Button Update;
        Button Delete;
        FrameLayout frameColor;
        LinearLayout buttonsLayout;
        DatabaseHelper mDBHelper;
    }

    @SuppressLint("ResourceAsColor")
    @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        final ViewHolder holder;
        try {
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);


                holder = new ViewHolder();
                holder.TaskTitle = (TextView) v
                        .findViewById(R.id.TaskTitle);
                holder.Description = (TextView) v
                        .findViewById(R.id.Description);
                holder.CurrentStatus = (TextView) v
                        .findViewById(R.id.CurrentStatus);
                holder.CreatedOn = (TextView) v
                        .findViewById(R.id.CreatedOn);
                holder.CompleteText = (TextView) v
                        .findViewById(R.id.completeText);
                holder.frameColor = (FrameLayout) v
                        .findViewById(R.id.status_bg);
                holder.Complete = (Button) v
                        .findViewById(R.id.completeButton);
                holder.Update = (Button) v
                        .findViewById(R.id.updateButton);
                holder.Delete = (Button) v
                        .findViewById(R.id.deleteButton);
                holder.buttonsLayout = (LinearLayout) v
                        .findViewById(R.id.buttonsLayout);
                holder.mDBHelper = new DatabaseHelper(context);

                v.setTag(holder);
            } else
                holder = (ViewHolder) v.getTag();

            myTaskID.add(rowItems.get(position).getTaskID());
            myTaskTitle.add(rowItems.get(position).getTaskTitle());
            myTaskDescription.add(rowItems.get(position).getDescription());
            myCreatedOn.add(rowItems.get(position).getCreateDate());
            myStatus.add(rowItems.get(position).getTaskStatus());
            myPriority.add(rowItems.get(position).getTaskPriority());

            holder.TaskTitle.setText(myTaskTitle.get(position));
            holder.Description.setText(myTaskDescription.get(position));
            holder.CurrentStatus.setText(myStatus.get(position));
            holder.CreatedOn.setText(myCreatedOn.get(position));

            if(myPriority.get(position).equals("Low"))
                holder.frameColor.setBackgroundColor(context.getResources().getColor(R.color.green));
            else if(myPriority.get(position).equals("Medium"))
                holder.frameColor.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            else
                holder.frameColor.setBackgroundColor(context.getResources().getColor(R.color.red));

            if(myStatus.get(position).equals("Complete"))
            {
                holder.buttonsLayout.setVisibility(View.GONE);
                holder.CompleteText.setVisibility(View.VISIBLE);
                holder.frameColor.setBackgroundColor(context.getResources().getColor(R.color.parrot));
            }

            holder.Complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUpdateStatus(position);
                }
            });

            holder.Update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, CreateTask.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("TaskID" , myTaskID.get(position));
                    bundle.putString("TaskTitle" , myTaskTitle.get(position));
                    bundle.putString("Description" , myTaskDescription.get(position));
                    bundle.putString("TaskStatus" , myStatus.get(position));
                    bundle.putString("CreateDate" , myCreatedOn.get(position));
                    bundle.putString("TaskPriority" , myPriority.get(position));
                    update.putExtras(bundle);
                    context.startActivity(update);
                }
            });

            holder.Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.mDBHelper.deleteTask(myTaskID.get(position));
                    TastyToast.makeText(context, "Task has been Deleted", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                    MainActivity.LoadTasks();
                }
            });

        }
        catch (Exception ex)
        {
            Log.e("Custom Main Exception" , ex+"");
        }
            return v;
    }

    private void showUpdateStatus(final int position) {
        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.complete_dialog);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM;
        wmlp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        wmlp.windowAnimations = R.style.DialogAnimation;
        TextView message = (TextView) dialog.findViewById(R.id.message);
        TextView complete = (TextView) dialog.findViewById(R.id.complete);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer updateValue = mDBHelper.updateTask(myTaskID.get(position), myTaskTitle.get(position), myTaskDescription.get(position), "Complete", myPriority.get(position), myCreatedOn.get(position));
                if(updateValue != 0)
                {
                    TastyToast.makeText(context, "Task has been Completed", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                    myStatus.set(position, "Complete");
                    MainActivity.LoadTasks();
                } else
                {
                    TastyToast.makeText(context, "Sorry, Something went wrong", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                }
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

}

