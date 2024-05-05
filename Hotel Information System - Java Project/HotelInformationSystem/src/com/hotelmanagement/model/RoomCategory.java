package com.hotelmanagement.model;

public enum RoomCategory {
	SINGLE("1", "Single bed", 500.00),
    DOUBLE_SINGLE_BED("2", "Double room with one bed", 750.00),
    DOUBLE_TWIN_BEDS("1+1", "Double room with two single beds", 850.00),
    TRIPLE("2+1", "Triple room with one double and one single bed", 1000.00),
    TRIPLE_SINGLE("1+1+1", "Triple room with three single beds", 1050.00),
    QUAD("2+2", "Quad room with two double beds", 1200.00),
    QUAD_EXTRA("3+1", "Quad room with three single beds and one extra bed", 1400.00),
    FIVE_BED("2+2+1", "Room for five with two double beds and one single bed", 1600.00);

    private final String bedCombination;
    private final String description;
    private final double price;
    
    RoomCategory(String bedCombination, String description, double price) {
        this.bedCombination = bedCombination;
        this.description = description;
        this.price = price;
    }

    public String getBedCombination() {
        return bedCombination;
    }

    public String getDescription() {
        return description;
    }
    
	public double getPrice() {
		return price;
	}
}
