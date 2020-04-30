package process;


import java.util.Random;
import java.util.Scanner;

import customer.Customer;

public class Process {
	
	private int customerCount = 0;
	long startTime = 0;
	
	Server<Customer> primary = new Server<>();
	Server<Customer> secondary = new Server<>(false);
	
	public void startServing() {
		
		startTime = System.nanoTime();
		
		Scanner in = new Scanner(System.in);
		int option = 10;
		
		System.out.println("Please select an option: \n\n1. Create a Customer \n" +
				"2. Process a Customer \n3. Open secondary server \n4. Close secondary " +
				"server \n5. Find q-hat \n6. Find u-hat \n7. Find B(t) \n8. Find " +
				"primary server history \n9. Find secondary server history \n0. Quit");
		
		
		while (option != 0) {
			option = in.nextInt();
			
			if (option == 1) {
				createCustomer();
			}
			else if (option == 2) {
				
			}
			else if (option == 3) {
				
			}
			else if (option == 4) {
				
			}
			else if (option == 5) {
				
			}
			else if (option == 6) {
				
			}
			else if (option == 7) {
				
			}
			else if (option == 8) {
				
			}
			else if (option == 9) {
				
			}
			else if (option == 0) {
				
			}
			else {
				System.out.println("What happened");
			}
		}
	}

	private void createCustomer() {
		
		customerCount++;
		
		//TODO: generate random number via custom code
		Random rand = new Random();
		
		Customer customer = new Customer(customerCount, rand.nextInt(25) + 1, startTime, 0);
		
		// Check if secondary queue is open, and enter which one has less customers queuing
		if (secondary.isOpen() && secondary.size() < primary.size()) {
			secondary.add(customer);
		}
		else {
			primary.add(customer);
		}
	}

}
