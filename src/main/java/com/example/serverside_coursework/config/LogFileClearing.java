package com.example.serverside_coursework.config;

import java.io.FileWriter;
import java.io.IOException;

public class LogFileClearing {
    public static void clearLogFile(String filePath) {
        try {
            // Open the file in overwrite mode, which effectively clears its contents
            new FileWriter(filePath, false).close();
            System.out.println("Log file cleared successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        clearLogFile("logs/ticket-system.log");
    }
}