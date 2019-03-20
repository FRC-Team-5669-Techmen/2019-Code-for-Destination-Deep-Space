package edu.boscotech.robot2019.util;

public class ModeTracker {
    private static ModeTracker s_instance = new ModeTracker();
    private Mode m_mode = Mode.kHatch;
    private int m_lastLiftHeight = 0;

    private ModeTracker() { }

    public Mode getMode() {
        return m_mode;
    }

    public void setMode(Mode mode) {
        m_mode = mode;
    }

    public int getLastLiftHeight() {
        return m_lastLiftHeight;
    }

    public void setLastLiftHeight(int height) {
        m_lastLiftHeight = height;
    }

    public static ModeTracker getInstance() {
        return s_instance;
    }
}