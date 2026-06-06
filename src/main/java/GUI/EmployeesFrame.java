package GUI;

import BusinessLogic.TasksManagement;
import Data.SerializationOperations;
import Model.Employee;
import java.util.List;
import Model.Task;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EmployeesFrame extends JFrame {

    private TasksManagement manager;

    public EmployeesFrame()
    {
        this.manager = SerializationOperations.load();
        int nextIdEmployee = SerializationOperations.loadEmployeeNextId();
        Employee.setNextIdEmployee(nextIdEmployee);

        this.setSize(850, 650);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea outputArea = new JTextArea();

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewEmployeesTableBtn = new JButton("View Employees Table");
        row1.add(viewEmployeesTableBtn);
        mainPanel.add(row1);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField employeeNameField = new JTextField(15);
        JButton addEmployeeBtn = new JButton("Add Employee");
        row2.add(new JLabel("Employee Name:"));
        row2.add(employeeNameField);
        row2.add(addEmployeeBtn);
        mainPanel.add(row2);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField employeeIdRemoveField = new JTextField(5);
        JButton removeEmployeeBtn = new JButton("Remove Employee");
        row3.add(new JLabel("Employee ID to Remove:"));
        row3.add(employeeIdRemoveField);
        row3.add(removeEmployeeBtn);
        mainPanel.add(row3);

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton showEmployeesBtn = new JButton("Show Employees List");
        row4.add(showEmployeesBtn);
        mainPanel.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField employeeIdDurationField = new JTextField(5);
        JButton calculateDurationBtn = new JButton("Calculate Work Duration");
        row5.add(new JLabel("Employee ID:"));
        row5.add(employeeIdDurationField);
        row5.add(calculateDurationBtn);
        mainPanel.add(row5);

        JPanel row6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton filterEmployeesBtn = new JButton("Filter Employees by Work Duration");
        row6.add(filterEmployeesBtn);
        mainPanel.add(row6);

        viewEmployeesTableBtn.addActionListener(e ->
        {
            List<Employee> employees = manager.getEmployees();
            JTable table = TableGenerator.createTable(employees, Employee.class);
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            for (int i = table.getColumnCount() - 1; i >= 0; i--)
            {
                String colName = table.getColumnName(i).toLowerCase();
                if (colName.contains("nextid"))
                {
                    table.removeColumn(table.getColumnModel().getColumn(i));
                }
            }

            model.addColumn("Assigned Tasks");

            int targetColumnIndex = model.findColumn("Assigned Tasks");
            for (int i = 0; i < employees.size(); i++)
            {
                model.setValueAt("Show assigned tasks of employee", i, targetColumnIndex);
            }

            JFrame tableFrame = new JFrame("Employees List Table");
            tableFrame.setSize(650, 400);
            tableFrame.setLocationRelativeTo(this);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            table.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (row != -1 && col == targetColumnIndex)
                    {
                        Employee selectedEmp = employees.get(row);
                        HashMap<Employee, ArrayList<Task>> taskMap = manager.getMap();
                        ArrayList<Task> employeeTasks = taskMap.get(selectedEmp);

                        if (employeeTasks == null)
                        {
                            employeeTasks = new ArrayList<>();
                        }

                        JTable tasksTable = TableGenerator.createTable(employeeTasks, Task.class);

                        for (int i = tasksTable.getColumnCount() - 1; i >= 0; i--)
                        {
                            String colName = tasksTable.getColumnName(i).toLowerCase();
                            if (colName.contains("nextid"))
                            {
                                tasksTable.removeColumn(tasksTable.getColumnModel().getColumn(i));
                            }
                        }

                        JFrame tasksFrame = new JFrame("Tasks for " + selectedEmp.getName());
                        tasksFrame.setSize(600, 400);
                        tasksFrame.setLocationRelativeTo(tableFrame);
                        tasksFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JScrollPane scrollPane = new JScrollPane(tasksTable);
                        tasksFrame.add(scrollPane, BorderLayout.CENTER);
                        tasksFrame.setVisible(true);
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            tableFrame.add(scrollPane, BorderLayout.CENTER);
            tableFrame.setVisible(true);
        });

        addEmployeeBtn.addActionListener(e ->
        {
            String name = employeeNameField.getText();
            Employee emp = new Employee(name);
            try
            {
                manager.addEmployee(emp);
            }
            catch (IllegalArgumentException d)
            {
                outputArea.setText("Employee needs to have a name!");
                return;
            }
            SerializationOperations.save(manager);
            SerializationOperations.saveEmployeeNextId(emp.getNextIdEmployee());
            outputArea.setText("Added: " + emp);
        });

        removeEmployeeBtn.addActionListener(e ->
        {
            int idEmployee = Integer.parseInt(employeeIdRemoveField.getText());
            List<Employee> employees = manager.getEmployees();
            Employee empToRemove = null;

            for (Employee emp : employees)
            {
                if (emp.getIdEmployee() == idEmployee)
                {
                    empToRemove = emp;
                    break;
                }
            }

            if (empToRemove != null)
            {
                manager.removeEmployee(empToRemove);
                SerializationOperations.save(manager);
                outputArea.setText("Employee with ID: " + idEmployee + " has been successfully removed.");
            }
            else
            {
                outputArea.setText("Employee with ID: " + idEmployee + " could not be found.");
            }
        });

        showEmployeesBtn.addActionListener(e ->
        {
            StringBuilder sb = new StringBuilder();
            List<Employee> employees = manager.getEmployees();
            HashMap<Employee, java.util.ArrayList<Task>> taskMap = manager.getMap();

            for (Employee emp : employees)
            {
                sb.append("Employee: ").append(emp.getName()).append(" (ID: ").append(emp.getIdEmployee()).append(")\n");

                java.util.ArrayList<Task> employeeTasks = taskMap.get(emp);

                if (employeeTasks == null || employeeTasks.isEmpty())
                {
                    sb.append("Tasks: No tasks assigned.\n");
                }
                else
                {
                    sb.append("Tasks:\n").append(employeeTasks.toString()).append("\n");
                }
            }
            outputArea.setText(sb.toString());
        });

        calculateDurationBtn.addActionListener(e ->
        {
            int idEmployee = Integer.parseInt(employeeIdDurationField.getText());
            if (manager.findEmployee(idEmployee) == -1)
            {
                outputArea.setText("This Employee does not belong in the company");
            }
            else
            {
                int employeeWorkDuration = manager.calculateEmployeeWorkDuration(idEmployee);
                outputArea.setText("Employee with ID: " + idEmployee + " has worked " + employeeWorkDuration + " hours");
            }
        });

        filterEmployeesBtn.addActionListener(e ->
        {
            BusinessLogic.Utility utility = new BusinessLogic.Utility(manager);
            String rawData = utility.filterByWorkDuration();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Employee Rank & Details");
            model.addColumn("Total Work Duration");

            if (rawData != null && !rawData.trim().isEmpty())
            {
                String[] lines = rawData.split("\n");
                for (String line : lines)
                {
                    if (line.trim().isEmpty())
                        continue;

                    String details = line;
                    String duration = "/";

                    if (line.contains("with ") && line.contains(" worked hours"))
                    {
                        int idxWith = line.lastIndexOf("with ");
                        details = line.substring(0, idxWith).trim();
                        duration = line.substring(idxWith + 5).trim();
                    }

                    model.addRow(new Object[]{details, duration});
                }
            }

            JTable table = new JTable(model);

            JFrame tableFrame = new JFrame("Filtered Employees (>40 Hours)");
            tableFrame.setSize(750, 400);
            tableFrame.setLocationRelativeTo(this);
            tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JScrollPane scrollPane = new JScrollPane(table);
            tableFrame.add(scrollPane, BorderLayout.CENTER);
            tableFrame.setVisible(true);
        });

        this.add(mainPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        new EmployeesFrame();
    }
}