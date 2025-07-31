import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced Personal Management System
 * Demonstrates: Collections, Streams, Lambda expressions, Design patterns
 */
public class PersonalManagementSystem {
    private List<Person> people;
    private List<Employee> employees;
    private Map<String, Employee> employeeMap; // Quick lookup by ID
    private Map<String, List<Employee>> departmentMap; // Group by department
    
    // Statistics tracking
    private int totalOperations;
    private Date lastModified;
    
    public PersonalManagementSystem() {
        this.people = new ArrayList<>();
        this.employees = new ArrayList<>();
        this.employeeMap = new HashMap<>();
        this.departmentMap = new HashMap<>();
        this.totalOperations = 0;
        this.lastModified = new Date();
    }
    
    // Person management methods
    public void addPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        
        // Check for duplicates
        if (people.contains(person)) {
            throw new IllegalArgumentException("Person already exists in the system");
        }
        
        people.add(person);
        totalOperations++;
        lastModified = new Date();
        System.out.println("✓ Added person: " + person.getName());
    }
    
    public boolean removePerson(String name) {
        Person toRemove = findPersonByName(name);
        if (toRemove != null) {
            people.remove(toRemove);
            totalOperations++;
            lastModified = new Date();
            System.out.println("✓ Removed person: " + name);
            return true;
        }
        return false;
    }
    
    public Person findPersonByName(String name) {
        return people.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElse(null);
    }
    
    public List<Person> findPeopleByAge(int minAge, int maxAge) {
        return people.stream()
                    .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
                    .collect(Collectors.toList());
    }
    
    // Employee management methods
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        
        // Check for duplicate employee ID
        if (employeeMap.containsKey(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists: " + employee.getEmployeeId());
        }
        
        employees.add(employee);
        employeeMap.put(employee.getEmployeeId(), employee);
        
        // Add to department map
        String dept = employee.getDepartment();
        departmentMap.computeIfAbsent(dept, k -> new ArrayList<>()).add(employee);
        
        // Also add to people list
        addPerson(employee);
        
        System.out.println("✓ Added employee: " + employee.getName() + " (ID: " + employee.getEmployeeId() + ")");
    }
    
    public Employee findEmployeeById(String employeeId) {
        return employeeMap.get(employeeId.toUpperCase());
    }
    
    public List<Employee> getEmployeesByDepartment(String department) {
        return departmentMap.getOrDefault(department, new ArrayList<>());
    }
    
    public List<Employee> getActiveEmployees() {
        return employees.stream()
                       .filter(Employee::isActive)
                       .collect(Collectors.toList());
    }
    
    public List<Employee> getInactiveEmployees() {
        return employees.stream()
                       .filter(e -> !e.isActive())
                       .collect(Collectors.toList());
    }
    
    // Advanced search and filtering
    public List<Employee> searchEmployees(String searchTerm) {
        String term = searchTerm.toLowerCase();
        return employees.stream()
                       .filter(e -> e.getName().toLowerCase().contains(term) ||
                                  e.getDepartment().toLowerCase().contains(term) ||
                                  e.getPosition().toLowerCase().contains(term) ||
                                  e.getEmployeeId().toLowerCase().contains(term))
                       .collect(Collectors.toList());
    }
    
    public List<Employee> getEmployeesBySalaryRange(double minSalary, double maxSalary) {
        return employees.stream()
                       .filter(e -> e.getTotalSalary() >= minSalary && e.getTotalSalary() <= maxSalary)
                       .collect(Collectors.toList());
    }
    
    public List<Employee> getTopEarners(int count) {
        return employees.stream()
                       .filter(Employee::isActive)
                       .sorted((e1, e2) -> Double.compare(e2.getTotalSalary(), e1.getTotalSalary()))
                       .limit(count)
                       .collect(Collectors.toList());
    }
    
    // Sorting methods
    public List<Person> getPeopleSortedByName() {
        return people.stream()
                    .sorted()  // Uses Comparable implementation
                    .collect(Collectors.toList());
    }
    
    public List<Employee> getEmployeesSortedBySalary() {
        return employees.stream()
                       .sorted((e1, e2) -> Double.compare(e2.getTotalSalary(), e1.getTotalSalary()))
                       .collect(Collectors.toList());
    }
    
    public List<Employee> getEmployeesSortedByExperience() {
        return employees.stream()
                       .sorted((e1, e2) -> Integer.compare(e2.getYearsOfService(), e1.getYearsOfService()))
                       .collect(Collectors.toList());
    }
    
    // Statistics and analytics
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("Total People", people.size());
        stats.put("Total Employees", employees.size());
        stats.put("Active Employees", getActiveEmployees().size());
        stats.put("Departments", departmentMap.size());
        stats.put("Total Operations", totalOperations);
        stats.put("Last Modified", lastModified);
        
        if (!employees.isEmpty()) {
            double avgSalary = employees.stream()
                                      .filter(Employee::isActive)
                                      .mapToDouble(Employee::getTotalSalary)
                                      .average()
                                      .orElse(0.0);
            stats.put("Average Salary", String.format("$%.2f", avgSalary));
            
            double totalPayroll = employees.stream()
                                          .filter(Employee::isActive)
                                          .mapToDouble(Employee::getTotalSalary)
                                          .sum();
            stats.put("Total Payroll", String.format("$%.2f", totalPayroll));
        }
        
        return stats;
    }
    
    public Map<String, Integer> getDepartmentStats() {
        return employees.stream()
                       .filter(Employee::isActive)
                       .collect(Collectors.groupingBy(
                           Employee::getDepartment,
                           Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                       ));
    }
    
    public Map<String, Double> getDepartmentPayroll() {
        return employees.stream()
                       .filter(Employee::isActive)
                       .collect(Collectors.groupingBy(
                           Employee::getDepartment,
                           Collectors.summingDouble(Employee::getTotalSalary)
                       ));
    }
    
    // Bulk operations
    public void giveRaiseToAll(double percentage) {
        employees.stream()
                .filter(Employee::isActive)
                .forEach(e -> e.giveRaise(percentage));
        
        totalOperations += employees.size();
        lastModified = new Date();
        System.out.println("✓ Applied " + percentage + "% raise to all active employees");
    }
    
    public void giveRaiseToDepartment(String department, double percentage) {
        List<Employee> deptEmployees = getEmployeesByDepartment(department);
        deptEmployees.stream()
                    .filter(Employee::isActive)
                    .forEach(e -> e.giveRaise(percentage));
        
        totalOperations += deptEmployees.size();
        lastModified = new Date();
        System.out.println("✓ Applied " + percentage + "% raise to " + department + " department");
    }
    
    // Display methods
    public void displayAllPeople() {
        if (people.isEmpty()) {
            System.out.println("No people in the system.");
            return;
        }
        
        System.out.println("\n=== ALL PEOPLE ===");
        people.stream()
              .sorted()
              .forEach(p -> System.out.println(p.getDisplayInfo()));
    }
    
    public void displayAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees in the system.");
            return;
        }
        
        System.out.println("\n=== ALL EMPLOYEES ===");
        employees.stream()
                .sorted((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()))
                .forEach(e -> System.out.println(e.getEmployeeDetails()));
    }
    
    public void displayDepartmentSummary() {
        if (departmentMap.isEmpty()) {
            System.out.println("No departments found.");
            return;
        }
        
        System.out.println("\n=== DEPARTMENT SUMMARY ===");
        departmentMap.forEach((dept, empList) -> {
            long activeCount = empList.stream().filter(Employee::isActive).count();
            double totalSalary = empList.stream()
                                       .filter(Employee::isActive)
                                       .mapToDouble(Employee::getTotalSalary)
                                       .sum();
            
            System.out.printf("%-20s | Employees: %d | Payroll: $%.2f%n", 
                            dept, activeCount, totalSalary);
        });
    }
    
    // Getters
    public List<Person> getAllPeople() {
        return new ArrayList<>(people);  // Return copy to prevent external modification
    }
    
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
    
    public Set<String> getAllDepartments() {
        return new HashSet<>(departmentMap.keySet());
    }
}