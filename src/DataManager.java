import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Data Manager for file I/O operations
 * Demonstrates: File I/O, Exception handling, Data persistence, CSV parsing
 */
public class DataManager {
    private static final String DATA_DIRECTORY = "data";
    private static final String PEOPLE_FILE = DATA_DIRECTORY + "/people.csv";
    private static final String EMPLOYEES_FILE = DATA_DIRECTORY + "/employees.csv";
    private static final String BACKUP_DIRECTORY = DATA_DIRECTORY + "/backups";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public DataManager() {
        createDirectories();
    }
    
    private void createDirectories() {
        try {
            File dataDir = new File(DATA_DIRECTORY);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
                System.out.println("✓ Created data directory");
            }
            
            File backupDir = new File(BACKUP_DIRECTORY);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
                System.out.println("✓ Created backup directory");
            }
        } catch (Exception e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }
    
    // Save data to files
    public boolean saveSystemData(PersonalManagementSystem system) {
        try {
            // Create backup first
            createBackup();
            
            // Save people and employees
            savePeople(system.getAllPeople());
            saveEmployees(system.getAllEmployees());
            
            System.out.println("✓ System data saved successfully");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error saving system data: " + e.getMessage());
            return false;
        }
    }
    
    public boolean savePeople(List<Person> people) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PEOPLE_FILE))) {
            // Write CSV header
            writer.println("Name,BirthDate,Gender,Email,PhoneNumber,Type");
            
            for (Person person : people) {
                if (!(person instanceof Employee)) { // Don't duplicate employees
                    writer.printf("%s,%s,%s,%s,%s,Person%n",
                        escapeCsv(person.getName()),
                        person.getBirthDate().format(DATE_FORMATTER),
                        escapeCsv(person.getGender()),
                        escapeCsv(person.getEmail()),
                        escapeCsv(person.getPhoneNumber())
                    );
                }
            }
            
            System.out.println("✓ Saved " + people.stream().filter(p -> !(p instanceof Employee)).count() + " people");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving people: " + e.getMessage());
            return false;
        }
    }
    
    public boolean saveEmployees(List<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
            // Write CSV header
            writer.println("Name,BirthDate,Gender,Email,PhoneNumber,EmployeeId,Department,Position,BaseSalary,BonusPercentage,HireDate,IsActive");
            
            for (Employee employee : employees) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%.2f,%s,%s%n",
                    escapeCsv(employee.getName()),
                    employee.getBirthDate().format(DATE_FORMATTER),
                    escapeCsv(employee.getGender()),
                    escapeCsv(employee.getEmail()),
                    escapeCsv(employee.getPhoneNumber()),
                    escapeCsv(employee.getEmployeeId()),
                    escapeCsv(employee.getDepartment()),
                    escapeCsv(employee.getPosition()),
                    employee.getBaseSalary(),
                    employee.getBonusPercentage(),
                    employee.getHireDate().format(DATE_FORMATTER),
                    employee.isActive()
                );
            }
            
            System.out.println("✓ Saved " + employees.size() + " employees");
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving employees: " + e.getMessage());
            return false;
        }
    }
    
    // Load data from files
    public PersonalManagementSystem loadSystemData() {
        PersonalManagementSystem system = new PersonalManagementSystem();
        
        try {
            // Load people first
            List<Person> people = loadPeople();
            for (Person person : people) {
                try {
                    system.addPerson(person);
                } catch (Exception e) {
                    System.err.println("Warning: Could not add person " + person.getName() + ": " + e.getMessage());
                }
            }
            
            // Load employees
            List<Employee> employees = loadEmployees();
            for (Employee employee : employees) {
                try {
                    system.addEmployee(employee);
                } catch (Exception e) {
                    System.err.println("Warning: Could not add employee " + employee.getName() + ": " + e.getMessage());
                }
            }
            
            System.out.println("✓ System data loaded successfully");
            
        } catch (Exception e) {
            System.err.println("Error loading system data: " + e.getMessage());
        }
        
        return system;
    }
    
    public List<Person> loadPeople() {
        List<Person> people = new ArrayList<>();
        File file = new File(PEOPLE_FILE);
        
        if (!file.exists()) {
            System.out.println("No people data file found, starting with empty list");
            return people;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = parseCsvLine(line);
                    if (parts.length >= 5) {
                        Person person = new Person(
                            parts[0], // name
                            LocalDate.parse(parts[1], DATE_FORMATTER), // birthDate
                            parts[2], // gender
                            parts[3], // email
                            parts[4]  // phoneNumber
                        );
                        people.add(person);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + " in people file: " + e.getMessage());
                }
            }
            
            System.out.println("✓ Loaded " + people.size() + " people");
            
        } catch (IOException e) {
            System.err.println("Error reading people file: " + e.getMessage());
        }
        
        return people;
    }
    
    public List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();
        File file = new File(EMPLOYEES_FILE);
        
        if (!file.exists()) {
            System.out.println("No employee data file found, starting with empty list");
            return employees;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = parseCsvLine(line);
                    if (parts.length >= 12) {
                        Employee employee = new Employee(
                            parts[0], // name
                            LocalDate.parse(parts[1], DATE_FORMATTER), // birthDate
                            parts[2], // gender
                            parts[3], // email
                            parts[4], // phoneNumber
                            parts[5], // employeeId
                            parts[6], // department
                            parts[7], // position
                            Double.parseDouble(parts[8]), // baseSalary
                            LocalDate.parse(parts[10], DATE_FORMATTER) // hireDate
                        );
                        
                        employee.setBonusPercentage(Double.parseDouble(parts[9]));
                        employee.setActive(Boolean.parseBoolean(parts[11]));
                        
                        employees.add(employee);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + " in employees file: " + e.getMessage());
                }
            }
            
            System.out.println("✓ Loaded " + employees.size() + " employees");
            
        } catch (IOException e) {
            System.err.println("Error reading employees file: " + e.getMessage());
        }
        
        return employees;
    }
    
    // Backup functionality
    public boolean createBackup() {
        try {
            String timestamp = new Date().toString().replaceAll("[:\\s]", "_");
            String backupDir = BACKUP_DIRECTORY + "/" + timestamp;
            
            File backupDirFile = new File(backupDir);
            backupDirFile.mkdirs();
            
            // Copy existing files to backup
            copyFile(PEOPLE_FILE, backupDir + "/people.csv");
            copyFile(EMPLOYEES_FILE, backupDir + "/employees.csv");
            
            System.out.println("✓ Backup created: " + backupDir);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error creating backup: " + e.getMessage());
            return false;
        }
    }
    
    private void copyFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        if (!sourceFile.exists()) return;
        
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
    
    // Export functionality
    public boolean exportToJson(PersonalManagementSystem system, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("{");
            writer.println("  \"system_info\": {");
            writer.println("    \"export_date\": \"" + new Date() + "\",");
            writer.println("    \"total_people\": " + system.getAllPeople().size() + ",");
            writer.println("    \"total_employees\": " + system.getAllEmployees().size());
            writer.println("  },");
            
            writer.println("  \"statistics\": {");
            Map<String, Object> stats = system.getSystemStatistics();
            boolean first = true;
            for (Map.Entry<String, Object> entry : stats.entrySet()) {
                if (!first) writer.println(",");
                writer.printf("    \"%s\": \"%s\"", entry.getKey(), entry.getValue());
                first = false;
            }
            writer.println();
            writer.println("  }");
            writer.println("}");
            
            System.out.println("✓ Exported system data to JSON: " + filename);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error exporting to JSON: " + e.getMessage());
            return false;
        }
    }
    
    // Utility methods
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
    
    public boolean dataFilesExist() {
        return new File(PEOPLE_FILE).exists() || new File(EMPLOYEES_FILE).exists();
    }
    
    public void printFileInfo() {
        System.out.println("\n=== DATA FILES INFO ===");
        
        File peopleFile = new File(PEOPLE_FILE);
        if (peopleFile.exists()) {
            System.out.printf("People file: %s (%.2f KB)%n", 
                            PEOPLE_FILE, peopleFile.length() / 1024.0);
        } else {
            System.out.println("People file: Not found");
        }
        
        File employeesFile = new File(EMPLOYEES_FILE);
        if (employeesFile.exists()) {
            System.out.printf("Employees file: %s (%.2f KB)%n", 
                            EMPLOYEES_FILE, employeesFile.length() / 1024.0);
        } else {
            System.out.println("Employees file: Not found");
        }
        
        File backupDir = new File(BACKUP_DIRECTORY);
        if (backupDir.exists()) {
            File[] backups = backupDir.listFiles();
            System.out.println("Backups: " + (backups != null ? backups.length : 0) + " available");
        }
    }
}