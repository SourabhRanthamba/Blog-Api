package com.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.blog.Repositories.RoleRepo;
import com.blog.entities.Role;

import java.util.List;

import org.modelmapper.ModelMapper;
@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner{


	
	
	@Autowired
	private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

    @Bean
    ModelMapper modelmapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role role=new Role();
			role.setId(1);
			role.setName("ADMIN_USER");
			
			Role role1=new Role();
			role1.setId(2);
			role1.setName("NORMAL_USER");
		
			List<Role> of = List.of(role,role1);
		
			List<Role> all = this.roleRepo.saveAll(of);
			all.forEach(r->{
				System.out.println(r.getName());
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
 