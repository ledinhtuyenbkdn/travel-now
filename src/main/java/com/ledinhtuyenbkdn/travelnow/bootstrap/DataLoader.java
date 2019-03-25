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
        //add admin
        Admin admin = new Admin();
        admin.setUserName("admin");
        admin.setPassword("secret");
        admin.setFullName("ledinhtuyen");
        admin.setGender(true);

        adminRepository.save(admin);
        //add province
        Province hanoi = new Province();
        hanoi.setNameProvince("Hà Nội");

        Province danang = new Province();
        danang.setNameProvince("Đà Nẵng");

        Province hochiminh = new Province();
        hochiminh.setNameProvince("Hồ Chí Minh");

        provinceRepository.save(hanoi);
        provinceRepository.save(danang);
        provinceRepository.save(hochiminh);
        //add place categories
        Category category1 = new Category();
        category1.setNameCategory("Danh lam & Thắng cảnh");

        Category category2 = new Category();
        category2.setNameCategory("Thiên nhiên & Công viên");

        Category category3 = new Category();
        category3.setNameCategory("Chuyến tham quan");

        Category category4 = new Category();
        category4.setNameCategory("Spa & Sức khỏe");

        Category category5 = new Category();
        category5.setNameCategory("Hoạt động ngoài trời");

        Category category6 = new Category();
        category6.setNameCategory("Công viên nước & giải trí");

        Category category7 = new Category();
        category7.setNameCategory("Đồ ăn & Đồ uống");

        Category category8 = new Category();
        category8.setNameCategory("Nơi mua sắm");

        Category category9 = new Category();
        category9.setNameCategory("Bảo tàng");

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);
        categoryRepository.save(category5);
        categoryRepository.save(category6);
        categoryRepository.save(category7);
        categoryRepository.save(category8);
        categoryRepository.save(category9);


        //TEST EXAMPLE PLACE
        Photo photo = new Photo();
        photo.setUrlImage("image.jpg");

        Place dragonBridge = new Place();
        dragonBridge.setNamePlace("Cau Rong");
        dragonBridge.setAbout("about smth");
        dragonBridge.setAbout("nguyen van linh");
        dragonBridge.setLongitude(123d);
        dragonBridge.setLatitude(321d);
        dragonBridge.getPhotos().add(photo);
        dragonBridge.setProvince(danang);
        dragonBridge.setCategory(category1);

        photoRepository.save(photo);
        placeRepository.save(dragonBridge);
    }
}
