package com.library.business;

public interface Reservable {
    void reserve(String userId);
    void cancelReservation();
    boolean isReserved();
    String getReservedBy();
}
