package x.socket;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTextArea;

class ComplexCompany implements Serializable {

    private String name;

    private ComplexEmployee president;

    private Vector departments;

    public ComplexCompany(String name) {
        this.name = name;
        departments = new Vector();
    }

    public String getName() {
        return this.name;
    }

    public void addDepartment(ComplexDepartment dept) {
        departments.addElement(dept);
    }

    public ComplexEmployee getPresident() {
        return this.president;
    }

    public void addPresident(ComplexEmployee e) {
        this.president = e;
    }

    public Iterator getDepartmentIterator() {
        return departments.iterator();
    }

    @Override
    public String toString() {
        String print = "\n" + "The company name is " + getName();
        print += "\n" + "The company president is " + getPresident().getName() + "\n";
        Iterator i = getDepartmentIterator();
        while (i.hasNext()) {
            ComplexDepartment d = (ComplexDepartment) i.next();
            print += "\n" + "   The department name is " + d.getName();
            print += "\n" + "   The department manager is " + d.getManager().getName();
        }
        return print;
    }
}
