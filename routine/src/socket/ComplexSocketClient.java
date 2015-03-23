package socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ComplexSocketClient {

    public static final int SERVER_PORT = 1777;
    
    public static void main(String args[]) throws Exception {
        Socket socket1;
        String str = "";

        socket1 = new Socket("192.168.1.115", SERVER_PORT);
        ObjectInputStream ois = new ObjectInputStream(socket1.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket1.getOutputStream());

        oos.writeObject(createObject());

        try {
            while ((str = (String) ois.readObject()) != null) {
                System.out.println(str);
                oos.writeObject("bye");
                if (str.equals("bye")) {
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        ois.close();
        oos.close();
        socket1.close();
    }

    private static Object createObject() {
        ComplexCompany comp = new ComplexCompany("Silvio Santos para presidente");
        ComplexEmployee emp0 = new ComplexEmployee("Bigail", 1000);
        comp.addPresident(emp0);

        ComplexDepartment sales = new ComplexDepartment("Castro");
        ComplexEmployee emp1 = new ComplexEmployee("Drone", 1200);
        sales.addManager(emp1);
        comp.addDepartment(sales);

        ComplexDepartment accounting = new ComplexDepartment("Ermital");
        ComplexEmployee emp2 = new ComplexEmployee("Faraoh", 1230);
        accounting.addManager(emp2);
        comp.addDepartment(accounting);

        ComplexDepartment maintenance = new ComplexDepartment("Maintenance");
        ComplexEmployee emp3 = new ComplexEmployee("Greg Hladlick", 1020);
        maintenance.addManager(emp3);
        comp.addDepartment(maintenance);
        return comp;
    }

}
