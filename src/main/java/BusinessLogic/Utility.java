package BusinessLogic;

import Model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.io.*;

public class Utility implements Serializable{
    TasksManagement manageEmployees;

    public Utility(TasksManagement manageEmployees)
    {
        this.manageEmployees = manageEmployees;
    }

    public String filterByWorkDuration()
    {
        String filteredEmployees = " ";
        ArrayList<Employee> employees = manageEmployees.getEmployees();
        ArrayList<Employee> employeesWith41HoursOrGreater = new ArrayList<>();
        for(Employee employee : employees)
        {
            if(manageEmployees.calculateEmployeeWorkDuration(employee.getIdEmployee()) > 40)
            {
                employeesWith41HoursOrGreater.add(employee);
            }
        }

        employeesWith41HoursOrGreater.sort(Comparator.comparingInt(
                employee ->manageEmployees.calculateEmployeeWorkDuration(employee.getIdEmployee())
        ));

        int cnt=employeesWith41HoursOrGreater.size()+1;
        for(Employee employee : employeesWith41HoursOrGreater)
        {
            cnt=cnt-1;
            int workingHours = manageEmployees.calculateEmployeeWorkDuration(employee.getIdEmployee());
            filteredEmployees += "Employee: " + employee.getName() + " is on top " + cnt + " based on the working hours, with " + workingHours+ " worked hours\n";
        }
        return filteredEmployees;
    }


    public HashMap numberOfCompletedUncompletedTasks()
    {
        HashMap<Employee,HashMap<String,Integer>>  map = new HashMap<>();
        ArrayList<Employee> employees = manageEmployees.getEmployees();
        HashMap<Employee,ArrayList<Task>> assignedTasks = manageEmployees.getAssignedTasksToEmployee();
        for(Employee employee : employees)
        {
            int completed = 0;
            int uncompleted = 0;
            HashMap<String,Integer> mapTasks = new HashMap<>();
            ArrayList<Task> tasks = assignedTasks.get(employee);
            if(tasks != null)
            {
                for (Task tasksOfEmployee : tasks)
                {
                    if (tasksOfEmployee.getStatusTask().equals("Completed"))
                    {
                        completed++;
                    }
                    else
                    {
                        uncompleted++;
                    }
                }
            }
            mapTasks.put("Completed",completed);
            mapTasks.put("Uncompleted",uncompleted);
            map.put(employee,mapTasks);
        }
        return map;
    }

}
