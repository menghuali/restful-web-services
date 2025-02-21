package aloha.spring.restful_web_services.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import aloha.spring.restful_web_services.user.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
}
