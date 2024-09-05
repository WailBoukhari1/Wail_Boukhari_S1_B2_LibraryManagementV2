package org.library.service;

public interface Reservable {
    boolean isReserved();
    void reserve();
    void cancelReservation();
}
