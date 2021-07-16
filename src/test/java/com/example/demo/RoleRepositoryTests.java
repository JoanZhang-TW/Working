package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
    @Autowired
    RoleRepository repo;

    @Test
    public void testCreateRoles() {
        Role user = new Role("user");
        Role admin = new Role("admin");
        Role customer = new Role("customer");
        repo.saveAll(List.of(user,admin,customer));

        List<Role> listRoles = repo.findAll();
        assertThat(listRoles.size()).isEqualTo(3);
    }


}
