package de.ollihoo.domain

class LinkedPoi {

    private PointOfInterest currentPoi
    private LinkedPoi nextPoi

    LinkedPoi(PointOfInterest currentPoi) {
        this.currentPoi = currentPoi
    }

    void setNextPoi(LinkedPoi poi) {
        this.nextPoi = poi
    }

    LinkedPoi getNextPoi() {
        nextPoi
    }

    PointOfInterest getPoi() {
        currentPoi
    }

}
