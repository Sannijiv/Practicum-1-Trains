package models;

import java.util.Iterator;

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
        if (firstWagon != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPassengerTrain() {
        if (firstWagon instanceof PassengerWagon) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isFreightTrain() {
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
        this.firstWagon = newSequence;
    }

    /**
     * @return the number of Wagons connected to the train
     */
    public int getNumberOfWagons() {
        int numberOfWagons = 0;
        for (Wagon w : this) {
            numberOfWagons++;
        }
        return numberOfWagons;

    }

    /**
     * @return the last wagon attached to the train
     */
    public Wagon getLastWagonAttached() {
        if (firstWagon != null) {
            if (firstWagon.hasNextWagon()) {
                return firstWagon.getLastWagonAttached();
            } else {
                return firstWagon;
            }
        }

        return null;
    }

    /**
     * @return the total number of seats on a passenger train
     * (return 0 for a freight train)
     */
    public int getTotalNumberOfSeats() {
        if (isPassengerTrain()) {
            int numberOfSeats = 0;
            for (Wagon w : this) {
                //Cast Wagon to PassengerWagon so getNumberOfSeats() can be used.
                numberOfSeats += ((PassengerWagon) w).getNumberOfSeats();
            }
            return numberOfSeats;
        }

        return 0;
    }

    /**
     * calculates the total maximum weight of a freight train
     *
     * @return the total maximum weight of a freight train
     * (return 0 for a passenger train)
     */
    public int getTotalMaxWeight() {
        if (isFreightTrain()) {
            int totalMaxWeight = 0;
            for (Wagon w : this) {
                //Cast Wagon to FreightWagon so getMaxWeight() can be used.
                totalMaxWeight += ((FreightWagon) w).getMaxWeight();
            }
            return totalMaxWeight;
        }

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
        int count = 0;
        Wagon wagon = null;

        for (Wagon w : this) {
            count++;
            // When the position in the train equals the position requested to find we break the for loop and return wagon;
            if (count == position) {
                wagon = w;
                break;
            }
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
        for (Wagon w : this) {
            //Loop till we find a wagon with the same id and return the wagon w;
            if (w.getId() == wagonId) {
                return w;
            }
        }

        return null;
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
        //First looks for the provided wagon in the train to see if the wagon is already part of the train
        int wagonId = sequence.getId();
        Wagon wagon = findWagonById(wagonId);

        //Can't attach if the amount of wagons added will exceed the engine's capacity or is already part of the train
        if (this.getNumberOfWagons() + sequence.getSequenceLength() > engine.getMaxWagons() || wagon != null) {
            return false;
        }
        //Can't attach if the type of wagon isn't in line with the train type
        else if (this.isPassengerTrain() && sequence instanceof FreightWagon) {
            return false;
        } else if (this.isFreightTrain() && sequence instanceof PassengerWagon) {
            return false;
        }
        //Return true if all the above checks are passed
        else {
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
        //Check if the firstWagon is null and make the sequence the firstWagon if so
        if (firstWagon == null) {
            setFirstWagon(sequence);
            return true;
        }

        //Check if the sequence can attach to the train
        if (canAttach(sequence)) {
            //Extra check to see if the wagon that is going to get attached has a previousWagon and uses reAttachTo() if so else attachTo()
            if (sequence.hasPreviousWagon()) {
                sequence.reAttachTo(getLastWagonAttached());
                return true;
            } else {
                sequence.attachTo(getLastWagonAttached());
                return true;
            }
        }

        return false;

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
        if (firstWagon == null) {
            setFirstWagon(sequence);
            return true;
        }

        //Check if sequence canAttach to train
        if (canAttach(sequence)) {
            //Check if the sequence has more than 1 wagon, if so reAttachTo the lastWagonAttached
            if (sequence.getSequenceLength() > 1) {
                firstWagon.reAttachTo(sequence.getLastWagonAttached());
            } else {
                firstWagon.reAttachTo(sequence);
            }
            //Set the firstWagon to the newSequence
            setFirstWagon(sequence);
            return true;
        }
        return false;

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
        //Find if there is a wagon at the desired position
        Wagon wagon = findWagonAtPosition(position);

        //If it passes through the tests of canAttach and there is no wagon at desired position we continue
        if (canAttach(sequence) && wagon == null) {
            //If desired position is 1 we sequence as firstWagon
            if (position == 1) {
                setFirstWagon(sequence);
                return true;
            }

            //Checks if there is a wagon at the spot before the desired position,
            //if not the connection cannot be made i.e. 4 wagon and position 6 was desired for connection
            wagon = findWagonAtPosition(position - 1);
            if (wagon != null) {
                sequence.attachTo(wagon);
                return true;
            } else {
                return false;
            }
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
        //Find the wagon that needs to be moved
        Wagon wagon = findWagonById(wagonId);

        //If the wagon doesn't exist we return false since there is nothing to be moved
        if (wagon == null) {
            return false;
        }

        //Check to see if the wagon can attach to the desired train.
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
        //Find the wagon where we want to split from
        Wagon wagon = findWagonAtPosition(position);

        //Detaches the desired wagon from its previousWagons and attaches it to the trains rear if the checks are passed and wagon != null;
        if (wagon != null && toTrain.canAttach(wagon)) {
            wagon.detachFromPrevious();
            toTrain.attachToRear(wagon);
            return true;
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
    
    public class WagonIterator implements Iterator<Wagon> {

        private Wagon currentWagon = Train.this.firstWagon;

        @Override
        public boolean hasNext() {
            return currentWagon != null;
        }

        @Override
        public Wagon next() {
            Wagon returnWagon = currentWagon;
            currentWagon = currentWagon.getNextWagon();
            return returnWagon;
        }
    }

    @Override
    public String toString() {
        String wagons = "";
        //Adds all the wagon toString to each other
        for (Wagon w : this) {
            wagons += w.toString();
        }

        return engine.toString() + wagons + " with " + getNumberOfWagons() + " wagons from " + origin + " to " + destination;
    }
}
