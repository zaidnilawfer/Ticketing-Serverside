package com.example.serverside_coursework;

import com.example.serverside_coursework.cli.command.TicketSystemController;
import com.example.serverside_coursework.cli.config.TicketConfigurationManager;
import com.example.serverside_coursework.cli.config.TicketSystemConfiguration;
import com.example.serverside_coursework.cli.producerconsumer.TicketPoolCli;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ServerSideCourseworkApplication {

	public static void main(String[] args) {
		// Run Spring Boot application
		SpringApplication.run(ServerSideCourseworkApplication.class, args);

		// Load the configuration
		TicketConfigurationManager configManager = new TicketConfigurationManager();
		TicketSystemConfiguration config = configManager.configure();

		// Check if configuration loaded successfully
		if (config == null) {
			System.out.println("Configuration loading failed. Exiting.");
			return;
		}

		// Display loaded configuration
		System.out.println("Loaded Configuration: " + config);

		// Initialize ticket pool with max ticket capacity
		TicketPoolCli ticketPoolCli = new TicketPoolCli(config.getMaxTicketCapacity(),config.getTotalTickets());

		// Initialize the ticket system controller for managing threads
		TicketSystemController controller = new TicketSystemController(ticketPoolCli);

		// Command loop to handle user input
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("\nEnter command (start, stop, exit, config): ");
			String command = scanner.nextLine().trim().toLowerCase();

			try {
				switch (command) {
					case "start":
						// Prompt for the number of vendors and customers
						System.out.print("Enter number of vendors: ");
						int numVendors = Integer.parseInt(scanner.nextLine().trim());

						System.out.print("Enter number of customers: ");
						int numCustomers = Integer.parseInt(scanner.nextLine().trim());

						// Start ticket handling with user-defined parameters
						// 20 is the number of tickets to add per vendor cycle
						controller.start(
								(long)(config.getTicketReleaseRate()),
								(long)(config.getCustomerRetrievalRate()),
								numVendors,
								numCustomers
						);
						break;

					case "stop":
						controller.stop();
						break;

					case "config":
						System.out.println(config);
						break;

					case "exit":
						controller.stop();
						System.out.println("Exiting...");
						return;

					default:
						System.out.println("Unknown command. Please enter 'start', 'stop', 'config', or 'exit'.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a valid number.");
			}
		}
	}
}