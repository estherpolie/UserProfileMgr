//package com.example.UserProfileManager3;
//
//
//import com.example.UserProfileManager3.entity.User;
//import com.example.UserProfileManager3.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//
//
//
//import com.example.UserProfileManager3.entity.User;
//import com.example.UserProfileManager3.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.junit.Assert;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Optional;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class UserRepositoryTest {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Test
//	public void testFindUserByEmail() {
//		// Create a new user
//		User user = new User();
//		user.setEmail("test@gmail.com");
//		user.setFirstName("esther");
//		user.setLastName("poli");
//		user.setPassword("1234");
//		userRepository.save(user);
//
//		// Search for the user by email
//		Optional<User> foundUser = userRepository.findUserByEmail("test@example.com");
//
//		// Check that the user was found
//		assertTrue(foundUser.isPresent());
//		assertEquals(user.getEmail(), foundUser.get().getEmail());
//	}
//
//}
