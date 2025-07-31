import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Validation Utilities - Demonstrates Utility Pattern and Input Validation
 * Features: Static utility methods, Regular expressions, Design patterns
 */
public final class ValidationUtils {
    
    // Private constructor to prevent instantiation (Utility Pattern)
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // Email validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // Phone number patterns (flexible format)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[\\+]?[1-9]?[0-9]{7,15}$"
    );
    
    // Employee ID pattern (alphanumeric, 3-10 characters)
    private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile(
        "^[A-Z0-9]{3,10}$"
    );
    
    // Name validation (letters, spaces, hyphens, apostrophes)
    private static final Pattern NAME_PATTERN = Pattern.compile(
        "^[a-zA-Z\\s\\-']{2,50}$"
    );
    
    /**
     * Validates if a string is not null and not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates phone number format
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (!isNotEmpty(phone)) return false;
        
        // Remove common phone number formatting
        String cleanPhone = phone.replaceAll("[\\s\\-\\(\\)\\.]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    /**
     * Validates employee ID format
     */
    public static boolean isValidEmployeeId(String employeeId) {
        return isNotEmpty(employeeId) && 
               EMPLOYEE_ID_PATTERN.matcher(employeeId.trim().toUpperCase()).matches();
    }
    
    /**
     * Validates person name
     */
    public static boolean isValidName(String name) {
        return isNotEmpty(name) && NAME_PATTERN.matcher(name.trim()).matches();
    }
    
    /**
     * Validates birth date (not in future, reasonable age limits)
     */
    public static boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate == null) return false;
        
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(150); // Maximum reasonable age
        
        return !birthDate.isAfter(now) && !birthDate.isBefore(minDate);
    }
    
    /**
     * Validates salary (positive, reasonable range)
     */
    public static boolean isValidSalary(double salary) {
        return salary >= 0 && salary <= 10_000_000; // Reasonable upper limit
    }
    
    /**
     * Validates bonus percentage (0-100)
     */
    public static boolean isValidBonusPercentage(double percentage) {
        return percentage >= 0 && percentage <= 100;
    }
    
    /**
     * Validates age range
     */
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 150;
    }
    
    /**
     * Validates department name
     */
    public static boolean isValidDepartment(String department) {
        return isNotEmpty(department) && department.trim().length() <= 50;
    }
    
    /**
     * Validates position title
     */
    public static boolean isValidPosition(String position) {
        return isNotEmpty(position) && position.trim().length() <= 100;
    }
    
    /**
     * Validates hire date (not in future, not too far in past)
     */
    public static boolean isValidHireDate(LocalDate hireDate) {
        if (hireDate == null) return false;
        
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(70); // Reasonable employment history
        
        return !hireDate.isAfter(now) && !hireDate.isBefore(minDate);
    }
    
    /**
     * Comprehensive person validation
     */
    public static ValidationResult validatePerson(String name, LocalDate birthDate, 
                                                String gender, String email, String phone) {
        ValidationResult result = new ValidationResult();
        
        if (!isValidName(name)) {
            result.addError("Invalid name format. Use only letters, spaces, hyphens, and apostrophes (2-50 characters).");
        }
        
        if (!isValidBirthDate(birthDate)) {
            result.addError("Invalid birth date. Must be a valid date not in the future and within reasonable age limits.");
        }
        
        if (!isNotEmpty(gender)) {
            result.addError("Gender cannot be empty.");
        }
        
        if (!isValidEmail(email)) {
            result.addError("Invalid email format.");
        }
        
        if (!isValidPhoneNumber(phone)) {
            result.addError("Invalid phone number format.");
        }
        
        return result;
    }
    
    /**
     * Comprehensive employee validation
     */
    public static ValidationResult validateEmployee(String name, LocalDate birthDate, 
                                                  String gender, String email, String phone,
                                                  String employeeId, String department, 
                                                  String position, double salary, LocalDate hireDate) {
        // First validate as person
        ValidationResult result = validatePerson(name, birthDate, gender, email, phone);
        
        // Additional employee validations
        if (!isValidEmployeeId(employeeId)) {
            result.addError("Invalid employee ID format. Use 3-10 alphanumeric characters.");
        }
        
        if (!isValidDepartment(department)) {
            result.addError("Invalid department name.");
        }
        
        if (!isValidPosition(position)) {
            result.addError("Invalid position title.");
        }
        
        if (!isValidSalary(salary)) {
            result.addError("Invalid salary amount.");
        }
        
        if (!isValidHireDate(hireDate)) {
            result.addError("Invalid hire date.");
        }
        
        return result;
    }
    
    /**
     * Sanitizes string input (trims whitespace, handles null)
     */
    public static String sanitizeString(String input) {
        return input != null ? input.trim() : "";
    }
    
    /**
     * Formats phone number for display
     */
    public static String formatPhoneNumber(String phone) {
        if (!isValidPhoneNumber(phone)) return phone;
        
        String clean = phone.replaceAll("[^0-9]", "");
        
        if (clean.length() == 10) {
            return String.format("(%s) %s-%s", 
                                clean.substring(0, 3),
                                clean.substring(3, 6),
                                clean.substring(6));
        }
        
        return phone; // Return original if can't format
    }
    
    /**
     * Validates search term (not empty, reasonable length)
     */
    public static boolean isValidSearchTerm(String searchTerm) {
        return isNotEmpty(searchTerm) && searchTerm.trim().length() <= 100;
    }
    
    /**
     * Validates numeric range
     */
    public static boolean isValidRange(double min, double max) {
        return min >= 0 && max >= min && max <= Double.MAX_VALUE;
    }
    
    /**
     * Validation Result class to hold multiple validation errors
     */
    public static class ValidationResult {
        private final java.util.List<String> errors;
        
        public ValidationResult() {
            this.errors = new java.util.ArrayList<>();
        }
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public java.util.List<String> getErrors() {
            return new java.util.ArrayList<>(errors); // Return copy
        }
        
        public String getErrorMessage() {
            if (isValid()) return "";
            return String.join("; ", errors);
        }
        
        @Override
        public String toString() {
            return isValid() ? "Valid" : "Errors: " + getErrorMessage();
        }
    }
}