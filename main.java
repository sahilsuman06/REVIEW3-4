import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

class Bike {
    private int id;
    private boolean isRented;

    public Bike(int id) {
        this.id = id;
        this.isRented = false;
    }

    public int getId() {
        return id;
    }

    public boolean isRented() {
        return isRented;
    }

    public void rentBike() {
        this.isRented = true;
    }

    public void returnBike() {
        this.isRented = false;
    }
}

class Customer {
    private String name;
    private int numberOfBikes;
    private Date rentalStart;
    private Date rentalEnd;

    public Customer(String name, int numberOfBikes, Date rentalStart) {
        this.name = name;
        this.numberOfBikes = numberOfBikes;
        this.rentalStart = rentalStart;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfBikes() {
        return numberOfBikes;
    }

    public Date getRentalStart() {
        return rentalStart;
    }

    public void setRentalEnd(Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public Date getRentalEnd() {
        return rentalEnd;
    }
}

class BikeRentalSystem {
    private List<Bike> bikes = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private static final int RATE_PER_DAY = 100;

    public BikeRentalSystem(int totalBikes) {
        for (int i = 1; i <= totalBikes; i++) {
            bikes.add(new Bike(i));
        }
    }

    public List<Integer> getAvailableBikes() {
        List<Integer> availableBikes = new ArrayList<>();
        for (Bike bike : bikes) {
            if (!bike.isRented()) {
                availableBikes.add(bike.getId());
            }
        }
        return availableBikes;
    }

    public boolean rentBike(String customerName, int numberOfBikes) {
        int availableCount = getAvailableBikes().size();

        if (numberOfBikes > availableCount) {
            return false;
        }

        Date rentalStart = new Date();
        Customer customer = new Customer(customerName, numberOfBikes, rentalStart);
        customers.add(customer);

        int rentedBikes = 0;
        for (Bike bike : bikes) {
            if (!bike.isRented() && rentedBikes < numberOfBikes) {
                bike.rentBike();
                rentedBikes++;
            }
        }
        return true;
    }

    public double returnBikes(String customerName) {
        Customer customer = null;
        for (Customer c : customers) {
            if (c.getName().equalsIgnoreCase(customerName)) {
                customer = c;
                break;
            }
        }

        if (customer == null) {
            return -1; // Customer not found
        }

        Date rentalEnd = new Date();
        customer.setRentalEnd(rentalEnd);

        for (Bike bike : bikes) {
            if (bike.isRented()) {
                bike.returnBike();
            }
        }

        long diffInMillies = customer.getRentalEnd().getTime() - customer.getRentalStart().getTime();
        long rentalDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        return rentalDays * RATE_PER_DAY * customer.getNumberOfBikes();
    }
}
