package process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import customer.Customer;
import customer.Item;

public class Process {

	Scanner in = new Scanner(System.in);

	Random randBool = new Random();
	Rng rand = new Rng();

	DecimalFormat df = new DecimalFormat(".##");
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");

	String primaryFile = "primarydata.txt";
	String secondaryFile = "secondarydata.txt";

	int totalEntered = 0;
	int totalServed = 0;

	long startTime = 0;
	double endTime = 0;
	double totalTime = 0;

	ArrayList<Double> customerTTList = new ArrayList<>();
	ArrayList<Double> customerServeTime = new ArrayList<>();

	Server<Customer> primary = new Server<>();
	Server<Customer> secondary = new Server<>(false);

	public void startServing() {

		startTime = System.currentTimeMillis();

		System.out.println("Queue similator.\nCreated by: Russell Quinlan, Zach Ringhoff & Kiernan McCormick");

		int option = 10;

		while (option != 0) {
			checkJockey();
			checkReneg();

			System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n"
					+ "Please select an option: \n\n1. Create a Customer \n"
					+ "2. Process a Customer \n3. Open secondary server \n4. Close secondary "
					+ "\n5. View primary queue \\n6. View secondary queue \\n7.Queue Analysis \\n8. Quit with Analysis Report "
					+ "\n0. Quit");
			try {
				option = in.nextInt();

				if (option == 1) {
					createCustomer();
				} else if (option == 2) {
					determineCustomer();
				} else if (option == 3) {
					if (secondary.isOpen()) {
						System.out.println("Secondary server is already open.");
					} else {
						secondary.openServer();
						System.out.println("Secondary server is now open.");
						try (BufferedWriter bw2 = new BufferedWriter(new FileWriter(secondaryFile, true))) {
							bw2.write(dtf.format(LocalDateTime.now()) + " - Secondary server is now open.");
							bw2.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (option == 4) {
					if (!secondary.isOpen()) {
						System.out.println("Secondary server is already closed.");
					} else {
						secondary.closeServer();
						System.out.println("Secondary server is now closed.");
						try (BufferedWriter bw2 = new BufferedWriter(new FileWriter(secondaryFile, true))) {
							bw2.write(dtf.format(LocalDateTime.now()) + " - Secondary server is now closed.");
							bw2.newLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if (option == 5) {
					viewPrimaryQueue();
				} else if (option == 6) {
					viewSecondaryQueue();
				} else if (option == 7) {
					viewStatsMenu();
				} else if (option == 8){
					System.out.println("Thanks for using our queue simulator.");
					//calculate end of simulation stats
					endTime = ((System.currentTimeMillis() - startTime) / 1000.0);
					System.out.println("U-hat = " + calculateUHat(endTime));
					System.out.println("Q-hat = " + calculateQHat(endTime));
					System.out.println("B(t) = "+ calculateBofT());

				} else if (option == 0) {
					System.out.println("Thanks for using our queue simulator.");
				} else {
					System.out.println("Please enter a valid selection.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid selection.");
				in.next();
			}
		}
	}

	// If customer can move from one queue to the other and be in a better position
	// (i.e. position 4 in primary to position 1/2/3 in secondary) customer will
	// choose to jockey.
	private void checkJockey() {
		if (secondary.isOpen()) {
			try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(primaryFile, true));
					BufferedWriter bw2 = new BufferedWriter(new FileWriter(secondaryFile, true))) {
				int i = 1;
				while (i < primary.size()) {
					if (i > secondary.size()) {
						Customer customer = primary.removeAtPosition(i);
						secondary.add(customer);
						primary.incJockeyed();
						System.out.println("Customer " + customer.getId() + " jockeyed from primary queue, position "
								+ (i + 1) + " to secondary queue, position " + secondary.size());

						bw1.write(dtf.format(LocalDateTime.now()) + " - Customer " + customer.getId()
								+ " jockeyed from primary queue, position " + (i + 1) + " to secondary queue, position "
								+ secondary.size());
						bw1.newLine();
						bw2.write(dtf.format(LocalDateTime.now()) + " - Customer " + customer.getId()
								+ " jockeyed from primary queue, position " + (i + 1) + " to secondary queue, position "
								+ secondary.size());
						bw2.newLine();

					} else {
						i++;
					}
				}
				i = 1;
				while (i < secondary.size()) {
					if (i > primary.size()) {
						Customer customer = secondary.removeAtPosition(i);
						primary.add(customer);
						secondary.incJockeyed();
						System.out.println("Customer " + customer.getId() + " jockeyed from secondary queue, position "
								+ (i + 1) + " to primary queue, position " + primary.size());
						bw1.write(dtf.format(LocalDateTime.now()) + " - Customer " + customer.getId()
								+ " jockeyed from primary queue, position " + (i + 1) + " to secondary queue, position "
								+ secondary.size());
						bw1.newLine();
						bw2.write(dtf.format(LocalDateTime.now()) + " - Customer " + customer.getId()
								+ " jockeyed from primary queue, position " + (i + 1) + " to secondary queue, position "
								+ secondary.size());
						bw2.newLine();
					} else {
						i++;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// TODO: generate random numbers via custom code
	// A customer has a 1% chance of reneging, or choosing to leave a queue, after
	// every action. This percentage is so low because as more customers enter the
	// queue, the greater the chance that one of them will randomly be selected to
	// reneg.
	private void checkReneg() {
		for (int i = 0; i < primary.size(); i++) {
			int num = randBool.nextInt(100);
			if (num < 1) {
				Customer customer = primary.removeAtPosition(i);
				System.out.println("Customer " + customer.getId() + " has decided to reneg from the primary queue.");
			}
		}
		if (secondary.isOpen()) {
			for (int i = 0; i < secondary.size(); i++) {
				int num = randBool.nextInt(100);
				if (num < 1) {
					Customer customer = secondary.removeAtPosition(i);
					System.out.println(
							"Customer " + customer.getId() + " has decided to reneg from the secondary queue.");
				}
			}
		}
	}

	private void createCustomer() {

		// TODO: generate random numbers via custom code

		ArrayList<Item> items = new ArrayList<>();
		int numItems = (int) rand.getPoisson() + 1;

		// Give customer a random number of items(1-15), each with a random time to
		// process(.2-2.0 seconds)
		for (int i = 0; i < numItems; i++) {
			Item item = new Item(i + 1, 0);
			item.setProcessTime(.2 + (2.0 - .2) * randBool.nextDouble());
			items.add(item);
		}

		Customer customer = new Customer(totalEntered + 1, (System.currentTimeMillis() - startTime) / 1000.0, items);

		try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(primaryFile, true));
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(secondaryFile, true))) {
			// Check if secondary queue is open, and enter which one has less customers
			// queuing. Also check if each queue has 5 or more people. If so, customer has a
			// 50/50 chance of deciding to enter or balk.
			if (secondary.isOpen() && secondary.size() < primary.size()) {
				if (secondary.size() >= 5 && primary.size() >= 5) {
					boolean enters = randBool.nextBoolean();
					if (enters) {
						secondary.add(customer);
						secondary.incEntered();
						totalEntered++;
						System.out.println("Customer " + totalEntered + " added to secondary queue.");
						bw2.write(dtf.format(LocalDateTime.now()) + " - Customer " + totalEntered
								+ " added to secondary queue.");
						bw2.newLine();
					} else {
						totalEntered++;
						System.out.println(
								"The queue is too long! Customer " + customer.getId() + " decided not to enter.");
						bw2.write(dtf.format(LocalDateTime.now()) + " - The queue is too long! Customer "
								+ customer.getId() + " decided not to enter.");
						bw2.newLine();
						secondary.incBalked();
					}
				} else {
					secondary.add(customer);
					secondary.incEntered();
					totalEntered++;
					System.out.println("Customer " + totalEntered + " added to secondary queue.");
					bw2.write(dtf.format(LocalDateTime.now()) + " - Customer " + totalEntered
							+ " added to secondary queue.");
					bw2.newLine();
				}
			} else {
				if (primary.size() >= 5) {
					boolean enters = randBool.nextBoolean();
					if (enters) {
						primary.add(customer);
						primary.incEntered();
						totalEntered++;
						System.out.println("Customer " + totalEntered + " added to primary queue.");
						bw1.write(dtf.format(LocalDateTime.now()) + " - Customer " + totalEntered
								+ " added to primary queue.");
						bw1.newLine();
					} else {
						totalEntered++;
						System.out.println(
								"The queue is too long! Customer " + customer.getId() + " decided not to enter.");
						bw1.write(dtf.format(LocalDateTime.now()) + " - The queue is too long! Customer "
								+ customer.getId() + " decided not to enter.");
						bw1.newLine();
						primary.incBalked();
					}
				} else {
					primary.add(customer);
					primary.incEntered();
					totalEntered++;
					System.out.println("Customer " + totalEntered + " added to primary queue.");
					bw1.write(dtf.format(LocalDateTime.now()) + " - Customer " + totalEntered
							+ " added to primary queue.");
					bw1.newLine();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void determineCustomer() {

		int option = 1;

		System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n" + "Primary queue: "
				+ primary.size() + " customers. \nSecondary queue: " + secondary.size() + " customers.");

		if (!primary.isEmpty() && !secondary.isEmpty()) {

			System.out.println("Which would you like to process from? \n\n1. Primary \n2. Secondary");

			try {
				option = in.nextInt();

				if (option == 1) {
					processCustomer(option);
				} else if (option == 2) {
					processCustomer(option);
				} else {
					System.out.println("Please enter a valid selection.");
					determineCustomer();
				}
			} catch (InputMismatchException ime) {
				System.out.println("Please enter a valid selection.");
				in.next();
				determineCustomer();
			}
		} else if (!primary.isEmpty() && secondary.isEmpty()) {
			processCustomer(1);
		} else if (primary.isEmpty() && !secondary.isEmpty()) {
			processCustomer(2);
		} else {
			System.out.println("No customers to process.");
		}

	}

	private void processCustomer(int server) {

		Customer customer = primary.peek();

		try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(primaryFile, true));
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(secondaryFile, true))) {

			// Primary
			if (server == 1) {
				customer = primary.remove();
				primary.incServed();
				bw1.write(dtf.format(LocalDateTime.now()) + " - Processing customer " + customer.getId() + " with "
						+ customer.getItems().size() + " items from primary queue...");
				bw1.newLine();
			}
			// Secondary
			else if (server == 2) {
				customer = secondary.remove();
				secondary.incServed();
				bw2.write(dtf.format(LocalDateTime.now()) + " - Processing customer " + customer.getId() + " with "
						+ customer.getItems().size() + " items from secondary queue...");
				bw2.newLine();
			}

			System.out.println(
					"Processing customer " + customer.getId() + " with " + customer.getItems().size() + " items...\n");

			customer.setStartServeTime((System.currentTimeMillis() - startTime) / 1000.0);
			customer.setWaitTime(customer.getStartServeTime() - customer.getArriveTime());
			customer.getItems().forEach(item -> {
				try {
					System.out.println(
							"Processing item " + item.getId() + ", " + df.format(item.getProcessTime()) + " seconds.");

					// Simulates "processing" each item for its process time
					TimeUnit.MILLISECONDS.sleep((long) (item.getProcessTime() * 1000));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});

			// TODO: Fix timing bug for serveTime
			// customer.setServeTime(System.currentTimeMillis() -
			// customer.getStartServeTime());

			totalServed++;
			customerTTList.add(customer.getCustomerTT());
			customerServeTime.add(customer.getServeTime());

			System.out.println("\nCustomer " + customer.getId() + " served. \nTotal wait time: "
					+ df.format(customer.getWaitTime()) + " seconds. " + "\nTotal serve time: "
					+ df.format(customer.getServeTime()) + " seconds.");

			if (server == 1) {
				bw1.write(dtf.format(LocalDateTime.now()) + " - Customer " + customer.getId()
						+ " served.\n				Total wait time: " + df.format(customer.getWaitTime()) + " seconds."
						+ "\n				Total serve time: " + df.format(customer.getServeTime()) + " seconds.");
				bw1.newLine();
			} else if (server == 2) {
				bw2.write(dtf.format(LocalDateTime.now()) + " - Customer " + customer.getId()
						+ " served.\n				Total wait time: " + df.format(customer.getWaitTime()) + " seconds."
						+ "\n				Total serve time: " + df.format(customer.getServeTime()) + " seconds.");
				bw2.newLine();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private double calculateQHat(double time) {
		double tServeTime = 0.0;
		double multiplier = 0.0;
		for(double i : customerServeTime){
			tServeTime += i*multiplier;
			multiplier++;
		}
		return (tServeTime/time);


	}

	private double calculateUHat(double time) {
		double addedTime=0.0;
		for(double i : customerTTList){
			addedTime += i;
		}

		return (addedTime/time);

	}

	private double calculateBofT() {
		double addedTime = 0.0;
		for(double i : customerTTList){
			addedTime += 1;
		}

		return addedTime;

	}

	private void changeCustomerVariation(){
		Scanner ccv = new Scanner(System.in);
		double num = 0.0;
		System.out.println("The higher the number the more variation");
		System.out.print("Enter any decimal 0.0-15.0");
		num = ccv.nextDouble();
		if(num < 0.0 || num > 15.0){
			System.out.println("Invalid number variation unchanged");
		}else {
			rand.setPlambda(num);
		}
		return;
	}

	private void changeCheckoutVariation(){
		Scanner ccv = new Scanner(System.in);
		double num = 0.0;
		System.out.println("The higher the number the more variation");
		System.out.print("Enter any decimal 0.0-15.0");
		num = ccv.nextDouble();
		if(num < 0.0 || num > 15.0){
			System.out.println("Invalid number variation unchanged");
		}else {
			rand.setUlambda(num);
		}
		return;
	}

	private void viewPrimaryQueue() {
		System.out.println("Primary queue:\n  Customers entered: " + primary.getEntered() + "\n  Customers served: "
				+ primary.getServed() + "\n\n" + primary.toString());
	}

	private void viewSecondaryQueue() {
		System.out.println("Secondary queue:\n  Customers entered: " + secondary.getEntered() + "\n  Customers served: "
				+ secondary.getServed() + "\n\n" + secondary.toString());
	}

	private void viewStatsMenu() {
		Scanner sec = new Scanner(System.in);
		int opt = 0;
		while (opt != 4) {
			checkJockey();
			checkReneg();

			System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n"
					+ "Please select an option: \n\n1. Calculate current Qhat \n"
					+ "2. Calculate current Uhat \n3. Calculate current b(t)  \n4. Change variation on Customer items "
					+ " \n5. Change variation on checkout time \n6. back" );
			try {
				opt = sec.nextInt();

				if (opt == 1) {
					totalTime = ((System.currentTimeMillis() - startTime) / 1000.0);
					System.out.println("Q-hat = " + calculateQHat(totalTime));
				} else if (opt == 2) {
					totalTime = ((System.currentTimeMillis() - startTime) / 1000.0);
					System.out.println("U-hat = " + calculateUHat(totalTime));
				} else if (opt == 3) {
					System.out.println("B(t) = " + calculateBofT());
				} else if (opt == 4) {
					changeCustomerVariation();
				} else if (opt == 5) {
					changeCheckoutVariation();
				}else if (opt == 6) {
					return;
				}else{
					System.out.println("Please enter a valid selection.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Please enter a valid selection.");
				sec.next();
			}
		}
		return;
	}
}
