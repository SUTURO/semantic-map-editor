package com.malte3d.suturo.commons.exception;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Logs uncaught exceptions.
 */
@Slf4j
@UtilityClass
public class UncaughtExceptionLogger {

    /**
     * Installs the uncaught exception logger.
     *
     * <p>
     * Any uncaught exceptions will be logged.
     * </p>
     */
    public static void install() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> log.error("Uncaught exception in thread {}", thread.getName(), throwable));
    }
}
