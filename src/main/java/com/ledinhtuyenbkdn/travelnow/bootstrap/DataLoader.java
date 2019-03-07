package com.ledinhtuyenbkdn.travelnow.bootstrap;

import com.ledinhtuyenbkdn.travelnow.model.*;
import com.ledinhtuyenbkdn.travelnow.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    private AdminRepository adminRepository;
    private PlaceRepository placeRepository;
    private PhotoRepository photoRepository;
    private CategoryRepository categoryRepository;
    private ProvinceRepository provinceRepository;

    @Autowired
    public DataLoader(AdminRepository adminRepository, PlaceRepository placeRepository, PhotoRepository photoRepository, CategoryRepository categoryRepository, ProvinceRepository provinceRepository) {
        this.adminRepository = adminRepository;
        this.placeRepository = placeRepository;
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
        this.provinceRepository = provinceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Admin admin = new Admin();
        admin.setUserName("admin");
        admin.setPassword("secret");
        admin.setFullName("ledinhtuyen");
        admin.setGender(true);
        admin.setBirthDate("10/01/1997");
        adminRepository.save(admin);

        Place dragonBridge = new Place();
        dragonBridge.setNamePlace("Dragon Bridge");
        dragonBridge.setAddress("Da Nang");
        dragonBridge.setAbout("This is description about..");
        dragonBridge.setLatitude(123.45);
        dragonBridge.setLongitude(543.21);

        Photo photo1 = new Photo();
        photo1.setUrlImage("photo1.jpg");

        Province daNang = new Province();
        daNang.setNameProvince("Da Nang");

        Category sightseeing = new Category();
        sightseeing.setNameCategory("Sightseeing");

        dragonBridge.getPhotos().add(photo1);
        dragonBridge.setCategory(sightseeing);
        dragonBridge.setProvince(daNang);

        placeRepository.save(dragonBridge);
    }
}
