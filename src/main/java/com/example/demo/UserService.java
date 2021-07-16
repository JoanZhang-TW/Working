package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    public void saveUserWithDefaultRole(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Role roleUser = roleRepo.findByName("User");
        user.addRole(roleUser);

        userRepo.save(user);

    }

    public void save(User user) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);

        userRepo.save(user);

    }

    public List<User> listAll() {
        return userRepo.findAll();
    }

//    public User get(Long id) {
//        return userRepo.findById(id).get();
//    }

    public User get(Long id) throws UserNotFoundException{
       Optional<User> result = userRepo.findById(id);
       if(result.isPresent()){
           return result.get();
       }
        throw new UserNotFoundException("Could not find any users with ID" + id);
    }

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    public void delete(Long id) throws UserNotFoundException{
        Long count = userRepo.countById(id);
        if(count == null || count == 0){
            throw new UserNotFoundException("Could not find any users with ID" + id);
        }
        userRepo.deleteById(id);
    }
}
