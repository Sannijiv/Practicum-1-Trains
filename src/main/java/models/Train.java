package models;

import java.util.Iterator;
import java.util.LinkedList;

public class Train implements Iterable<Wagon> {
    private String origin;
    private String destination;
    private Locomotive engine;
    private Wagon firstWagon;


    /* Representation invariants:
        firstWagon == null || firstWagon.previousWagon == null
        engine != null
     */

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /* three helper methods that are usefull in other methods */
    public boolean hasWagons() {
        // TODO
        if (firstWagon != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPassengerTrain() {
        // TODO
        if (firstWagon instanceof PassengerWagon) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isFreightTrain() {
        // TODO
        if (firstWagon instanceof FreightWagon) {
            return true;
        } else {
            return false;
        }
    }

    public Locomotive getEngine() {
        return engine;
    }

    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Replaces the current sequence of wagons (if any) in the train
     * by the given new sequence of wagons (if any)
     * (sustaining all representation invariants)
     *
     * @param newSequence the new sequence of wagons (can be null)
     */
    public void setFirstWagon(Wagon newSequence) {
        // TODO
        this.firstWagon = newSequence;


    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        // TODO
        boolean lastWagonFound = false;
        int count;

        if (firstWagon != null) {
            count = 1;
        } else {
            count = 0;
            lastWagonFound = true;
        }

        Wagon currentWagon = firstWagon;
        while (!lastWagonFound) {
            if (currentWagon.hasNextWagon()) {
                count++;
                currentWagon = currentWagon.getNextWagon();
            } else {
                lastWagonFound = true;
            }
        }

        return count;
    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        // TODO
        if (firstWagon != null) {
            if (firstWagon.hasNextWagon()) {
                return firstWagon.getLastWagonAttached();
            } else {
                return firstWagon;
            }
        } else {
            return null;
        }

    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        // TODO ******************
        Iterator<Wagon> iterator = iterator();
        int numberOfSeats = 0;
        PassengerWagon wagon = null;

        if (isFreightTrain()) {
            return 0;
        } else if (firstWagon != null) {
            while (iterator.hasNext()) {
                wagon = (PassengerWagon) iterator.next();
                numberOfSeats += wagon.getNumberOfSeats();
            }
            wagon = (PassengerWagon) iterator.next();
            numberOfSeats += wagon.getNumberOfSeats();
        }
        return numberOfSeats;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        // TODO

        return 0;
    }

    /**
     * Finds the wagon at the given position (starting at 1 for the first wagon of the train)
     *
     * @param position
     * @return the wagon found at the given position
     * (return null if the position is not valid for this train)
     */
    public Wagon findWagonAtPosition(int position) {
        // TODO
        Iterator<Wagon> iterator = iterator();
        Wagon wagon = null;
        int count = 1;

        while (iterator.hasNext()) {
            wagon = iterator.next();
            System.out.println(wagon);
            if (count == position) {
                break;
            }
            count++;

        }
        return wagon;
    }

    /**
     * Finds the wagon with a given wagonId
     *
     * @param wagonId
     * @return the wagon found
     * (return null if no wagon was found with the given wagonId)
     */
    public Wagon findWagonById(int wagonId) {
        // TODO
        Iterator<Wagon> iterator = iterator();
        Wagon wagon = null;

        while (iterator.hasNext()) {
            wagon = iterator.next();
            System.out.println(wagon);
            if (wagon.getId() == wagonId) {
                break;
            }
        }
        return wagon;
    }

    /**
     * Determines if the given sequence of wagons can be attached to the train
     * Verfies of the type of wagons match the type of train (Passenger or Freight)
     * Verfies that the capacity of the engine is sufficient to pull the additional wagons
     *
     * @param sequence
     * @return
     */
    public boolean canAttach(Wagon sequence) {
        // TODO
        if (this.getNumberOfWagons() + 1 > engine.getMaxWagons()) {
            return false;
        } else if (this.isPassengerTrain() && sequence instanceof FreightWagon) {
            return false;
        } else if (this.isFreightTrain() && sequence instanceof PassengerWagon) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Tries to attach the given sequence of wagons to the rear of the train
     * No change is made if the attachment cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param sequence
     * @return whether the attachment could be completed successfully
     */
    public boolean attachToRear(Wagon sequence) {
        // TODO
        if (firstWagon == null) {
            firstWagon = sequence;
            return true;
        }

        if (canAttach(sequence)) {
            sequence.attachTo(getLastWagonAttached());
            return true;
        } else {
            return false;
        }

    }

    /**
     * Tries to insert the given sequence of wagons at the front of the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible or the engine has insufficient capacity)
     *
     * @param sequence
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtFront(Wagon sequence) {
        // TODO **********
        if (canAttach(sequence)) {
            this.firstWagon.reAttachTo(sequence);
            this.firstWagon = sequence;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tries to insert the given sequence of wagons at the given wagon position in the train
     * No change is made if the insertion cannot be made.
     * (when the sequence is not compatible of the engine has insufficient capacity
     * or the given position is not valid in this train)
     *
     * @param sequence
     * @return whether the insertion could be completed successfully
     */
    public boolean insertAtPosition(int position, Wagon sequence) {
        // TODO
        Wagon wagon = findWagonAtPosition(position);
        if (canAttach(sequence) || wagon == null) {
            wagon = findWagonAtPosition(position - 1);
            sequence.attachTo(wagon);
            return true;
        }
        return false;
    }

    /**
     * Tries to remove one Wagon with the given wagonId from this train
     * and attach it at the rear of the given toTrain
     * No change is made if the removal or attachment cannot be made
     * (when the wagon cannot be found, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param wagonId
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean moveOneWagon(int wagonId, Train toTrain) {
        // TODO
        Wagon wagon = findWagonById(wagonId);
        if (toTrain.canAttach(wagon)) {
            wagon.removeFromSequence();
            toTrain.attachToRear(wagon);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Tries to split this train and move the complete sequence of wagons from the given position
     * to the rear of toTrain
     * No change is made if the split or re-attachment cannot be made
     * (when the position is not valid for this train, or the trains are not compatible
     * or the engine of toTrain has insufficient capacity)
     *
     * @param position
     * @param toTrain
     * @return whether the move could be completed successfully
     */
    public boolean splitAtPosition(int position, Train toTrain) {
        // TODO
        Wagon wagon = findWagonAtPosition(position);
        if (wagon != null) {
            wagon.detachFromPrevious();
            if (toTrain.canAttach(wagon)) {
                toTrain.attachToRear(wagon);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Reverses the sequence of wagons in this train (if any)
     * i.e. the last wagon becomes the first wagon
     * the previous wagon of the last wagon becomes the second wagon
     * etc.
     * (No change if the train has no wagons or only one wagon)
     */
    public void reverse() {
        // TODO
    }

    @Override
    public Iterator<Wagon> iterator() {
        return new WagonIterator();
    }

    // TODO
    public class WagonIterator implements Iterator<Wagon> {

        private Wagon currentWagon = Train.this.firstWagon;

        @Override
        public boolean hasNext() {
            return currentWagon.hasNextWagon();
        }

        @Override
        public Wagon next() {
            Wagon returnWagon = currentWagon;
            currentWagon = currentWagon.getNextWagon();
            return returnWagon;
        }
    }
}
