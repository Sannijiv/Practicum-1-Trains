package models;

abstract public class Wagon {
    protected int id;                 // some unique ID of a Wagon
    private Wagon nextWagon;        // another wagon that is appended at the tail of this wagon
    // a.k.a. the successor of this wagon in a sequence
    // set to null if no successor is connected
    private Wagon previousWagon;    // another wagon that is prepended at the front of this wagon
    // a.k.a. the predecessor of this wagon in a sequence
    // set to null if no predecessor is connected


    // representation invariant propositions:
    // tail-connection-invariant:   wagon.nextWagon == null or wagon == wagon.nextWagon.previousWagon
    // front-connection-invariant:  wagon.previousWagon == null or wagon = wagon.previousWagon.nextWagon

    public Wagon(int wagonId) {
        this.id = wagonId;
    }

    public int getId() {
        return id;
    }

    public Wagon getNextWagon() {
        return nextWagon;
    }

    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    /**
     * @return whether this wagon has a wagon appended at the tail
     */
    public boolean hasNextWagon() {
        if (nextWagon != null) {
            return true;
        }
            return false;
    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        if (previousWagon != null) {
            return true;
        }
            return false;
    }

    /**
     * finds the last wagon of the sequence of wagons attached to this wagon
     * if no wagons are attached return this wagon itselves
     *
     * @return the wagon found
     */
    public Wagon getLastWagonAttached() {
        boolean lastWagonIsFound = false;
        Wagon current = this;

        while (!lastWagonIsFound) {
            if (current.hasNextWagon()) {
                current = current.getNextWagon();
            } else {
                lastWagonIsFound = true;
            }
        }
        return current;
    }

    /**
     * @return the number of wagons appended to this wagon
     * return 0 if no wagons have been appended.
     */
    public int getSequenceLength() {
        if (hasNextWagon()) {
            return count(this, 0);
        } else {
            return 1;
        }
    }

    /**
     * Method to recursively keep count of the amount of wagons
     * in a sequence. Adds 1 to the count to account for the current wagon
     * in the sequence.
     *
     * @param current
     * @param count
     * @return count of wagons
     *
     */
    public int count(Wagon current, int count) {
        if (current.hasNextWagon()) {
            return count(current.getNextWagon(), count + 1);
        } else {
            return count + 1;
        }
    }


    /**
     * attaches this wagon at the tail of a given prevWagon.
     *
     * @param newPreviousWagon
     * @throws RuntimeException if this wagon already has been appended to a wagon.
     * @throws RuntimeException if prevWagon already has got a wagon appended.
     */
    public void attachTo(Wagon newPreviousWagon) throws RuntimeException {
        if (this.previousWagon != null) {
            throw new RuntimeException("This wagon is already appended to a wagon");
        } else if (newPreviousWagon.nextWagon != null) {
            throw new RuntimeException("The previous wagon already has a wagon appended to it");
        }

        // Attach this wagon to its new predecessor (sustaining the invariant propositions).
        newPreviousWagon.nextWagon = this;
        this.previousWagon = newPreviousWagon;
    }

    /**
     * detaches this wagon from its previous wagons.
     * no action if this wagon has no previous wagon attached.
     */
    public void detachFromPrevious() {
        // Detach this wagon from its predecessors (sustaining the invariant propositions).
        if (this.hasPreviousWagon()) {
            this.previousWagon.nextWagon = null;
            this.previousWagon = null;
        }
    }

    /**
     * detaches this wagon from its tail wagons.
     * no action if this wagon has no succeeding next wagon attached.
     */
    public void detachTail() {
        // Detach this wagon from its successors (sustaining the invariant propositions).
        if (this.hasNextWagon()) {
            this.nextWagon.previousWagon = null;
            this.nextWagon = null;
        }
    }

    /**
     * attaches this wagon at the tail of a given newPreviousWagon.
     * if required, first detaches this wagon from its current predecessor
     * and/or detaches the newPreviousWagon from its current successor
     *
     * @param newPreviousWagon
     */
    public void reAttachTo(Wagon newPreviousWagon) {
        // Detach any existing connections that will be rearranged
        this.detachFromPrevious();
        newPreviousWagon.detachTail();

        // Attach this wagon to its new predecessor (sustaining the invariant propositions).
        this.attachTo(newPreviousWagon);
    }

    /**
     * Removes this wagon from the sequence that it is part of, if any.
     * Reconnect the subsequence of its predecessors with the subsequence of its successors, if any.
     */
    public void removeFromSequence() {
        if (hasNextWagon() && hasPreviousWagon()) {
            this.previousWagon.nextWagon = this.nextWagon;
            this.nextWagon.previousWagon = this.previousWagon;
            this.nextWagon = null;
            this.previousWagon = null;
        } else if (!hasPreviousWagon() && hasNextWagon()) {
            detachTail();
        } else if (!hasNextWagon() && hasPreviousWagon()) {
            detachFromPrevious();
        } else {
            System.out.println("Nothing in sequence so no need to remove anything");
        }
    }


    /**
     * reverses the order in the sequence of wagons from this Wagon until its final successor.
     * The reversed sequence is attached again to the predecessor of this Wagon, if any.
     * no action if this Wagon has no succeeding next wagon attached.
     *
     * @return the new start Wagon of the reversed sequence (with is the former last Wagon of the original sequence)
     */
    public Wagon reverseSequence() {
        if(!hasNextWagon()) {
            return null;
        }

        Wagon newHeadWagon = reverse(this);

        return newHeadWagon;
    }

    /**
     * recursively reverses the sequence of a given wagon until the final wagon in the sequence.
     * @param currentWagon
     * @return the current wagon if there are no more previous wagons (since it's the first in the list).
     */
    public Wagon reverse(Wagon currentWagon){
        Wagon storedNextWagon = currentWagon.nextWagon;
        currentWagon.nextWagon = currentWagon.previousWagon;
        currentWagon.previousWagon = storedNextWagon;

        // If there are no more previous wagons
        // the list has been reversed.
        if(currentWagon.previousWagon == null){
            return currentWagon;
        }

        // Calls itself again with the previousWagon,
        // which is actually the stored next wagon.
        return reverse(currentWagon.previousWagon);
    }

    // TODO

    @Override
    public String toString() {
        return "[Wagon-" + id + "]";
    }
}
