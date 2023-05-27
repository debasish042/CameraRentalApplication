
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CameraRentalAppMain {

    static Scanner sc = new Scanner(System.in);
    static List<Camera> cameraList = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static Map<String, Double> wallet = new HashMap<>();
    static String loggedInUser = null;

    public static final String STATUS_AVAILABLE = "Available";
    public static final String STATUS_RENTED = "Rented";

    public static void main(String[] args) {
        cameraList.add(new Camera(1, "Canon", "3000D", 1500.0, STATUS_AVAILABLE));
        cameraList.add(new Camera(2, "Nikon", "Z30", 3000.0, STATUS_AVAILABLE));
        cameraList.add(new Camera(3, "Sony", "IN5", 2000.0, STATUS_AVAILABLE));
        cameraList.add(new Camera(4, "Raptas", "5D", 500.0, STATUS_AVAILABLE));
        users.add(new User("raja", "raja369", 10000));

        


		System.out.println("+-------------------------------------------+");
		System.out.println("|        WELCOME TO CAMERA RENTAL APP       |");
		System.out.println("+-------------------------------------------+");
		System.out.println("PLEASE LOGIN TO CONTINUE -");

		// login
		while (true) {
			try {
				System.out.print("USERNAME - ");
				String username = sc.nextLine();
				System.out.print("PASSWORD - ");
				String password = sc.nextLine();

				for (int i = 0; i < users.size(); i++) {
					User user = users.get(i);
					if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
						loggedInUser = username;
						break;
					}
				}
				if (loggedInUser == null) {
					System.out.println("Invalid credentials. Please try again.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("Please Enter valid credentials\n");
			}
		}

		// main menu
		while (true) {
			System.out.println("1. MY CAMERA");
			System.out.println("2. RENT A CAMERA");
			System.out.println("3. VIEW ALL CAMERAS");
			System.out.println("4. MY WALLET");
			System.out.println("5. EXIT");
			try {
				int choice = Integer.parseInt(sc.nextLine());
				switch (choice) {
				case 1:
					myCamera(loggedInUser);
					break;
				case 2:
					viewAllCameras();
					rentCamera(loggedInUser);
					break;
				case 3:
					viewAllCameras();
					break;
				case 4:
					myWallet(loggedInUser);
					break;
				case 5:
					System.out.println("Thank you for using the app!");
					System.exit(0);
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			} catch (NumberFormatException | InputMismatchException e) {
				System.out.println("Please enter valid number from 1 to 5\n");
			}
		}
	}

    public static void myWallet(String loggedInUser) {
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			if (user.getUsername().equalsIgnoreCase(loggedInUser)) {
				System.out.println("YOUR CURRENT WALLET BALANCE IS - INR." + user.getWallet());
				System.out.print("DO YOU WANT TO DEPOSIT MORE AMOUNT TO YOUR WALLET?(1.YES 2.NO) - ");
				String input = sc.nextLine();
				if (input.equals("1")) {
					try {
						System.out.print("ENTER THE AMOUNT (INR) - ");
						String input2 = sc.nextLine();
						int amountUpdate = Integer.parseInt(input2);

						// Find the user with the specified username
						User userToUpdateWallet = null;
						for (int j = 0; j < users.size(); j++) {
							User user1 = users.get(j);
							if (user.getUsername().equalsIgnoreCase(loggedInUser)) {
								userToUpdateWallet = user1;
								break;
							}
						}

						// Update the user wallet with money
						if (userToUpdateWallet != null) {
							userToUpdateWallet.addMoneyToWallet(amountUpdate);
							System.out.println("YOUR WALLET BALANCE UPDATED SUCCESSFULLY. CURRENT WALLET BALANCE - INR."
									+ user.getWallet());
						} else {
							System.out.println("User with username " + loggedInUser + " not found.");
						}
					} catch (NumberFormatException | InputMismatchException e) {
						System.out.println("\nPlease enter valid amount in numbers");
					}
				} else {
					break;
				}
			}
		}
	}


	public static void myCamera(String loggedInUser) {
		Scanner sc = new Scanner(System.in);
		boolean backToMenu = false;
		while (!backToMenu) {
			try {
				System.out.println("1. ADD");
				System.out.println("2. REMOVE");
				System.out.println("3. VIEW MY CAMERAS");
				System.out.println("4. GO TO PREVIOUS MENU");
//				System.out.print("Enter your choice: ");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					addCamera();
					break;
				case 2:
					removeCamera();
					break;
				case 3:
					viewMyCameras(loggedInUser);
					break;
				case 4:
					backToMenu = true;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			} catch (InputMismatchException e) {
				System.out.println("\nPlease enter valid number from 1 to 4\n");
				break;
			}
		}

	}

	private static void rentCamera(String loggedInUser) {
	    Scanner sc = new Scanner(System.in);
	    try {
	        // Get user input
	        System.out.print("ENTER THE CAMERA ID YOU WANT TO RENT - ");
	        int cameraCode = sc.nextInt();
	        sc.nextLine();

	        // Get the camera from the camera list
	        Camera rentedCamera = getCameraById(cameraList, cameraCode);
	        if (rentedCamera == null) {
	            System.out.println("Camera with ID " + cameraCode + " not found.");
	            return;
	        }

	        if (!rentedCamera.getRentalStatus().equals(STATUS_AVAILABLE)) {
	            System.out.println("Camera with ID " + cameraCode + " is not available for rent.");
	            return;
	        }

	        // Get rental period
	        System.out.print("ENTER RENTAL PERIOD (in days) - ");
	        int rentalPeriod = sc.nextInt();
	        sc.nextLine();

	        double rentAmount = rentedCamera.getPrice() * rentalPeriod;

	        // Find the user with the specified username
	        User userToUpdate = null;
	        for (User user : users) {
	            if (user.getUsername().equalsIgnoreCase(loggedInUser)) {
	                userToUpdate = user;
	                break;
	            }
	        }

	        if (userToUpdate == null) {
	            System.out.println("User not found.");
	            return;
	        }

	        if (rentAmount > userToUpdate.getWallet()) {
	            System.out.println("ERROR: TRANSACTION FAILED DUE TO INSUFFICIENT WALLET BALANCE. PLEASE DEPOSIT THE AMOUNT TO YOUR WALLET.");
	            return;
	        }

	        // Update camera and user data
	        rentedCamera.setRentalStatus(STATUS_RENTED);
	        userToUpdate.addCamera(rentedCamera);
	        userToUpdate.deductMoneyFromWallet(rentAmount);

	        System.out.println("YOUR TRANSACTION FOR CAMERA with ID " + cameraCode + " with rent INR " + rentAmount + " HAS SUCCESSFULLY COMPLETED.");
	    } catch (NumberFormatException | InputMismatchException e) {
	        System.out.println("Please enter a valid number.\n");
	    }
	}

	public static Camera getCameraById(List<Camera> cameraList, int id) {
	    for (Camera camera : cameraList) {
	        if (camera.getId() == id) {
	            return camera;
	        }
	    }
	    return null;
	}


	public static void addCamera() {
	    try {
	        Scanner sc = new Scanner(System.in);

	        System.out.print("ENTER THE CAMERA BRAND - ");
	        String brand = sc.nextLine();
	        System.out.print("ENTER THE MODEL - ");
	        String model = sc.nextLine();
	        System.out.print("ENTER THE PER DAY PRICE (INR) - ");
	        double price = Double.parseDouble(sc.nextLine());

	        int id = generateCameraId();
	        Camera camera = new Camera(id, brand, model, price, STATUS_AVAILABLE);
	        cameraList.add(camera);

	        System.out.println("YOUR CAMERA HAS BEEN SUCCESSFULLY ADDED TO THE LIST.\n");
	    } catch (NumberFormatException | InputMismatchException e) {
	        System.out.println("Please enter valid data.\n");
	    }
	}

	public static int generateCameraId() {
	    int lastCameraId = 0;
	    if (!cameraList.isEmpty()) {
	        Camera lastCamera = cameraList.get(cameraList.size() - 1);
	        lastCameraId = lastCamera.getId();
	    }
	    int newCameraId = lastCameraId + 1;
	    return newCameraId;
	}


	public static void removeCamera() {
	    Scanner scanner = new Scanner(System.in);
	    try {
	        System.out.println("================================================================");
	        System.out.println("CAMERA ID  BRAND     MODEL    PRICE (PER DAY)  STATUS");
	        System.out.println("=============================================================== ");
	        for (int i = 0; i < cameraList.size(); i++) {
	            Camera camera = cameraList.get(i);
	            System.out.format("%-10d%-10s%-10s%-18s%s%n", camera.getId(), camera.getBrand(), camera.getModel(),
	                    camera.getPrice(),
	                    camera.getRentalStatus().equalsIgnoreCase(STATUS_AVAILABLE) ? STATUS_AVAILABLE : STATUS_RENTED);
	        }
	        System.out.println("------------------------------------------------------");
	        System.out.print("ENTER THE CAMERA ID TO REMOVE - ");

	        if (scanner.hasNextInt()) {
	            int cameraId = scanner.nextInt();
	            // processing the input
	            boolean found = false;
	            for (int i = 0; i < cameraList.size(); i++) {
	                Camera camera = cameraList.get(i);
	                if (camera.getId() == cameraId) {
	                    cameraList.remove(camera);
	                    found = true;
	                    System.out.println("CAMERA SUCCESSFULLY REMOVED FROM THE LIST.");
	                    break;
	                }
	            }
	            if (!found) {
	                System.out.println("Camera with ID " + cameraId + " not found.");
	            }
	        } else {
	            System.out.println("Invalid input. Please enter a valid integer.");
	        }
	    } catch (NumberFormatException | InputMismatchException e) {
	        System.out.println("Please enter valid data.\n");
	    } finally {
	        scanner.nextLine(); // Consume the remaining newline character
	    }
	}


	public static void viewAllCameras() {
	    System.out.println("\nFOLLOWING IS THE LIST OF AVAILABLE CAMERA(S)-");
	    System.out.println("================================================================================");
	    if (cameraList.size() == 0) {
	        System.out.println("No cameras are available for rent.");
	    } else {
	        System.out.println("CAMERA ID\tBRAND\t\tMODEL\t\tPRICE (PER DAY)\tSTATUS");
	        System.out.println("================================================================================");
	        for (Camera camera : cameraList) {
	            System.out.println(camera.getId() + "\t\t" + camera.getBrand() + "\t\t" + camera.getModel() + "\t\t"
	                    + camera.getPrice() + "\t\t" + camera.getRentalStatus());
	        }
	    }
	    System.out.println("================================================================================");
	}

	public static void viewMyCameras(String loggedInUser) {
	    for (User user : users) {
	        if (user.getUsername().equals(loggedInUser)) {
	            user.viewMyCameras();
	            return;
	        }
	    }
	    System.out.println("User with username " + loggedInUser + " not found.");
	}
}
