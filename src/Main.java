import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Advanced Personal Management System - Main Application
 * Demonstrates: Interactive menus, Exception handling, Input validation,
 * Integration of all system components, User experience design
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static PersonalManagementSystem system;
    private static DataManager dataManager;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        try {
            initializeSystem();
            displayWelcomeMessage();
            runMainMenu();
        } catch (Exception e) {
            System.err.println("Critical system error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
    
    private static void initializeSystem() {
        System.out.println("🚀 Initializing Personal Management System...");
        dataManager = new DataManager();
        
        // Try to load existing data
        if (dataManager.dataFilesExist()) {
            System.out.println("📁 Found existing data files. Loading...");
            system = dataManager.loadSystemData();
        } else {
            System.out.println("📝 No existing data found. Creating new system...");
            system = new PersonalManagementSystem();
            loadSampleData();
        }
        
        System.out.println("✅ System initialized successfully!\n");
    }
    
    private static void loadSampleData() {
        try {
            System.out.println("🔄 Loading sample data for demonstration...");
            
            // Add sample people
            system.addPerson(new Person("Alice Johnson", LocalDate.of(1990, 5, 15), 
                                      "Female", "alice.johnson@email.com", "555-0101"));
            system.addPerson(new Person("Bob Smith", LocalDate.of(1985, 8, 22), 
                                      "Male", "bob.smith@email.com", "555-0102"));
            
            // Add sample employees
            system.addEmployee(new Employee("Precious Williams", LocalDate.of(2002, 3, 10), 
                                          "Male", "precious@company.com", "555-0103",
                                          "EMP001", "Engineering", "Software Developer", 75000, LocalDate.of(2023, 1, 15)));
            
            system.addEmployee(new Employee("Sarah Davis", LocalDate.of(1988, 12, 5), 
                                          "Female", "sarah.davis@company.com", "555-0104",
                                          "EMP002", "Marketing", "Marketing Manager", 65000, LocalDate.of(2022, 6, 1)));
            
            system.addEmployee(new Employee("Mike Chen", LocalDate.of(1992, 7, 18), 
                                          "Male", "mike.chen@company.com", "555-0105",
                                          "EMP003", "Engineering", "Senior Developer", 85000, LocalDate.of(2021, 3, 20)));
            
            // Set some bonuses
            Employee precious = system.findEmployeeById("EMP001");
            if (precious != null) precious.setBonusPercentage(5.0);
            
            Employee mike = system.findEmployeeById("EMP003");
            if (mike != null) mike.setBonusPercentage(8.0);
            
            System.out.println("✅ Sample data loaded successfully!");
            
        } catch (Exception e) {
            System.err.println("Warning: Error loading sample data: " + e.getMessage());
        }
    }
    
    private static void displayWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              🏢 PERSONAL MANAGEMENT SYSTEM 🏢               ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Advanced Java Prototype demonstrating:                     ║");
        System.out.println("║  • Object-Oriented Programming (OOP)                        ║");
        System.out.println("║  • Collections & Data Structures                            ║");
        System.out.println("║  • File I/O & Data Persistence                              ║");
        System.out.println("║  • Exception Handling                                       ║");
        System.out.println("║  • Lambda Expressions & Streams                             ║");
        System.out.println("║  • Design Patterns                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void runMainMenu() {
        boolean running = true;
        
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> personManagementMenu();
                    case 2 -> employeeManagementMenu();
                    case 3 -> searchAndFilterMenu();
                    case 4 -> reportsAndStatisticsMenu();
                    case 5 -> dataManagementMenu();
                    case 6 -> systemUtilitiesMenu();
                    case 0 -> {
                        if (confirmExit()) {
                            running = false;
                        }
                    }
                    default -> System.out.println("❌ Invalid choice. Please try again.");
                }
                
            } catch (Exception e) {
                System.err.println("❌ Error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    🏠 MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. 👥 Person Management");
        System.out.println("2. 💼 Employee Management");
        System.out.println("3. 🔍 Search & Filter");
        System.out.println("4. 📊 Reports & Statistics");
        System.out.println("5. 💾 Data Management");
        System.out.println("6. 🛠️  System Utilities");
        System.out.println("0. 🚪 Exit");
        System.out.println("=".repeat(60));
    }
    
    // Person Management Menu
    private static void personManagementMenu() {
        boolean back = false;
        
        while (!back) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              👥 PERSON MANAGEMENT");
                System.out.println("=".repeat(50));
                System.out.println("1. ➕ Add New Person");
                System.out.println("2. 📋 View All People");
                System.out.println("3. 🔍 Find Person by Name");
                System.out.println("4. 🗑️  Remove Person");
                System.out.println("5. 📊 Age Range Search");
                System.out.println("0. ⬅️  Back to Main Menu");
                System.out.println("=".repeat(50));
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> addNewPerson();
                    case 2 -> system.displayAllPeople();
                    case 3 -> findPersonByName();
                    case 4 -> removePerson();
                    case 5 -> searchPeopleByAge();
                    case 0 -> back = true;
                    default -> System.out.println("❌ Invalid choice.");
                }
                
                if (!back) pauseForUser();
                
            } catch (Exception e) {
                System.err.println("❌ Error in person management: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    private static void addNewPerson() {
        try {
            System.out.println("\n➕ Adding New Person");
            System.out.println("-".repeat(30));
            
            String name = getStringInput("Enter name: ");
            LocalDate birthDate = getDateInput("Enter birth date (yyyy-mm-dd): ");
            String gender = getStringInput("Enter gender: ");
            String email = getEmailInput("Enter email: ");
            String phone = getStringInput("Enter phone number: ");
            
            Person person = new Person(name, birthDate, gender, email, phone);
            system.addPerson(person);
            
        } catch (Exception e) {
            System.err.println("❌ Error adding person: " + e.getMessage());
        }
    }
    
    private static void findPersonByName() {
        try {
            String name = getStringInput("Enter name to search: ");
            Person person = system.findPersonByName(name);
            
            if (person != null) {
                System.out.println("✅ Person found:");
                System.out.println(person.getDisplayInfo());
            } else {
                System.out.println("❌ Person not found: " + name);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error searching for person: " + e.getMessage());
        }
    }
    
    private static void removePerson() {
        try {
            String name = getStringInput("Enter name of person to remove: ");
            
            if (system.removePerson(name)) {
                System.out.println("✅ Person removed successfully.");
            } else {
                System.out.println("❌ Person not found: " + name);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error removing person: " + e.getMessage());
        }
    }
    
    private static void searchPeopleByAge() {
        try {
            int minAge = getIntInput("Enter minimum age: ");
            int maxAge = getIntInput("Enter maximum age: ");
            
            List<Person> results = system.findPeopleByAge(minAge, maxAge);
            
            if (results.isEmpty()) {
                System.out.println("❌ No people found in age range " + minAge + "-" + maxAge);
            } else {
                System.out.println("✅ Found " + results.size() + " people in age range " + minAge + "-" + maxAge + ":");
                results.forEach(p -> System.out.println("  " + p.getDisplayInfo()));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error searching by age: " + e.getMessage());
        }
    }
    
    // Employee Management Menu
    private static void employeeManagementMenu() {
        boolean back = false;
        
        while (!back) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              💼 EMPLOYEE MANAGEMENT");
                System.out.println("=".repeat(50));
                System.out.println("1. ➕ Add New Employee");
                System.out.println("2. 📋 View All Employees");
                System.out.println("3. 🔍 Find Employee by ID");
                System.out.println("4. 💰 Give Raise");
                System.out.println("5. 🎯 Set Bonus");
                System.out.println("6. 🏢 View by Department");
                System.out.println("7. 🚀 Promote Employee");
                System.out.println("8. ❌ Terminate/Reactivate Employee");
                System.out.println("0. ⬅️  Back to Main Menu");
                System.out.println("=".repeat(50));
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> addNewEmployee();
                    case 2 -> system.displayAllEmployees();
                    case 3 -> findEmployeeById();
                    case 4 -> giveRaise();
                    case 5 -> setBonus();
                    case 6 -> viewByDepartment();
                    case 7 -> promoteEmployee();
                    case 8 -> toggleEmployeeStatus();
                    case 0 -> back = true;
                    default -> System.out.println("❌ Invalid choice.");
                }
                
                if (!back) pauseForUser();
                
            } catch (Exception e) {
                System.err.println("❌ Error in employee management: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    private static void addNewEmployee() {
        try {
            System.out.println("\n➕ Adding New Employee");
            System.out.println("-".repeat(30));
            
            String name = getStringInput("Enter name: ");
            LocalDate birthDate = getDateInput("Enter birth date (yyyy-mm-dd): ");
            String gender = getStringInput("Enter gender: ");
            String email = getEmailInput("Enter email: ");
            String phone = getStringInput("Enter phone number: ");
            String empId = getStringInput("Enter employee ID: ");
            String department = getStringInput("Enter department: ");
            String position = getStringInput("Enter position: ");
            double salary = getDoubleInput("Enter base salary: ");
            LocalDate hireDate = getDateInput("Enter hire date (yyyy-mm-dd): ");
            
            Employee employee = new Employee(name, birthDate, gender, email, phone, 
                                           empId, department, position, salary, hireDate);
            system.addEmployee(employee);
            
        } catch (Exception e) {
            System.err.println("❌ Error adding employee: " + e.getMessage());
        }
    }
    
    private static void findEmployeeById() {
        try {
            String empId = getStringInput("Enter employee ID: ");
            Employee employee = system.findEmployeeById(empId);
            
            if (employee != null) {
                System.out.println("✅ Employee found:");
                System.out.println(employee.getEmployeeDetails());
            } else {
                System.out.println("❌ Employee not found: " + empId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error searching for employee: " + e.getMessage());
        }
    }
    
    private static void giveRaise() {
        try {
            String empId = getStringInput("Enter employee ID: ");
            Employee employee = system.findEmployeeById(empId);
            
            if (employee != null) {
                System.out.println("Current salary: $" + employee.getTotalSalary());
                double percentage = getDoubleInput("Enter raise percentage: ");
                
                employee.giveRaise(percentage);
                System.out.println("✅ Raise applied! New salary: $" + employee.getTotalSalary());
            } else {
                System.out.println("❌ Employee not found: " + empId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error giving raise: " + e.getMessage());
        }
    }
    
    private static void setBonus() {
        try {
            String empId = getStringInput("Enter employee ID: ");
            Employee employee = system.findEmployeeById(empId);
            
            if (employee != null) {
                System.out.println("Current bonus: " + employee.getBonusPercentage() + "%");
                double bonus = getDoubleInput("Enter new bonus percentage (0-100): ");
                
                employee.setBonusPercentage(bonus);
                System.out.println("✅ Bonus set! New total salary: $" + employee.getTotalSalary());
            } else {
                System.out.println("❌ Employee not found: " + empId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error setting bonus: " + e.getMessage());
        }
    }
    
    private static void viewByDepartment() {
        try {
            Set<String> departments = system.getAllDepartments();
            
            if (departments.isEmpty()) {
                System.out.println("❌ No departments found.");
                return;
            }
            
            System.out.println("Available departments:");
            departments.forEach(dept -> System.out.println("  • " + dept));
            
            String department = getStringInput("Enter department name: ");
            List<Employee> employees = system.getEmployeesByDepartment(department);
            
            if (employees.isEmpty()) {
                System.out.println("❌ No employees found in department: " + department);
            } else {
                System.out.println("✅ Employees in " + department + ":");
                employees.forEach(emp -> System.out.println("  " + emp.getEmployeeDetails()));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error viewing department: " + e.getMessage());
        }
    }
    
    private static void promoteEmployee() {
        try {
            String empId = getStringInput("Enter employee ID: ");
            Employee employee = system.findEmployeeById(empId);
            
            if (employee != null) {
                System.out.println("Current position: " + employee.getPosition());
                String newPosition = getStringInput("Enter new position: ");
                double salaryIncrease = getDoubleInput("Enter salary increase: ");
                
                employee.promote(newPosition, salaryIncrease);
                System.out.println("✅ Employee promoted! New details:");
                System.out.println(employee.getEmployeeDetails());
            } else {
                System.out.println("❌ Employee not found: " + empId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error promoting employee: " + e.getMessage());
        }
    }
    
    private static void toggleEmployeeStatus() {
        try {
            String empId = getStringInput("Enter employee ID: ");
            Employee employee = system.findEmployeeById(empId);
            
            if (employee != null) {
                boolean currentStatus = employee.isActive();
                System.out.println("Current status: " + (currentStatus ? "Active" : "Inactive"));
                
                if (currentStatus) {
                    employee.terminate();
                    System.out.println("✅ Employee terminated.");
                } else {
                    employee.reactivate();
                    System.out.println("✅ Employee reactivated.");
                }
            } else {
                System.out.println("❌ Employee not found: " + empId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error changing employee status: " + e.getMessage());
        }
    }
    
    // Search and Filter Menu
    private static void searchAndFilterMenu() {
        boolean back = false;
        
        while (!back) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              🔍 SEARCH & FILTER");
                System.out.println("=".repeat(50));
                System.out.println("1. 🔎 Search Employees");
                System.out.println("2. 💰 Filter by Salary Range");
                System.out.println("3. 🏆 Top Earners");
                System.out.println("4. 📈 Sort by Salary");
                System.out.println("5. 🎯 Sort by Experience");
                System.out.println("6. 📝 Sort by Name");
                System.out.println("0. ⬅️  Back to Main Menu");
                System.out.println("=".repeat(50));
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> searchEmployees();
                    case 2 -> filterBySalary();
                    case 3 -> showTopEarners();
                    case 4 -> showEmployeesSortedBySalary();
                    case 5 -> showEmployeesSortedByExperience();
                    case 6 -> showPeopleSortedByName();
                    case 0 -> back = true;
                    default -> System.out.println("❌ Invalid choice.");
                }
                
                if (!back) pauseForUser();
                
            } catch (Exception e) {
                System.err.println("❌ Error in search and filter: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    private static void searchEmployees() {
        try {
            String searchTerm = getStringInput("Enter search term (name, department, position, or ID): ");
            List<Employee> results = system.searchEmployees(searchTerm);
            
            if (results.isEmpty()) {
                System.out.println("❌ No employees found matching: " + searchTerm);
            } else {
                System.out.println("✅ Found " + results.size() + " employees matching '" + searchTerm + "':");
                results.forEach(emp -> System.out.println("  " + emp.getEmployeeDetails()));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error searching employees: " + e.getMessage());
        }
    }
    
    private static void filterBySalary() {
        try {
            double minSalary = getDoubleInput("Enter minimum salary: ");
            double maxSalary = getDoubleInput("Enter maximum salary: ");
            
            List<Employee> results = system.getEmployeesBySalaryRange(minSalary, maxSalary);
            
            if (results.isEmpty()) {
                System.out.println("❌ No employees found in salary range $" + minSalary + " - $" + maxSalary);
            } else {
                System.out.println("✅ Found " + results.size() + " employees in salary range:");
                results.forEach(emp -> System.out.println("  " + emp.getEmployeeDetails()));
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error filtering by salary: " + e.getMessage());
        }
    }
    
    private static void showTopEarners() {
        try {
            int count = getIntInput("Enter number of top earners to show: ");
            List<Employee> topEarners = system.getTopEarners(count);
            
            if (topEarners.isEmpty()) {
                System.out.println("❌ No active employees found.");
            } else {
                System.out.println("✅ Top " + topEarners.size() + " earners:");
                for (int i = 0; i < topEarners.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + topEarners.get(i).getEmployeeDetails());
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error showing top earners: " + e.getMessage());
        }
    }
    
    private static void showEmployeesSortedBySalary() {
        List<Employee> sorted = system.getEmployeesSortedBySalary();
        
        if (sorted.isEmpty()) {
            System.out.println("❌ No employees found.");
        } else {
            System.out.println("✅ Employees sorted by salary (highest to lowest):");
            sorted.forEach(emp -> System.out.println("  " + emp.getEmployeeDetails()));
        }
    }
    
    private static void showEmployeesSortedByExperience() {
        List<Employee> sorted = system.getEmployeesSortedByExperience();
        
        if (sorted.isEmpty()) {
            System.out.println("❌ No employees found.");
        } else {
            System.out.println("✅ Employees sorted by experience (most to least):");
            sorted.forEach(emp -> System.out.println("  " + emp.getEmployeeDetails()));
        }
    }
    
    private static void showPeopleSortedByName() {
        List<Person> sorted = system.getPeopleSortedByName();
        
        if (sorted.isEmpty()) {
            System.out.println("❌ No people found.");
        } else {
            System.out.println("✅ People sorted by name:");
            sorted.forEach(person -> System.out.println("  " + person.getDisplayInfo()));
        }
    }
    
    // Reports and Statistics Menu
    private static void reportsAndStatisticsMenu() {
        boolean back = false;
        
        while (!back) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              📊 REPORTS & STATISTICS");
                System.out.println("=".repeat(50));
                System.out.println("1. 📈 System Statistics");
                System.out.println("2. 🏢 Department Summary");
                System.out.println("3. 💰 Department Payroll");
                System.out.println("4. 👥 Active vs Inactive");
                System.out.println("5. 📊 Detailed Report");
                System.out.println("0. ⬅️  Back to Main Menu");
                System.out.println("=".repeat(50));
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> showSystemStatistics();
                    case 2 -> system.displayDepartmentSummary();
                    case 3 -> showDepartmentPayroll();
                    case 4 -> showActiveInactiveStats();
                    case 5 -> showDetailedReport();
                    case 0 -> back = true;
                    default -> System.out.println("❌ Invalid choice.");
                }
                
                if (!back) pauseForUser();
                
            } catch (Exception e) {
                System.err.println("❌ Error in reports: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    private static void showSystemStatistics() {
        Map<String, Object> stats = system.getSystemStatistics();
        
        System.out.println("\n📈 SYSTEM STATISTICS");
        System.out.println("=".repeat(40));
        stats.forEach((key, value) -> 
            System.out.printf("%-20s: %s%n", key, value));
    }
    
    private static void showDepartmentPayroll() {
        Map<String, Double> payroll = system.getDepartmentPayroll();
        
        if (payroll.isEmpty()) {
            System.out.println("❌ No department data found.");
        } else {
            System.out.println("\n💰 DEPARTMENT PAYROLL");
            System.out.println("=".repeat(40));
            payroll.forEach((dept, total) -> 
                System.out.printf("%-20s: $%.2f%n", dept, total));
        }
    }
    
    private static void showActiveInactiveStats() {
        List<Employee> active = system.getActiveEmployees();
        List<Employee> inactive = system.getInactiveEmployees();
        
        System.out.println("\n👥 EMPLOYEE STATUS BREAKDOWN");
        System.out.println("=".repeat(40));
        System.out.println("Active Employees: " + active.size());
        System.out.println("Inactive Employees: " + inactive.size());
        System.out.println("Total Employees: " + (active.size() + inactive.size()));
        
        if (!inactive.isEmpty()) {
            System.out.println("\nInactive Employees:");
            inactive.forEach(emp -> System.out.println("  • " + emp.getName() + " (" + emp.getEmployeeId() + ")"));
        }
    }
    
    private static void showDetailedReport() {
        System.out.println("\n📊 DETAILED SYSTEM REPORT");
        System.out.println("=".repeat(60));
        
        showSystemStatistics();
        System.out.println();
        system.displayDepartmentSummary();
        System.out.println();
        showActiveInactiveStats();
    }
    
    // Data Management Menu
    private static void dataManagementMenu() {
        boolean back = false;
        
        while (!back) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              💾 DATA MANAGEMENT");
                System.out.println("=".repeat(50));
                System.out.println("1. 💾 Save Data");
                System.out.println("2. 📁 Load Data");
                System.out.println("3. 🔄 Create Backup");
                System.out.println("4. 📤 Export to JSON");
                System.out.println("5. 📋 File Information");
                System.out.println("0. ⬅️  Back to Main Menu");
                System.out.println("=".repeat(50));
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> saveData();
                    case 2 -> loadData();
                    case 3 -> createBackup();
                    case 4 -> exportToJson();
                    case 5 -> dataManager.printFileInfo();
                    case 0 -> back = true;
                    default -> System.out.println("❌ Invalid choice.");
                }
                
                if (!back) pauseForUser();
                
            } catch (Exception e) {
                System.err.println("❌ Error in data management: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    private static void saveData() {
        System.out.println("💾 Saving system data...");
        if (dataManager.saveSystemData(system)) {
            System.out.println("✅ Data saved successfully!");
        } else {
            System.out.println("❌ Failed to save data.");
        }
    }
    
    private static void loadData() {
        System.out.println("📁 Loading system data...");
        if (confirmAction("This will replace current data. Continue?")) {
            system = dataManager.loadSystemData();
            System.out.println("✅ Data loaded successfully!");
        }
    }
    
    private static void createBackup() {
        System.out.println("🔄 Creating backup...");
        if (dataManager.createBackup()) {
            System.out.println("✅ Backup created successfully!");
        } else {
            System.out.println("❌ Failed to create backup.");
        }
    }
    
    private static void exportToJson() {
        try {
            String filename = getStringInput("Enter filename (without extension): ") + ".json";
            System.out.println("📤 Exporting to JSON...");
            
            if (dataManager.exportToJson(system, filename)) {
                System.out.println("✅ Export completed successfully!");
            } else {
                System.out.println("❌ Export failed.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error exporting: " + e.getMessage());
        }
    }
    
    // System Utilities Menu
    private static void systemUtilitiesMenu() {
        boolean back = false;
        
        while (!back) {
            try {
                System.out.println("\n" + "=".repeat(50));
                System.out.println("              🛠️ SYSTEM UTILITIES");
                System.out.println("=".repeat(50));
                System.out.println("1. 💰 Bulk Raise (All Employees)");
                System.out.println("2. 🏢 Department Raise");
                System.out.println("3. 🧹 Clear All Data");
                System.out.println("4. 🔄 Reset Sample Data");
                System.out.println("5. ℹ️  System Information");
                System.out.println("0. ⬅️  Back to Main Menu");
                System.out.println("=".repeat(50));
                
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1 -> bulkRaise();
                    case 2 -> departmentRaise();
                    case 3 -> clearAllData();
                    case 4 -> resetSampleData();
                    case 5 -> showSystemInfo();
                    case 0 -> back = true;
                    default -> System.out.println("❌ Invalid choice.");
                }
                
                if (!back) pauseForUser();
                
            } catch (Exception e) {
                System.err.println("❌ Error in system utilities: " + e.getMessage());
                pauseForUser();
            }
        }
    }
    
    private static void bulkRaise() {
        try {
            double percentage = getDoubleInput("Enter raise percentage for all employees: ");
            
            if (confirmAction("Apply " + percentage + "% raise to ALL active employees?")) {
                system.giveRaiseToAll(percentage);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error applying bulk raise: " + e.getMessage());
        }
    }
    
    private static void departmentRaise() {
        try {
            Set<String> departments = system.getAllDepartments();
            
            if (departments.isEmpty()) {
                System.out.println("❌ No departments found.");
                return;
            }
            
            System.out.println("Available departments:");
            departments.forEach(dept -> System.out.println("  • " + dept));
            
            String department = getStringInput("Enter department name: ");
            double percentage = getDoubleInput("Enter raise percentage: ");
            
            if (confirmAction("Apply " + percentage + "% raise to " + department + " department?")) {
                system.giveRaiseToDepartment(department, percentage);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error applying department raise: " + e.getMessage());
        }
    }
    
    private static void clearAllData() {
        if (confirmAction("⚠️  WARNING: This will delete ALL data permanently. Continue?")) {
            system = new PersonalManagementSystem();
            System.out.println("✅ All data cleared.");
        }
    }
    
    private static void resetSampleData() {
        if (confirmAction("This will clear current data and load sample data. Continue?")) {
            system = new PersonalManagementSystem();
            loadSampleData();
            System.out.println("✅ Sample data loaded.");
        }
    }
    
    private static void showSystemInfo() {
        System.out.println("\n🛠️ SYSTEM INFORMATION");
        System.out.println("=".repeat(40));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("OS: " + System.getProperty("os.name"));
        System.out.println("User: " + System.getProperty("user.name"));
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Memory (Free/Total): " + 
                         Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB / " +
                         Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB");
    }
    
    // Input utility methods
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty");
        }
        return input;
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
    
    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Please enter date in format yyyy-mm-dd (e.g., 2002-03-10).");
            }
        }
    }
    
    private static String getEmailInput(String prompt) {
        while (true) {
            try {
                String email = getStringInput(prompt);
                if (email.contains("@") && email.contains(".")) {
                    return email;
                } else {
                    System.out.println("❌ Please enter a valid email address.");
                }
            } catch (Exception e) {
                System.out.println("❌ Please enter a valid email address.");
            }
        }
    }
    
    private static boolean confirmAction(String message) {
        System.out.print(message + " (y/N): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
    
    private static boolean confirmExit() {
        System.out.print("💾 Save data before exiting? (y/N): ");
        String response = scanner.nextLine().trim().toLowerCase();
        
        if (response.equals("y") || response.equals("yes")) {
            dataManager.saveSystemData(system);
        }
        
        System.out.println("👋 Thank you for using Personal Management System!");
        return true;
    }
    
    private static void pauseForUser() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private static void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }
}