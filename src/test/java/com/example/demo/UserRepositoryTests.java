package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser() {
         User user = new User();
         user.setEmail("tom@gmail.com");
         user.setPassword("tom2021");
         user.setFirstName("Tom");
         user.setLastName("Webb");

         User savedUser = userRepo.save(user);

         User existUser = entityManager.find(User.class,savedUser.getId());

         assertThat(existUser.getEmail()).isEqualTo(user.getEmail());
    }


//    public void testFindUserByEmail() {
//        String email = "nam@codejava.net";
//
//        User user =userRepo.findByEmail(email);
//
//        assertThat(user).isNotNull();
//    }

    @Test
    public void testAddRoleToNewUser() {
        User user = new User();
        user.setEmail("daniel@gmail.com");
        user.setPassword("daniel2021");
        user.setFirstName("Daniel");
        user.setLastName("Webb");

        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);

        User savedUser = userRepo.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }

    @Test
    public void testAddRolesToExistingUser() {
        User user = userRepo.findById(1L).get();
        Role roleUser = roleRepo.findByName("user");
        user.addRole(roleUser);

        Role roleAdmin = new Role(2l);
        user.addRole(roleAdmin);

        User savedUser = userRepo.save(user);

        assertThat(savedUser.getRoles().size()).isEqualTo(2);
    }

    @Test
    public void testListAll() {
       Iterable<User> users = userRepo.findAll();
       assertThat(users).hasSizeGreaterThan(0);

       for(User user : users) {
           System.out.println(user);
       }
    }

    @Test
    public void testUpdate() {
        long userId = 1;
        Optional<User> optionalUser = userRepo.findById(userId);

        User user = optionalUser.get();
        user.setPassword("rami2020");
        userRepo.save(user);

        User updatedUser = userRepo.findById(userId).get();
        assertThat(updatedUser.getPassword()).isEqualTo("rami2020");
    }

    @Test
    public void testGet() {
        long userId = 1;
        Optional<User> optionalUser = userRepo.findById(userId);
        assertThat(optionalUser).isPresent();
        System.out.println(optionalUser.get());
    }

    @Test
    public void testDelete(){
         long userId = 2;
         userRepo.deleteById(userId);

         Optional<User> optionalUser = userRepo.findById(userId);
         assertThat(optionalUser).isNotPresent();
    }
}
