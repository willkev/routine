package core;

public class Executor {

    private static final String SHOW_LOG = "showLog";
    private static final String NOT_SHOW_LOG = "notShowLog";
    private static final String WRITE_DATA = "writeData";
    private static final String NOT_WRITE_DATA = "notWriteData";

    public static void main(String[] args) {
        boolean showLog = false;
        boolean writeData = false;
        if (args != null && args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case SHOW_LOG:
                        showLog = true;
                        break;
                    case NOT_SHOW_LOG:
                        showLog = false;
                        break;
                    case WRITE_DATA:
                        writeData = true;
                        break;
                    case NOT_WRITE_DATA:
                        writeData = false;
                        break;
                    default:
                        info();
                }
            }
            new GetActions(showLog, writeData);
        } else {
            info();
        }
    }

    private static void info() {
        System.out.println("[Parameters]");
        System.out.println(SHOW_LOG);
        System.out.println(NOT_SHOW_LOG);
        System.out.println(WRITE_DATA);
        System.out.println(NOT_WRITE_DATA);
        System.exit(1);
    }
}
