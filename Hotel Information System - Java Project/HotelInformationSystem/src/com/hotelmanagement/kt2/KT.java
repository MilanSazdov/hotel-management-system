package com.hotelmanagement.kt2;

import java.time.LocalDate;
import java.util.ArrayList;

import com.hotelmanagement.controller.AdditionalServicesController;
import com.hotelmanagement.controller.MaidController;
import com.hotelmanagement.controller.PriceListController;
import com.hotelmanagement.controller.ReceptionistController;
import com.hotelmanagement.controller.ReservationController;
import com.hotelmanagement.controller.RoomController;
import com.hotelmanagement.controller.RoomTypeController;
import com.hotelmanagement.model.AdditionalServices;
import com.hotelmanagement.model.Admin;
import com.hotelmanagement.model.Gender;
import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Maid;
import com.hotelmanagement.model.PriceList;
import com.hotelmanagement.model.ProfessionalQualification;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.Room;
import com.hotelmanagement.model.RoomCategory;
import com.hotelmanagement.model.RoomStatus;
import com.hotelmanagement.model.RoomType;

// Ova klasa je napravljena u svrhu za kontrolnu tacku 2
// Ovde se koriste klase iz paketa controller i model
// Ovo resenje ostaje u posebnom paketu jer ce se zvanicni projekat nalaziti u src/com/hotelmanagement/full(app) paketu
// Ovde se koriste klase Admin, Receptionist, Maid, Guest, Room, RoomType, PriceList, Reservation, AdditionalServices
// Ovde se koriste controlleri: MaidController, ReceptionistController, RoomController, RoomTypeController, PriceListController, ReservationController, AdditionalServicesController
// Klase iz paketa model su koriscene za kreiranje objekata
// Klase iz paketa controller su koriscene za manipulaciju objektima i njihovim podacima i samu obradu podataka

// Autor: Milan Sazdov, SV21-2023, github: MilanSazdov

public class KT {

	public static void main(String[] args) {
		LocalDate currentDate = LocalDate.now();
		
		Admin admin = new Admin("Pera", "Peric", Gender.NOT_SPECIFIED, currentDate, "+381641234567", "pera", "pera123", 3, 50000, ProfessionalQualification.BASIC_SCHOOL);
		System.out.println(admin.toString());
		System.out.println();
		
		Receptionist receptionist1 = new Receptionist("Mika", "Mikic", Gender.MALE, currentDate, "+381641213567", "mika", "mika123", 10, 30000, ProfessionalQualification.BACHELOR_ACADEMIC);
		Receptionist receptionist2 = new Receptionist("Nikola", "Nikolic", Gender.OTHER, currentDate, "+381621334567", "nidza", "sifra123", 2, 20000, ProfessionalQualification.BASIC_SCHOOL);
		
		Maid maid = new Maid("Jana", "Janic", Gender.FEMALE, currentDate, "+3816342424567", "janica", "janica123", 21, 35000, ProfessionalQualification.PRIMARY_SCHOOL_FOURTH_GRADE);
		
		MaidController maids = MaidController.getInstance();
		maids.addMaid(maid);
		
		System.out.printf("************************************\n");
		System.out.println("Zaposleni: ");
		
		ReceptionistController receptionists = ReceptionistController.getInstance();
		receptionists.addReceptionist(receptionist1);
		receptionists.addReceptionist(receptionist2);
		
		System.out.println("Sobarice: ");
		admin.displayMaids();
		System.out.println("Recepcionisti: ");
		admin.displayReceptionists();
		
		System.out.printf("************************************\n");
		
		admin.removeReceptionist(receptionist2);
		System.out.printf("************************************\n");
		System.out.println("Zaposleni posle brisanja: ");
		System.out.println("Sobarice: ");
		admin.displayMaids();
		System.out.println("Recepcionisti: ");
		admin.displayReceptionists();
		System.out.printf("************************************\n");
		
		Guest guest1 = new Guest("Milica", "Milic", Gender.FEMALE, currentDate, "+100 965445509", "milica.milic@gmail.com", "1234567890");
		Guest guest2 = new Guest("Ana", "Anic", Gender.FEMALE, currentDate, "+130 13213153", "ana.anic@gmail.com", "910111213");
		
		receptionist1.addGuest(guest1);
		receptionist1.addGuest(guest2);
		
		System.out.printf("************************************\n");
		System.out.println("Gosti: ");
		receptionist1.displayAllGuests();
		System.out.printf("************************************\n");
		
		RoomTypeController roomTypeController = RoomTypeController.getInstance();
		
		// 1 - krevetna soba
	    RoomType roomType1 = new RoomType(1, RoomCategory.SINGLE);
	    
	    // 2 - krevetna soba (1 + 1)
	    RoomType roomType2 = new RoomType(2, RoomCategory.DOUBLE_SINGLE_BED);
	    
	    // 2 - krevetna soba 2
	    RoomType roomType2_2 = new RoomType(2, RoomCategory.DOUBLE_TWIN_BEDS);
	    
	    
	    // 3 - krevetna soba (2 + 1)
	    RoomType roomType3 = new RoomType(3, RoomCategory.TRIPLE);
	    
	    roomTypeController.addRoomType(roomType1);
	    roomTypeController.addRoomType(roomType2);
	    roomTypeController.addRoomType(roomType2_2);
	    roomTypeController.addRoomType(roomType3);
	    
	    RoomController roomController = RoomController.getInstance();
	    
	    
	    Room room1 = new Room(101, roomType1, RoomStatus.FREE, "Soba 101");
	    Room room2 = new Room(102, roomType2, RoomStatus.FREE, "Soba 102");
	    Room room3 = new Room(103, roomType3, RoomStatus.FREE, "Soba 103");
	    Room room4 = new Room(104, roomType2_2, RoomStatus.FREE, "Soba 104");
	    
	    
	    roomController.addRoom(room1);
	    roomController.addRoom(room2);
	    roomController.addRoom(room3);
	    roomController.addRoom(room4);
	    
	    
	    System.out.printf("************************************\n");
	    System.out.println("Sobe: ");
	    roomController.displayAllRooms();
	    System.out.printf("************************************\n");
	    
	    System.out.printf("************************************\n");
	    System.out.println("Sobe posle promene:");
	    // Ovde sada soba broj 2 postaje trokrevetna soba
	    roomController.modifyRoomCategory(4, roomTypeController.getRoomTypeList().get(3).getCategory());
	   // roomController.changeRoomType(room4, roomTypeController.getRoomTypeList().get(3));
	    roomController.modifyRoomType(4, roomTypeController.getRoomTypeList().get(3));
	    roomController.displayAllRooms();
	    System.out.printf("************************************\n");
	    
	    // Dodavanje dodatnih usluga
	    
	    AdditionalServicesController additionalServices = AdditionalServicesController.getInstance();
	    
	    AdditionalServices breakfast = new AdditionalServices("Dorucak", 500.00);
	    AdditionalServices lunch = new AdditionalServices("Rucak", 750.00);
	    AdditionalServices dinner = new AdditionalServices("Vecera", 1000.00);
	    AdditionalServices pool = new AdditionalServices("Bazen", 200.00);
	    AdditionalServices spaCenter = new AdditionalServices("Spa centar", 300.00);
	    
	    additionalServices.addAdditionalService(breakfast);
	    additionalServices.addAdditionalService(lunch);
	    additionalServices.addAdditionalService(dinner);
	    additionalServices.addAdditionalService(pool);
	    additionalServices.addAdditionalService(spaCenter);
	    
	    System.out.printf("************************************\n");
	    System.out.println("Dodatne usluge: ");
	    additionalServices.displayAllAdditionalServices();
	    System.out.printf("************************************\n");
	    
	    System.out.printf("************************************\n");
	    System.out.println("Dodatne usluge posle brisanja: ");
	    additionalServices.removeAdditionalService(spaCenter);
	    additionalServices.displayAllAdditionalServices();
	    System.out.printf("************************************\n");
	    
	    LocalDate validFrom = LocalDate.of(2024, 1, 1);
	    LocalDate validTo = LocalDate.of(2024, 12, 13);
	    
	    PriceListController priceList = PriceListController.getInstance();
	    
	    PriceList priceList1 = new PriceList(validFrom, validTo);
	    priceList1.setAdditionalServices(additionalServices.getAdditionalServicesList());
	    priceList1.setRoomTypes(roomTypeController.getRoomTypeList());
	    
	    System.out.println("Cenovnik: ");
	    System.out.println(priceList1.toString());
	    
	    System.out.println("Posle promene cene dorucka: ");
	    
	    priceList1.modifyAdditionalSevicePrice(1, 60.00);
	    
	    // ovim se prikazuje da se menja kompletan original a ne lokalna kopija => sto je i bila ideja mojih controller-a u package controller
	    System.out.println("Cenovnik: ");
	    System.out.println(priceList1.toString());
	    System.out.println("Cena samo dorucka: ");
	    additionalServices.displayAdditionalServiceByName("Dorucak");
	    
	    System.out.println();
	    // prikazivanje slobodnih tipova soba
	    System.out.println("Slobodni tipovi soba: od 2024-01-01 do 2024-12-13");
	    receptionists.displayFreeRoomTypes();
	    System.out.println();
	    
	    // nema potrebe da se prikazuju sobe koje su dostupne za neki odredjeni datum
	    // odnosno dovoljno je prikazati samo sobe koje imaju status FREE
	    // jer ako su neke sobe rezervisane za neki datum onda one nece imati status FREE i nece biti dostupne
	    // ista stvar vazi i za spremacicu i ciscenje (sobe ce tada imati status CLEANING)
	    // Zakljucak: dovoljno je samo ispisati sobe/tipove koje imaju status FREE
	    
	    LocalDate checkInDate = LocalDate.of(2024, 8, 13);
	    LocalDate checkOutDate = LocalDate.of(2024, 8, 23);
	    
	    ReservationController reservationController = ReservationController.getInstance();
	    
	    ArrayList<AdditionalServices> additionalServicesListrez = new ArrayList<>();
	    additionalServicesListrez.add(breakfast);
	    additionalServicesListrez.add(dinner);
	    
	    Reservation reservation = new Reservation(roomController.getRoomList().get(2), checkInDate, checkOutDate, ReservationStatus.WAITING, guest1.getGuestId(), additionalServicesListrez);
	    
	    reservationController.addReservation(reservation);
	    roomController.getRoomList().get(2).setStatus(RoomStatus.OCCUPIED);
	    
	    System.out.println("Rezervacije: ");
	    reservationController.displayAllReservations();
	    
	    LocalDate checkInDatea = LocalDate.of(2024, 6, 1);
	    LocalDate checkOutDatea = LocalDate.of(2024, 6, 30);
	    
	    // prikazivanje slobodnih tipova soba - sada ima 3 sobe i nema sobe sa id 3
	    System.out.println("Slobodni tipovi soba: od 2024-08-01 do 2024-08-31");
	    receptionists.displayFreeRoomTypes();
	    System.out.println();
	    
	    LocalDate checkInDateaa = LocalDate.of(2024, 6, 6);
	    LocalDate checkOutDateaa = LocalDate.of(2024, 6, 12);
	    
	    
	    Reservation reservation1 = new Reservation(roomController.getRoomList().get(1), checkInDateaa, checkOutDateaa, ReservationStatus.WAITING, guest2.getGuestId(), null);
	    
	    reservationController.addReservation(reservation1);
	    roomController.getRoomList().get(1).setStatus(RoomStatus.OCCUPIED);
	    
	    //Prikaz svih rezervacija i prikaz rezervacije od Milice Milic
	    
	    System.out.println("Rezervacije: ");
	    reservationController.displayAllReservations();
	    
	    System.out.println("Rezervacije od Milice Milic: ");
	    receptionist1.displayAllGuestReservations(guest1.getGuestId());
	    System.out.println();
	    
	    System.out.println("Trenutno slobodni tipovi soba: ");
	    receptionists.displayFreeRoomTypes();
	    System.out.println();
	}

}
