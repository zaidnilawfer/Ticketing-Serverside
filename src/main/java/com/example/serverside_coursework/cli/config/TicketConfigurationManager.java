package com.example.serverside_coursework.cli.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.Scanner;

public class TicketConfigurationManager {
    private static final Logger logger = LoggerFactory.getLogger(TicketConfigurationManager.class);
    private static final String CONFIG_FILE = "ticket_config.json";
    private final Scanner scanner;

    public TicketConfigurationManager() {
        scanner = new Scanner(System.in);
    }

    public TicketSystemConfiguration configure() {
        logger.info("Starting configuration process.");
        TicketSystemConfiguration config = loadExistingConfig();
        if (config == null) {
            config = new TicketSystemConfiguration();
        } else {
            logger.info("Loaded existing configuration: {}", config);
        }

        System.out.println("\n=== Ticket System Configuration ===\n");

        config.setTotalTickets(getValidatedIntInput(
                "Enter total number of tickets: "
        ));

        config.setTicketReleaseRate(getValidatedDoubleInput(
                "Enter number of tickets released per second: "
        ));

        config.setCustomerRetrievalRate(getValidatedDoubleInput(
                "Enter customer tickets retrieval per second: "
        ));

        config.setMaxTicketCapacity(getValidatedIntInput(
                "Enter maximum ticket capacity: "
        ));

        saveConfig(config);
        return config;
    }

    private int getValidatedIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    private double getValidatedDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number");
            }
        }
    }

    private TicketSystemConfiguration loadExistingConfig() {
        try {
            File file = new File(CONFIG_FILE);
            if (!file.exists()) {
                return null;
            }

            Gson gson = new Gson();
            try (Reader reader = new FileReader(file)) {
                TicketSystemConfiguration config = gson.fromJson(reader, TicketSystemConfiguration.class);
                return config;
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load existing configuration");
            return null;
        }
    }

    private void saveConfig(TicketSystemConfiguration config) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(config);

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                writer.write(json);
            }

            System.out.println("\nConfiguration saved successfully to " + CONFIG_FILE);
        } catch (IOException e) {
            System.out.println("Error: Could not save configuration");
 }
}
}
