package com.ztiany.basic.tools;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LoggerSample {

    public static void main(String... args) {
        Logger.getGlobal().log(Level.INFO, "LoggerSample");
    }

}
