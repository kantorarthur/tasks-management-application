package GUI;

import BusinessLogic.TasksManagement;
import Data.SerializationOperations;
import Model.ComplexTask;
import Model.SimpleTask;
import Model.Task;
import Model.Employee;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TasksFrame extends JFrame {

    private TasksManagement manager;

    public TasksFrame()
    {
        this.manager = SerializationOperations.load();
        int nextIdTask = SerializationOperations.loadTaskNextId();
        Task.setNextIdTask(nextIdTask);

        this.setSize(850, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JFrame frame = new JFrame("Tasks Manager");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea outputArea = new JTextArea();

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewUnassignedTasksBtn = new JButton("View Unassigned Tasks");
        row1.add(viewUnassignedTasksBtn);
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField startHourField = new JTextField(5);
        JTextField endHourField = new JTextField(5);
        JButton addSimpleTaskBtn = new JButton("Create Simple Task");
        row2.add(new JLabel("Start Hour:"));
        row2.add(startHourField);
        row2.add(new JLabel("  End Hour:"));
        row2.add(endHourField);
        row2.add(addSimpleTaskBtn);
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addComplexTaskBtn = new JButton("Create Complex Task");
        row3.add(addComplexTaskBtn);
        mainPanel.add(row3);

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField employeeIdAssignField = new JTextField(5);
        JTextField taskIdAssignField = new JTextField(5);
        JButton assignTaskBtn = new JButton("Assign Task to Employee");
        row4.add(new JLabel("Employee ID:"));
        row4.add(employeeIdAssignField);
        row4.add(new JLabel("  Task ID:"));
        row4.add(taskIdAssignField);
        row4.add(assignTaskBtn);
        mainPanel.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField taskIdRemoveField = new JTextField(5);
        JButton removeTaskBtn = new JButton("Remove Task");
        row5.add(new JLabel("Task ID to Remove:"));
        row5.add(taskIdRemoveField);
        row5.add(removeTaskBtn);
        mainPanel.add(row5);

        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField employeeIdStatusField = new JTextField(5);
        JTextField taskIdStatusField = new JTextField(5);
        JButton modifyTaskStatusBtn = new JButton("Modify Task Status");
        row6.add(new JLabel("Employee ID:"));
        row6.add(employeeIdStatusField);
        row6.add(new JLabel("  Task ID:"));
        row6.add(taskIdStatusField);
        row6.add(modifyTaskStatusBtn);
        mainPanel.add(row6);

        JPanel row7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField complexTaskIdField = new JTextField(5);
        JTextField subTaskIdField = new JTextField(5);
        JButton assignTaskToComplexTaskBtn = new JButton("Assign Task to Complex Task");
        row7.add(new JLabel("Complex Task ID:"));
        row7.add(complexTaskIdField);
        row7.add(new JLabel("  Sub-Task ID:"));
        row7.add(subTaskIdField);
        row7.add(assignTaskToComplexTaskBtn);
        mainPanel.add(row7);

        viewUnassignedTasksBtn.addActionListener(e ->
        {
            List<Task> unassignedTasks = manager.getUnassignedTasks();
            JTable table = TableGenerator.createTable(unassignedTasks, Task.class);
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            int modelIndex = model.findColumn("nextIdTask");
            if (modelIndex != -1)
            {
                table.removeColumn(table.getColumnModel().getColumn(
                        table.convertColumnIndexToView(modelIndex)
                ));
            }

            model.addColumn("Estimated duration");

            int targetColumnIndex = model.findColumn("Estimated duration");
            for (int i = 0; i < unassignedTasks.size(); i++)
            {
                Task task = unassignedTasks.get(i);
                model.setValueAt(task.estimateDuration(), i, targetColumnIndex);
            }

            JFrame tableFrame = new JFrame("Unassigned Tasks List");
            tableFrame.setSize(600, 400);
            tableFrame.setLocationRelativeTo(this);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JScrollPane scrollPane = new JScrollPane(table);
            tableFrame.add(scrollPane, BorderLayout.CENTER);
            tableFrame.setVisible(true);
        });

        addSimpleTaskBtn.addActionListener(e ->
        {
            int start = Integer.parseInt(startHourField.getText());
            int end = Integer.parseInt(endHourField.getText());

            SimpleTask sTask = new SimpleTask(start, end);
            manager.addUnassignedTask(sTask);
            SerializationOperations.save(manager);
            SerializationOperations.saveTaskNextId(sTask.getNextIdTask());
            outputArea.setText("Simple Task added: " + sTask);
        });

        addComplexTaskBtn.addActionListener(e ->
        {
            Task task = new ComplexTask();
            manager.addUnassignedTask(task);
            SerializationOperations.save(manager);
            SerializationOperations.saveTaskNextId(task.getNextIdTask());
            outputArea.setText("Complex Task added: " + task);
        });

        assignTaskBtn.addActionListener(e ->
        {
            int idEmployee = Integer.parseInt(employeeIdAssignField.getText());
            int idTask = Integer.parseInt(taskIdAssignField.getText());
            if (manager.findUnassignedTask(idTask) == -1)
            {
                outputArea.setText("This task does not appear in the unassigned tasks list");
            }
            else if (manager.findEmployee(idEmployee) == -1)
            {
                outputArea.setText("This Employee does not belong in the company");
            }
            else
            {
                manager.assignTaskToEmployee(idEmployee, idTask);
                SerializationOperations.save(manager);
                outputArea.setText("Task: " + idTask + " assigned to Employee: " + idEmployee);
            }
        });

        removeTaskBtn.addActionListener(e ->
        {
            int idTask = Integer.parseInt(taskIdRemoveField.getText());
            List<Task> unassignedTasks = manager.getUnassignedTasks();
            Task taskToRemove = null;

            for (Task t : unassignedTasks)
            {
                if (t.getIdTask() == idTask)
                {
                    taskToRemove = t;
                    break;
                }
            }

            if (taskToRemove != null)
            {
                manager.removeUnassignedTask(taskToRemove);
                SerializationOperations.save(manager);
                outputArea.setText("Task with ID: " + idTask + " has been successfully removed.");
            }
            else
            {
                outputArea.setText("Task with ID: " + idTask + " could not be found in the unassigned tasks list.");
            }
        });

        modifyTaskStatusBtn.addActionListener(e ->
        {
            int idEmployee = Integer.parseInt(employeeIdStatusField.getText());
            int idTask = Integer.parseInt(taskIdStatusField.getText());
            if (manager.findEmployee(idEmployee) == -1)
            {
                outputArea.setText("This Employee does not belong in the company");
            }
            else if (manager.isTaskAssignedToThisEmployee(idEmployee, idTask) == false)
            {
                outputArea.setText("This Task it's not assigned to this Employee");
            }
            else
            {
                manager.modifyTaskStatus(idEmployee, idTask);
                SerializationOperations.save(manager);
                outputArea.setText("The status of the Task with ID: " + idTask + " assigned to Employee with ID: " + idEmployee + " has been modified");
            }
        });

        assignTaskToComplexTaskBtn.addActionListener(e ->
        {
            int idCTask = Integer.parseInt(complexTaskIdField.getText());
            int idTask = Integer.parseInt(subTaskIdField.getText());
            if (manager.findUnassignedTask(idCTask) == -1)
            {
                outputArea.setText("The Task that you are trying to assign to does not exist in the list!");
            }
            else if (manager.findUnassignedTask(idTask) == -1)
            {
                outputArea.setText("The Task that you are trying to assign does not exist in the list!");
            }
            else
            {
                manager.assignTaskToComplexTask(idCTask, idTask);
                SerializationOperations.save(manager);
                outputArea.setText("Task with ID: " + idTask + " ,assigned to Complex Task with ID:" + idCTask);
            }
        });

        this.add(mainPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        new TasksFrame();
    }
}