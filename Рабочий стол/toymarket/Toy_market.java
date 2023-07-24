import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}

public class ToyStore {
    private List<Toy> toys;
    private List<Toy> prizeToys;

    public ToyStore() {
        toys = new ArrayList<>();
        prizeToys = new ArrayList<>();
    }

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void updateToyWeight(int toyId, double weight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(weight);
                break;
            }
        }
    }

    public void startToyGiveaway() {
        double totalWeight = 0;
        for (Toy toy : toys) {
            totalWeight += toy.getWeight();
        }

        Random random = new Random();
        double randomNumber = random.nextDouble() * totalWeight;

        double currentWeight = 0;
        for (Toy toy : toys) {
            currentWeight += toy.getWeight();
            if (currentWeight >= randomNumber) {
                addToPrizeToys(toy);
                break;
            }
        }
    }

    private void addToPrizeToys(Toy toy) {
        prizeToys.add(toy);
        toy.setQuantity(toy.getQuantity() - 1);
    }

    public Toy getPrizeToy() {
        if (prizeToys.isEmpty()) {
            return null;
        }

        Toy prizeToy = prizeToys.get(0);
        prizeToys.remove(0);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("prize_toys.txt", true))) {
            writer.write(prizeToy.getId() + "," + prizeToy.getName());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prizeToy;
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();

        // Добавляем игрушки
        toyStore.addToy(new Toy(1, "Кукла", 10, 20));
        toyStore.addToy(new Toy(2, "Мяч", 15, 30));
        toyStore.addToy(new Toy(3, "Машинка", 12, 10));

        // Изменяем вес игрушек
        toyStore.updateToyWeight(1, 15);
        toyStore.updateToyWeight(2, 40);
        toyStore.updateToyWeight(3, 25);

        // Организуем розыгрыш
        toyStore.startToyGiveaway();

        // Получаем призовую игрушку
        Toy prizeToy = toyStore.getPrizeToy();
        if (prizeToy != null) {
            System.out.println("Вы получили призовую игрушку: " + prizeToy.getName());
        } else {
            System.out.println("Призовые игрушки закончились");
        }
    }
}