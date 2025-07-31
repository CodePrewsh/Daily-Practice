import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Advanced Person class demonstrating OOP principles
 * Features: Encapsulation, validation, proper object methods
 */
public class Person implements Comparable<Person> {
    private String name;
    private LocalDate birthDate;
    private String gender;
    private String email;
    private String phoneNumber;
    
    // Constructor with validation
    public Person(String name, LocalDate birthDate, String gender, String email, String phoneNumber) {
        setName(name);
        setBirthDate(birthDate);
        setGender(gender);
        setEmail(email);
        setPhoneNumber(phoneNumber);
    }
    
    // Getters and setters with validation
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
        this.birthDate = birthDate;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException("Gender cannot be null or empty");
        }
        this.gender = gender.trim();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.trim().toLowerCase();
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        this.phoneNumber = phoneNumber.trim();
    }
    
    // Calculated property
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    // Override equals and hashCode for proper object comparison
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return Objects.equals(name, person.name) &&
               Objects.equals(birthDate, person.birthDate) &&
               Objects.equals(email, person.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, birthDate, email);
    }
    
    // Implement Comparable for sorting
    @Override
    public int compareTo(Person other) {
        int nameComparison = this.name.compareToIgnoreCase(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.birthDate.compareTo(other.birthDate);
    }
    
    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, gender='%s', email='%s', phone='%s'}", 
                           name, getAge(), gender, email, phoneNumber);
    }
    
    // Method to get formatted display string
    public String getDisplayInfo() {
        return String.format("%-20s | Age: %-3d | %s | %s", 
                           name, getAge(), email, phoneNumber);
    }
}