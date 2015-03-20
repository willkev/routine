package routine.start;

import routine.core.GetActions;
import routine.core.TaskBar;

public class Executor {

    private static final String SHOW_LOG = "show--log";
    private static final String WRITE_DATA = "write--data";

    private static boolean showLog = false;
    private static boolean writeData = false;

    public static void main(String[] args) {
        info();
        TaskBar.createTaskBar();
        for (String arg : args) {
            switch (arg) {
                case SHOW_LOG:
                    showLog = true;
                    break;
                case WRITE_DATA:
                    writeData = true;
                    break;
            }
        }
        showLog = true;
        writeData = true;
        new GetActions(showLog, writeData);
    }

    private static void info() {
        System.out.println("[Parameters]");
        System.out.println(SHOW_LOG);
        System.out.println(WRITE_DATA);
    }
}
