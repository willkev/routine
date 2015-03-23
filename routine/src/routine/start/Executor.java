package routine.start;

import routine.core.GetActions;
import gui.TaskBar;

public class Executor {

    public static void main(String[] args) {
        TaskBar.createTaskBar();
        new GetActions(true, true);
    }
}
