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
	Rng rand2 = new Rng();

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

		while (option != 0 || option != 8) {
			checkJockey();
			checkReneg();

			System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n"
					+ "Please select an option: \n\n1. Create a Customer \n"
					+ "2. Process a Customer \n3. Open secondary server \n4. Close secondary server"
					+ "\n5. View servers \n6. Queue Analysis \n7. Reset Sim "
					+ " \n8. Quit with Analysis report \n0. Quit");
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
							e.printStackTrace();
						}
					}
				} else if (option == 5) {
					 viewServers();
				} else if (option == 6) {
					viewStatsMenu();
				} else if (option == 7) {
					primary = new Server<>();
					secondary = new Server<>(false);
					customerServeTime.clear();
					customerTTList.clear();
					totalEntered = 0;
					totalServed = 0;
					secondary.closeServer();
					startTime = System.currentTimeMillis();
					rand.setUlambda(2.0);
					rand.setPlambda(5.65);
					System.out.println("Settings and simulation reset");
				} else if (option == 8) {
					System.out.println("Thanks for using our queue simulator.");
					// calculate end of simulation stats
					endTime = ((System.currentTimeMillis() - startTime) / 1000.0);
					System.out.println("U-hat = " + calculateUHat(endTime));
					System.out.println("Q-hat = " + calculateQHat(endTime));
					System.out.println("B(t) = " + calculateBofT());
					System.exit(0);
				} else if (option == 0) {
					System.out.println("Thanks for using our queue simulator.");
					System.exit(0);
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
						secondary.incEntered();
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
						primary.incEntered();
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
				e.printStackTrace();
			}
		}
	}

	// A customer has a 1% chance of reneging, or choosing to leave a queue, after
	// every action. This percentage is so low because as more customers enter the
	// queue, the greater the chance that one of them will randomly be selected to
	// reneg.
	private void checkReneg() {
		rand2.setUlambda(100.0);
		try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(primaryFile, true));
				BufferedWriter bw2 = new BufferedWriter(new FileWriter(secondaryFile, true))) {
			for (int i = 0; i < primary.size(); i++) {
				double num = rand2.getUniform() + .1;
				if (num < 1.0) {
					Customer customer = primary.removeAtPosition(i);
					primary.incReneged();
					System.out
							.println("Customer " + customer.getId() + " has decided to reneg from the primary queue.");
					bw1.write("Customer " + customer.getId() + " had decided to reneg from the primary queue.");
				}
			}
			if (secondary.isOpen()) {
				for (int i = 0; i < secondary.size(); i++) {
					double num = rand2.getUniform() + .1;
					if (num < 1.0) {
						Customer customer = secondary.removeAtPosition(i);
						secondary.incReneged();
						System.out.println(
								"Customer " + customer.getId() + " has decided to reneg from the secondary queue.");
						bw2.write("Customer " + customer.getId() + " had decided to reneg from the secondary queue.");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createCustomer() {
		// Give customer a random number of items, each with a random time to
		// process, based on Poisson and Uniform RNG. Plambda of 5.65 gives a range of
		// about 0 - 14. Ulambda of 2.0 give range of about .1 - 2.0
		ArrayList<Item> items = new ArrayList<>();
		int numItems = (int) rand.getPoisson() + 1;

		for (int i = 0; i < numItems; i++) {
			Item item = new Item(i + 1, 0);
			item.setProcessTime(rand.getUniform() + .1);
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

			customer.setServeTime(((System.currentTimeMillis() - startTime) / 1000.0) - customer.getStartServeTime());

			totalServed++;
			customerTTList.add(customer.getServeTime() + customer.getWaitTime());
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
			e1.printStackTrace();
		}
	}

	private double calculateQHat(double time) {
		double tServeTime = 0.0;
		double multiplier = 0.0;
		for (double i : customerServeTime) {
			tServeTime += i * multiplier;
			multiplier++;
		}
		return (tServeTime / time);

	}

	private double calculateUHat(double time) {
		double addedTime = 0.0;
		for (double i : customerTTList) {
			addedTime += i;
		}

		return (addedTime / time);

	}

	private double calculateBofT() {
		double addedTime = 0.0;
		for (double i : customerTTList) {
			addedTime += i;
		}

		return addedTime;

	}

	private void changeCustomerVariation() {
		Scanner ccv = new Scanner(System.in);
		double num = 0.0;
		System.out.println(
				"The higher the number the more items on average customers will have. This will produce a range of about "
						+ "a half to one and a half of the entered value. Default is 5.65, which produces 1-15 items.");
		System.out.println("Enter any decimal 0.0-15.0");
		num = ccv.nextDouble();
		if (num < 0.0 || num > 15.0) {
			System.out.println("Invalid number variation unchanged");
		} else {
			rand.setPlambda(num);
			System.out.println("New item range set. Note: This will only effect newly generated customers.");
		}
		return;
	}

	private void changeCheckoutVariation() {
		Scanner ccv = new Scanner(System.in);
		double num = 0.0;
		System.out.println(
				"The higher the number the more time on average itll take to be served. This will produced a range of about"
						+ " 0.1 to the value entered. Default is 2.0.");
		System.out.println("Enter any decimal 0.0-15.0");
		num = ccv.nextDouble();
		if (num < 0.0 || num > 15.0) {
			System.out.println("Invalid number variation unchanged");
		} else {
			rand.setUlambda(num);
			System.out.println("New checkout range set. Note: This will only effect newly generated customers.");
		}
		return;
	}

	private void viewServers() {
		System.out.println("Primary queue:\n  Customers entered: " + primary.getEntered() + "\n  Customers served: "
				+ primary.getServed() + "\n  Balked: " + primary.getBalked() + " \n  Jockeyed: " + primary.getJockeyed()
				+ "\n  Reneged: " + primary.getReneged() + "\n\n" + primary.toString());
		System.out.println("\nSecondary queue:\n  Customers entered: " + secondary.getEntered()
				+ "\n  Customers served: " + secondary.getServed() + "\n  Balked: " + secondary.getBalked()
				+ "\n  Jockeyed: " + secondary.getJockeyed() + "\n  Reneged: " + secondary.getReneged() + "\n\n"
				+ secondary.toString());
	}

	private void viewStatsMenu() {
		Scanner sec = new Scanner(System.in);
		int opt = 0;
		while (opt != 4) {
			// checkJockey();
			// checkReneg();

			System.out.println("\n--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--*--\n"
					+ "Please select an option: \n\n1. Calculate current Qhat \n"
					+ "2. Calculate current Uhat \n3. Calculate current b(t)  \n4. Change the average of Customer items "
					+ " \n5. Change the average on checkout time \n6. back");
			try {
				opt = sec.nextInt();

				if (opt == 1) {
					totalTime = ((System.currentTimeMillis() - startTime) / 1000.0);
					// System.out.println(customerServeTime);
					System.out.println("Q-hat = " + calculateQHat(totalTime));
				} else if (opt == 2) {
					totalTime = ((System.currentTimeMillis() - startTime) / 1000.0);
					// System.out.println(customerTTList);
					System.out.println("U-hat = " + calculateUHat(totalTime));
				} else if (opt == 3) {
					System.out.println("B(t) = " + calculateBofT());
				} else if (opt == 4) {
					changeCustomerVariation();
				} else if (opt == 5) {
					changeCheckoutVariation();
				} else if (opt == 6) {
					return;
				} else {
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
