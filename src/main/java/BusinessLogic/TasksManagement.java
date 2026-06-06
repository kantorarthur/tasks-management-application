package BusinessLogic;


import Model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class TasksManagement implements Serializable {
    private HashMap<Employee, ArrayList<Task>> map;
    private ArrayList<Task> unassignedTasks;
    private ArrayList<Employee> employees;

    public TasksManagement()
    {
        this.map = new HashMap<>();
        this.unassignedTasks = new ArrayList<>();
        this.employees = new ArrayList<>();
    }

    public HashMap getMap()
    {
        return this.map;
    }

    public ArrayList getUnassignedTasks()
    {
        return this.unassignedTasks;
    }

    public ArrayList getEmployees()
    {
        return this.employees;
    }

    public void addUnassignedTask(Task task)
    {
        unassignedTasks.add(task);
    }

    public void removeUnassignedTask(Task task)
    {
        unassignedTasks.remove(task);
    }

    public void addEmployee(Employee employee)
    {
        if(employee.getName() == null || employee.getName().isEmpty())
        {
            throw new IllegalArgumentException("Employee needs to have a name!");
        }
        employees.add(employee);
    }


    public void assignTaskToEmployee(int idEmployee, int idTask)
    {
        Task task = null;
        for(Task unassignedT : unassignedTasks)
        {
            if(unassignedT.getIdTask() == idTask)
            {
                task = unassignedT;
                removeUnassignedTask(unassignedT);
                break;
            }
        }

        if(task == null)
        {
            System.out.println("The Task does not exist in the system.");
            return;
        }

        for(Employee employee : employees)
        {
            if(employee.getIdEmployee() == idEmployee)
            {
                if(map.containsKey(employee) == false)
                {
                    map.put(employee, new ArrayList<Task>());
                }

                map.get(employee).add(task);
            }
        }
    }

    public HashMap getAssignedTasksToEmployee()
    {
        return this.map;
    }

    public boolean isTaskAssignedToThisEmployee(int idEmployee, int idTask)
    {
        for(Employee employee : employees)
        {
            if(employee.getIdEmployee() == idEmployee)
            {
                ArrayList<Task> tasks = map.get(employee);
                for(Task task : tasks)
                {
                    if(task.getIdTask() == idTask)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void modifyTaskStatus(int idEmployee, int idTask)
    {
        for(Employee employee : employees)
        {
            if(employee.getIdEmployee() == idEmployee)
            {
                for(Task tasks : map.get(employee))
                {
                    if(tasks.getIdTask() == idTask)
                    {
                        if(tasks.getStatusTask().equals("Uncompleted"))
                        {
                            tasks.setStatusTask("Completed");
                        }
                        else
                        {
                            tasks.setStatusTask("Uncompleted");
                        }
                    }
                }
            }
        }
    }

    public boolean removeEmployee(Employee employee)
    {
        if (employee == null)
        {
            return false;
        }
        if (employees.contains(employee))
        {
            employees.remove(employee);
            map.remove(employee);
            return true;
        }

        return false;
    }

    public int calculateEmployeeWorkDuration(int idEmployee)
    {
        int estimateEmployeeWorkDuration = 0;
        for(Employee employee : employees)
        {
            if(employee.getIdEmployee() == idEmployee)
            {
                ArrayList<Task> employeeTasks = map.get(employee);

                if(employeeTasks == null || employeeTasks.isEmpty())
                {
                    continue;
                }

                for(Task tasks : employeeTasks)
                {
                    if(tasks.getStatusTask().equals("Completed"))
                    {
                        estimateEmployeeWorkDuration = estimateEmployeeWorkDuration + tasks.estimateDuration();
                    }
                }
            }
        }
        return estimateEmployeeWorkDuration;
    }

    public int findEmployee(int idEmployee)
    {
        ArrayList<Employee> employees = this.getEmployees();
        for(Employee employee : employees)
        {
            if(employee.getIdEmployee() == idEmployee)
                return idEmployee;
        }
        return -1;
    }

    public int findUnassignedTask(int idTask)
    {
        ArrayList<Task> tasks = this.getUnassignedTasks();
        for(Task task : tasks)
        {
            if(task.getIdTask() == idTask)
                return idTask;
        }
        return -1;
    }

    public void assignTaskToComplexTask(int idTask, int assigningIdTask)
    {
        Task assigningTask = null;
        ComplexTask complexTask = null;
        for(Task tasks : unassignedTasks)
        {
            if(tasks.getIdTask() == assigningIdTask)
            {
                assigningTask = tasks;
            }
            if(tasks.getIdTask() == idTask)
            {
                complexTask = (ComplexTask) tasks;
            }
        }

        if(complexTask != null && assigningTask != null)
        {
            complexTask.addTask(assigningTask);
            unassignedTasks.remove(assigningTask);
        }

    }

    @Override
    public String toString()
    {
        return "Unassigned tasks: " + getUnassignedTasks().toString() + "\n Employees: " + getEmployees().toString() + "\n Assigned tasks to employees: "
                + getAssignedTasksToEmployee().toString() + "\n";
    }

}
