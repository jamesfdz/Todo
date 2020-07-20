package com.codechez.todo;

public class ItemList extends ItemId {
    private String task_content;
    private boolean checked;

    public ItemList(){}

    public ItemList(String task_content, boolean checked) {
        this.task_content = task_content;
        this.checked = checked;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
