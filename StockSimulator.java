import java.util.*;

class Stock {
    private String stokename;
    private double price;

    public Stock(String stokename, double price) {
        this.stokename = stokename;
        this.price = price;
    }

    public String getstokename() { return stokename; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

class Order {
    enum Type { BUY, SELL }
    private String stokename;
    private int quantity;
    private Type type;
    private double price; // execution price
    private Date timestamp;

    public Order(String stokename, int quantity, Type type, double price) {
        this.stokename = stokename;
        this.quantity = quantity;
        this.type = type;
        this.price = price;
        this.timestamp = new Date();
    }

    public String getstokename() { return stokename; }
    public int getQuantity() { return quantity; }
    public Type getType() { return type; }
    public double getPrice() { return price; }
    public Date getTimestamp() { return timestamp; }
}

class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>(); // stokename → quantity

    public int getShares(String stokename) {
        return holdings.getOrDefault(stokename, 0);
    }

    public void addShares(String stokename, int qty) {
        holdings.put(stokename, getShares(stokename) + qty);
    }

    public void removeShares(String stokename, int qty) {
        int current = getShares(stokename);
        if (qty > current) throw new RuntimeException("Not enough shares");
        holdings.put(stokename, current - qty);
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }
}

class User {
    private String name;
    private double cash;
    private Portfolio portfolio;
    private List<Order> history;

    public User(String name, double initialCash) {
        this.name = name;
        this.cash = initialCash;
        this.portfolio = new Portfolio();
        this.history = new ArrayList<>();
    }

    public double getCash() { return cash; }
    public Portfolio getPortfolio() { return portfolio; }
    public List<Order> getHistory() { return history; }

    public void addCash(double amount) { cash += amount; }
    public void deductCash(double amount) {
        if (amount > cash) throw new RuntimeException("Insufficient funds");
        cash -= amount;
    }

    public void recordOrder(Order o) {
        history.add(o);
    }
}

class Market {
    private Map<String, Stock> stocks = new HashMap<>();
    private Random rand = new Random();

    public void addStock(Stock s) {
        stocks.put(s.getstokename(), s);
    }

    public Stock getStock(String stokename) {
        return stocks.get(stokename);
    }

    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }

    // Simulate price updates, e.g. random walk
    public void simulatePriceChanges() {
        for (Stock s : stocks.values()) {
            double old = s.getPrice();
            double changePercent = (rand.nextDouble() - 0.5) * 0.1; // ±5%
            double newPrice = old * (1 + changePercent);
            if (newPrice < 0.1) newPrice = 0.1;
            s.setPrice(newPrice);
        }
    }

    // Execute a simple market order (immediate) for user
    public void executeOrder(User user, String stokename, int qty, Order.Type type) {
        Stock s = stocks.get(stokename);
        if (s == null) {
            System.out.println("Stock stokename not found.");
            return;
        }
        double price = s.getPrice();
        if (type == Order.Type.BUY) {
            double cost = price * qty;
            if (cost > user.getCash()) {
                System.out.println("Insufficient cash.");
                return;
            }
            user.deductCash(cost);
            user.getPortfolio().addShares(stokename, qty);
            Order o = new Order(stokename, qty, type, price);
            user.recordOrder(o);
            System.out.println("Bought " + qty + " shares of " + stokename + " @ " + price);
        } else { // SELL
            int have = user.getPortfolio().getShares(stokename);
            if (qty > have) {
                System.out.println("Not enough shares to sell.");
                return;
            }
            user.getPortfolio().removeShares(stokename, qty);
            double proceeds = price * qty;
            user.addCash(proceeds);
            Order o = new Order(stokename, qty, type, price);
            user.recordOrder(o);
            System.out.println("Sold " + qty + " shares of " + stokename + " @ " + price);
        }
    }
}

public class StockSimulator {
    private Market market;
    private User user;
    private Scanner sc;

    public StockSimulator() {
        market = new Market();
        user = new User("Alice", 10_000); // starting cash
        sc = new Scanner(System.in);

        // Add some sample stocks
        market.addStock(new Stock("ABC", 100.0));
        market.addStock(new Stock("XYZ", 50.0));
        market.addStock(new Stock("PQR", 200.0));
    }

    public void run() {
        while (true) {
            System.out.println("\n--- Market Prices ---");
            for (Stock s : market.getAllStocks()) {
                System.out.printf("%s : %.2f\n", s.getstokename(), s.getPrice());
            }
            System.out.println("Cash: " + user.getCash());
            System.out.println("Holdings: " + user.getPortfolio().getHoldings());

            System.out.println("\nOptions: 1) Buy 2) Sell 3) History 4) Next Step 5) Exit");
            int choice = sc.nextInt();
            if (choice == 1) {
                System.out.print("stokename: ");
                String sym = sc.next();
                System.out.print("Qty: ");
                int q = sc.nextInt();
                market.executeOrder(user, sym, q, Order.Type.BUY);
            } else if (choice == 2) {
                System.out.print("stokename: ");
                String sym = sc.next();
                System.out.print("Qty: ");
                int q = sc.nextInt();
                market.executeOrder(user, sym, q, Order.Type.SELL);
            } else if (choice == 3) {
                System.out.println("Transaction History:");
                for (Order o : user.getHistory()) {
                    System.out.printf("%s %s %d @ %.2f on %s\n",
                        o.getType(), o.getstokename(), o.getQuantity(), o.getPrice(), o.getTimestamp());
                }
            } else if (choice == 4) {
                // progress time, simulate price
                market.simulatePriceChanges();
            } else if (choice == 5) {
                break;
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        new StockSimulator().run();
    }
}
