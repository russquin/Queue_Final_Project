package process;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import customer.Customer;

public class Process {

	Scanner in = new Scanner(System.in);
	Random randBool = new Random();
	Rng rand = new Rng();

	String fileName = "queuedata.txt";

	int totalEntered = 0;
	int totalServed = 0;

	long startTime = 0;

	Server<Customer> primary = new Server<>();
	Server<Customer> secondary = new Server<>(false);

	public void startServing() {

		startTime = System.nanoTime();

		int option = 10;

		while (option != 0) {
			System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n"
					+ "Please select an option: \n\n1. Create a Customer \n"
					+ "2. Process a Customer \n3. Open secondary server \n4. Close secondary "
					+ "server \n5. Find q-hat \n6. Find u-hat \n7. Find B(t) \n8. Find "
					+ "primary server history \n9. Find secondary server history \n10 Find "
					+ "entire system history \n11. View primary queue \n12. View secondary " + "queue \n0. Quit");
			try {
				option = in.nextInt();

				if (option == 1) {
					createCustomer();
				} else if (option == 2) {
					processCustomer();
				} else if (option == 3) {
					if (secondary.isOpen()) {
						System.out.println("Secondary server is already open.");
					} else {
						secondary.openServer();
						System.out.println("Secondary server is now open.");
					}
				} else if (option == 4) {
					if (!secondary.isOpen()) {
						System.out.println("Secondary server is already closed.");
					} else {
						secondary.closeServer();
						System.out.println("Secondary server is now closed.");
					}
				} else if (option == 5) {
					calculateQHat();
				} else if (option == 6) {
					calculateUHat();
				} else if (option == 7) {
					calculateBofT();
				} else if (option == 8) {
					viewPrimaryHistory();
				} else if (option == 9) {
					viewSecondaryHistory();
				} else if (option == 10) {
					viewTotalHistory();
				} else if (option == 11) {
					viewPrimaryQueue();
				} else if (option == 12) {
					viewSecondaryQueue();
				} else if (option == 0) {

				} else {
					System.out.println("Please enter a valid selection.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid selection.");
				in.next();
			}
		}
	}

	private void createCustomer() {

		totalEntered++;

		// TODO: generate random numbers via custom code

		Customer customer = new Customer(totalEntered, (int) rand.getPoisson() + 1, startTime, 0);

		// Check if secondary queue is open, and enter which one has less customers
		// queuing.
		// Also check if each queue has 5 or more people. If so, customer has a 50/50
		// chance
		// of deciding to enter or balk.
		if (secondary.isOpen() && secondary.size() < primary.size()) {
			if (secondary.size() >= 5 && primary.size() >= 5) {
				boolean enters = randBool.nextBoolean();
				if (enters) {
					secondary.add(customer);
					secondary.incEntered();
					System.out.println("Customer " + totalEntered + " added to secondary queue.");
				} else {
					totalEntered--;
					System.out.println("The queue is too long! The customer decided not to enter.");
					secondary.incBalked();
				}
			}
		} else {
			if (primary.size() >= 5) {
				boolean enters = randBool.nextBoolean();
				if (enters) {
					primary.add(customer);
					primary.incEntered();
					System.out.println("Customer " + totalEntered + " added to primary queue.");
				} else {
					totalEntered--;
					System.out.println("The queue is too long! The customer decided not to enter.");
					primary.incBalked();
				}
			} else {
				primary.add(customer);
				primary.incEntered();
				System.out.println("Customer " + totalEntered + " added to primary queue.");
			}
		}
	}

	private void processCustomer() {

		int option = 1;

		System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n" + "Primary queue: "
				+ primary.size() + " customers. \nSecondary queue: " + secondary.size() + " customers.");

		if (!primary.isEmpty() && !secondary.isEmpty()) {

			System.out.println("Which would you like to process from? \n\n1. Primary \n2. Secondary");

			try {
				option = in.nextInt();

				if (option == 1) {
					processPrimaryCustomer();
				} else if (option == 2) {
					processSecondaryCustomer();
				} else {
					System.out.println("Please enter a valid selection.");
					processCustomer();
				}
			} catch (InputMismatchException ime) {
				System.out.println("Please enter a valid selection.");
				in.next();
				processCustomer();
			}
		} else if (!primary.isEmpty() && secondary.isEmpty()) {
			processPrimaryCustomer();
		} else if (primary.isEmpty() && !secondary.isEmpty()) {
			processSecondaryCustomer();
		} else {
			System.out.println("No customers to process.");
		}

	}

	private void processPrimaryCustomer() {

	}

	private void processSecondaryCustomer() {

	}

	private void calculateQHat() {

	}

	private void calculateUHat() {

	}

	private void calculateBofT() {

	}

	private void viewPrimaryHistory() {

	}

	private void viewSecondaryHistory() {

	}

	private void viewTotalHistory() {

	}

	private void viewPrimaryQueue() {
		System.out.println("Primary queue:\n  Customers entered: " + primary.getEntered() + "\n  Customers served: "
				+ primary.getServed() + "\n\n" + primary.toString());
	}

	private void viewSecondaryQueue() {
		System.out.println("Secondary queue:\n  Customers entered: " + secondary.getEntered() + "\n  Customers served: "
				+ secondary.getServed() + "\n\n" + secondary.toString());
	}

}
