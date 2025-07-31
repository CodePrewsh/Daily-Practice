import java.time.LocalDate;
import java.util.Objects;

/**
 * Employee class extending Person - demonstrates inheritance
 * Additional features: salary management, employment details
 */
public class Employee extends Person {
    private String employeeId;
    private String department;
    private String position;
    private double baseSalary;
    private double bonusPercentage;
    private LocalDate hireDate;
    private boolean isActive;
    
    public Employee(String name, LocalDate birthDate, String gender, String email, 
                   String phoneNumber, String employeeId, String department, 
                   String position, double baseSalary, LocalDate hireDate) {
        super(name, birthDate, gender, email, phoneNumber);
        setEmployeeId(employeeId);
        setDepartment(department);
        setPosition(position);
        setBaseSalary(baseSalary);
        setHireDate(hireDate);
        this.bonusPercentage = 0.0;
        this.isActive = true;
    }
    
    // Getters and setters with validation
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        this.employeeId = employeeId.trim().toUpperCase();
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be null or empty");
        }
        this.department = department.trim();
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be null or empty");
        }
        this.position = position.trim();
    }
    
    public double getBaseSalary() {
        return baseSalary;
    }
    
    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0) {
            throw new IllegalArgumentException("Base salary cannot be negative");
        }
        this.baseSalary = baseSalary;
    }
    
    public double getBonusPercentage() {
        return bonusPercentage;
    }
    
    public void setBonusPercentage(double bonusPercentage) {
        if (bonusPercentage < 0 || bonusPercentage > 100) {
            throw new IllegalArgumentException("Bonus percentage must be between 0 and 100");
        }
        this.bonusPercentage = bonusPercentage;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new IllegalArgumentException("Hire date cannot be null");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Hire date cannot be in the future");
        }
        this.hireDate = hireDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    // Calculated properties
    public double getTotalSalary() {
        return baseSalary + (baseSalary * bonusPercentage / 100);
    }
    
    public int getYearsOfService() {
        return java.time.Period.between(hireDate, LocalDate.now()).getYears();
    }
    
    public double getAnnualBonus() {
        return baseSalary * bonusPercentage / 100;
    }
    
    // Business methods
    public void giveRaise(double percentage) {
        if (percentage < 0) {
            throw new IllegalArgumentException("Raise percentage cannot be negative");
        }
        this.baseSalary += (this.baseSalary * percentage / 100);
    }
    
    public void promote(String newPosition, double salaryIncrease) {
        setPosition(newPosition);
        if (salaryIncrease > 0) {
            this.baseSalary += salaryIncrease;
        }
    }
    
    public void terminate() {
        this.isActive = false;
    }
    
    public void reactivate() {
        this.isActive = true;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Employee employee = (Employee) obj;
        return Objects.equals(employeeId, employee.employeeId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeId);
    }
    
    @Override
    public String toString() {
        return String.format("Employee{id='%s', name='%s', department='%s', position='%s', salary=%.2f, active=%s}", 
                           employeeId, getName(), department, position, getTotalSalary(), isActive);
    }
    
    public String getEmployeeDetails() {
        return String.format("ID: %s | %s | %s - %s | Salary: $%.2f | Years: %d | Status: %s", 
                           employeeId, getName(), department, position, 
                           getTotalSalary(), getYearsOfService(), 
                           isActive ? "Active" : "Inactive");
    }
}