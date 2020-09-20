package models;

public class Wagon {
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
        // TODO
        if (nextWagon != null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * @return whether this wagon has a wagon prepended at the front
     */
    public boolean hasPreviousWagon() {
        // TODO
        if (previousWagon != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * finds the last wagon of the sequence of wagons attached to this wagon
     * if no wagons are attached return this wagon itselves
     *
     * @return the wagon found
     */
    public Wagon getLastWagonAttached() {
        // TODO provide an iterative solution (without recursion)
        boolean lastWagonFound = false;
        Wagon current = this;

        while (!lastWagonFound) {
            if (current.hasNextWagon()) {
                current = current.getNextWagon();
            } else {
                lastWagonFound = true;
            }
        }

        return current;
    }

    /**
     * @return the number of wagons appended to this wagon
     * return 0 if no wagons have been appended.
     */
    public int getSequenceLength() {
        // TODO provide a recursive solution
        if (hasNextWagon()) {
            return count(this, 0);
        } else {
            return 0;
        }
    }

    public int count(Wagon current, int count) {
        if (current.hasNextWagon()) {
            return count(current.getNextWagon(), count + 1);
        } else {
            return count;
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
        // TODO verify the exceptions
        if (this.previousWagon != null) {
            throw new RuntimeException("This wagon is already appended to a wagon");
        } else if (newPreviousWagon.nextWagon != null) {
            throw new RuntimeException("The previous wagon already has a wagon appended to it");
        }

        // TODO attach this wagon to its new predecessor (sustaining the invariant propositions).
        newPreviousWagon.nextWagon = this;
        this.previousWagon = newPreviousWagon;
    }

    /**
     * detaches this wagon from its previous wagons.
     * no action if this wagon has no previous wagon attached.
     */
    public void detachFromPrevious() {
        // TODO detach this wagon from its predecessors (sustaining the invariant propositions).
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
        // TODO detach this wagon from its successors (sustaining the invariant propositions).
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
        // TODO detach any existing connections that will be rearranged
        this.detachFromPrevious();
        newPreviousWagon.detachTail();

        // TODO attach this wagon to its new predecessor (sustaining the invariant propositions).
        this.attachTo(newPreviousWagon);
    }

    /**
     * Removes this wagon from the sequence that it is part of, if any.
     * Reconnect the subsequence of its predecessors with the subsequence of its successors, if any.
     */
    public void removeFromSequence() {
        // TODO
        if (hasNextWagon() && hasPreviousWagon()) {
            this.previousWagon.nextWagon = this.nextWagon;
            this.nextWagon.previousWagon = this.previousWagon;
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
        // TODO provide a recursive implementation

        return null;
    }

    // TODO
}
