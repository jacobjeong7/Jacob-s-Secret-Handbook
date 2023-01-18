package warehouse;

import java.util.stream.IntStream;

/*
 *
 * This class implements a warehouse on a Hash Table like structure,
 * where each entry of the table stores a priority queue.
 * Due to your limited space, you are unable to simply rehash to get more space.
 * However, you can use your priority queue structure to delete less popular items
 * and keep the space constant.
 *
 * @author Ishaan Ivaturi
 */
public class Warehouse {
    private Sector[] sectors;

    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }

    /**
     * Provided method, code the parts to add their behavior
     *
     * @param id     The id of the item to add
     * @param name   The name of the item to add
     * @param stock  The stock of the item to add
     * @param day    The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     *
     * @param id     The id of the item to add
     * @param name   The name of the item to add
     * @param stock  The stock of the item to add
     * @param day    The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        Product jacob = new Product(id, name, stock, day, demand);
        int endID = id % 10;

        sectors[endID].add(jacob);
    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     *
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD
        if (sectors[id % 10].getSize() == 1) {
            return;
        }
        sectors[id % 10].swim(sectors[id % 10].getSize());
    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     *
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
        // IMPLEMENT THIS METHOD
        Sector sector = sectors[id % 10];
        if (sector.getSize() == 5) {
            sector.swap(1, 5);
            sector.deleteLast();
            sector.sink(1);
        }
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     *
     * @param id     The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHOD
        Sector sector = sectors[id % 10];
        IntStream.range(0, sector.getSize()).map(i -> i + 1).filter(temp -> sector.get(temp).getId() == id).forEach(temp -> sector.get(temp).updateStock(amount));
    }

    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     *
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD
        Sector sector = sectors[id % 10];
        int jacob = -1;

        int j = 0;
        while (j < sector.getSize()) {
            if (sector.get(j + 1).getId() == id) {
                jacob = j + 1;
            }
            j++;
        }
        if (jacob == -1) {
            return;
        }

        sector.swap(jacob, sector.getSize());
        sector.deleteLast();
        sector.sink(jacob);
    }

    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     *
     * @param id     The id of the purchased product
     * @param day    The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD
        Sector sector = sectors[id % 10];
        int jacob = -1;
        int j = 0;
        while (j < sector.getSize()) {
            if (sector.get(j + 1).getId() == id) {
                jacob = j + 1;
            }
            j++;
        }
        if (jacob == -1 || sector.get(jacob).getStock() < amount)
            return;

        sector.get(jacob).setLastPurchaseDay(day);
        sector.get(jacob).updateStock(-amount);
        sector.get(jacob).updateDemand(amount);
        sector.sink(jacob);
    }

    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     *
     * @param id     The id of the item to add
     * @param name   The name of the item to add
     * @param stock  The stock of the item to add
     * @param day    The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        Sector sector = sectors[id % 10];

        if (sector.getSize() < 5) {
            addToEnd(id, name, stock, day, demand);
            fixHeap(id);
        } else {
            boolean bocaj = false;
            int jawoozy = 1;
            while (jawoozy < 11) {
                Sector jacobSector = sectors[((id % 10) + jawoozy) % 10];
                if (jacobSector.getSize() < 5) {
                    jacobSector.add(new Product(id, name, stock, day, demand));
                    jacobSector.swim(jacobSector.getSize());
                    bocaj = true;
                    break;
                }
                jawoozy++;
            }

            if (!bocaj) {
                addProduct(id, name, stock, day, demand);
            }


            }
        }



    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }

        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */
    public Sector[] getSectors () {
        return sectors;
    }
}
