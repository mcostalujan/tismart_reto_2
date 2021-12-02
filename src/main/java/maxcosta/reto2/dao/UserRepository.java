package maxcosta.reto2.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import maxcosta.reto2.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

	
	User findUserByUsername(String username);

	User findUserByEmail(String email);
}
