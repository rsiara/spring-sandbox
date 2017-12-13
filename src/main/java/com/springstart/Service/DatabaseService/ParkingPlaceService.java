package com.springstart.Service.DatabaseService;

import com.springstart.Model.DatabaseDao.ParkingPlaceDao;
import com.springstart.Model.Entity.DatabaseEntity.ParkingPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingPlaceService {

    private ParkingPlaceDao parkingPlaceDao;

    /* Methods */

    public ParkingPlace getById(Integer id){
        return parkingPlaceDao.get(id);
    }

    public void save(ParkingPlace parkingPlace){
        parkingPlaceDao.save(parkingPlace);
    }

    /**/


    public ParkingPlaceDao getParkingPlaceDao() {
        return parkingPlaceDao;
    }

    @Autowired
    public void setParkingPlaceDao(ParkingPlaceDao parkingPlaceDao) {
        this.parkingPlaceDao = parkingPlaceDao;
    }
}
