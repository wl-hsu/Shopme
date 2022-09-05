package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserwithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userWLH = new User("test@test.com", "passwordtest", "WL", "H");
		userWLH.addRole(roleAdmin);
		
		User savedUser = repo.save(userWLH);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserwithTwoRoles() {
		User userWLH2 = new User("test2@test.com", "passwordtest", "WL2", "H2");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userWLH2.addRole(roleEditor);
		userWLH2.addRole(roleAssistant);
		
		User savedUser = repo.save(userWLH2);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userWLH = repo.findById(1).get();
		System.out.println(userWLH);
		assertThat(userWLH).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userWLH = repo.findById(1).get();
		userWLH.setEnabled(true);
		userWLH.setEmail("updateDeatil@gmail.com");
		
		repo.save(userWLH);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userWLH2 = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		userWLH2.getRoles().remove(roleEditor);
		userWLH2.addRole(roleSalesperson);
		
		repo.save(userWLH2);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "test2@test.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
}
