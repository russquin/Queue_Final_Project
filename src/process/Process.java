package process;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import customer.Customer;

public class Process {
	
	Scanner in = new Scanner(System.in);
	Random rand = new Random();
	//Rng rand = new Rng();
	
	String fileName = "queuedata.txt";
	int totalEntered = 0;
	int totalServed = 0;
	int primaryEntered = 0;
	int primaryServed = 0;
	int secondaryEntered = 0;
	int secondaryServed = 0;
	long startTime = 0;
	
	Server<Customer> primary = new Server<>();
	Server<Customer> secondary = new Server<>(false);
	
	public void startServing() {
		
		startTime = System.nanoTime();
		
		int option = 10;		
		
		while (option != 0) {
			System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n" + 
					"Please select an option: \n\n1. Create a Customer \n" +
					"2. Process a Customer \n3. Open secondary server \n4. Close secondary " +
					"server \n5. Find q-hat \n6. Find u-hat \n7. Find B(t) \n8. Find " +
					"primary server history \n9. Find secondary server history \n10. View " + 
					"primary queue \n11. View secondary queue \n0. Quit");
			try {
				option = in.nextInt();
				
				if (option == 1) {
					createCustomer();
				}
				else if (option == 2) {
					processCustomer();
				}
				else if (option == 3) {
					if (secondary.isOpen()) {
						System.out.println("Secondary server is already open.");
					}
					else {
						secondary.openServer();
						System.out.println("Secondary server is now open.");
					}
				}
				else if (option == 4) {
					if (!secondary.isOpen()) {
						System.out.println("Secondary server is already closed.");
					}
					else {
						secondary.closeServer();
						System.out.println("Secondary server is now closed.");
					}
				}
				else if (option == 5) {
					calculateQHat();
				}
				else if (option == 6) {
					calculateUHat();
				}
				else if (option == 7) {
		
				}
				else if (option == 8) {
					
				}
				else if (option == 9) {
					
				}
				else if (option == 10) {
					
				}
				else if (option == 11) {
					
				}
				else if (option == 0) {
					
				}
				else {
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
		
		//TODO: generate random numbers via custom code
		
		Customer customer = new Customer(totalEntered, rand.nextInt(25) + 1, startTime, 0);
		
		// Check if secondary queue is open, and enter which one has less customers queuing.
		// Also check if each queue has 5 or more people. If so, customer has a 50/50 chance 
		// of deciding to enter or balk.
		if (secondary.isOpen() && secondary.size() < primary.size()) {
			if (secondary.size() >= 5 && primary.size() >= 5) {
				boolean enters = rand.nextBoolean();
				if (enters) {
					secondary.add(customer);
					secondaryEntered++;
					System.out.println("Customer " + totalEntered + " added to secondary queue.");		
				}
				else {
					totalEntered--;
					System.out.println("The queue is too long! The customer decided not to enter.");
					//TODO: Record balking stats somewhere
				}
			}
		}
		else {
			if (primary.size() >= 5) {
				boolean enters = rand.nextBoolean();
				if (enters) {
					primary.add(customer);
					primaryEntered++;
					System.out.println("Customer " + totalEntered + " added to primary queue.");
				}
				else {
					totalEntered--;
					System.out.println("The queue is too long! The customer decided not to enter.");
					//TODO: Record balking stats somewhere
				}
			}
			else {
				primary.add(customer);
				primaryEntered++;
				System.out.println("Customer " + totalEntered + " added to primary queue.");
			}
		}
	}
	
	private void processCustomer() {
		
		int option = 1;
		
		System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n" +
				"Primary queue: " + primary.size() + " customers. \nSecondary queue: " + 
				secondary.size() + " customers.");
		
		if (!primary.isEmpty() && !secondary.isEmpty()) {
			
			System.out.println("Which would you like to process from? \n\n1. Primary \n2. Secondary");
			
			try {
				option = in.nextInt();
				
				if (option == 1) {
					processPrimaryCustomer();
				}
				else if (option == 2) {
					processSecondaryCustomer();
				}
				else {
					System.out.println("Please enter a valid selection.");
					processCustomer();
				}
			} catch (InputMismatchException ime) {
				System.out.println("Please enter a valid selection.");
				in.next();
				processCustomer();
			}
		}
		else if (!primary.isEmpty() && secondary.isEmpty()) {
			processPrimaryCustomer();
		}
		else if (primary.isEmpty() && !secondary.isEmpty()) {
			processSecondaryCustomer();
		}
		else {
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

}
