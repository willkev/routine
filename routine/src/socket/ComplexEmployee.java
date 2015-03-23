package socket;

import java.io.Serializable;

class ComplexEmployee implements Serializable {

    private String name;

    private int salary;

    /**
     * Creates a new instance of ComplexEmployee
     */
    public ComplexEmployee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return this.salary;
    }
}
