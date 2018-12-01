package numan.todolist.Adapter;

public class RowItem {

    private Integer TaskID;
    private String TaskTitle;
    private String Description;
    private String TaskStatus;
    private String CreateDate;
    private String TaskPriority;

    public RowItem(Integer TaskID, String TaskTitle, String Description, String TaskStatus, String TaskPriority, String CreateDate) {

        this.TaskID = TaskID;
        this.TaskTitle = TaskTitle;
        this.Description = Description;
        this.TaskStatus = TaskStatus;
        this.TaskPriority = TaskPriority;
        this.CreateDate = CreateDate;

    }

    public Integer getTaskID() {
        return TaskID;
    }

    public String getTaskTitle() {
        return TaskTitle;
    }

    public String getDescription() {
        return Description;
    }

    public String getTaskStatus() {
        return TaskStatus;
    }

    public String getTaskPriority() {
        return TaskPriority;
    }

    public String getCreateDate() {
        return CreateDate;
    }


}
