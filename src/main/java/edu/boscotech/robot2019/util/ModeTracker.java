package edu.boscotech.robot2019.util;

public class ModeTracker {
    private static ModeTracker s_instance = new ModeTracker();
    private Mode m_mode;

    private ModeTracker() { }

    public Mode getMode() {
        return m_mode;
    }

    public void setMode(Mode mode) {
        m_mode = mode;
    }

    public static ModeTracker getInstance() {
        return s_instance;
    }
}