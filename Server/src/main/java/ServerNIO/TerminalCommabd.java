package ServerNIO;

import java.util.Arrays;

public enum TerminalCommabd {
    DIR("dir"),
    CAT("cat"),
    CD("cd"),
    TYPE("type"),
    ECHO("echo"),
    RD("rd"),
    HELP("help");
 private final String command;
    TerminalCommabd(String command  ) {
        this.command=command;
    }
    public String getCommand() {
        return command;
    }

    public static TerminalCommabd byCommand(String command) {
        return Arrays.stream(values())
                .filter(cmd -> cmd.getCommand().equals(command))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Command not found"));
    }
}