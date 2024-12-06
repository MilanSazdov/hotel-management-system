package com.hotelmanagement.model;

public enum ReservationStatus {
	WAITING, CONFIRMED, REJECTED, CANCELLED, CHECKED_IN, CHECKED_OUT, CHANGED_MIND
}

// CHANGED_MIND - when a guest cancels a reservation on waiting status
// CANCLED - when a guest cancels a reservation on confirmed status