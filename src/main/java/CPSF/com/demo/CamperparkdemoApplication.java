package CPSF.com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class CamperparkdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamperparkdemoApplication.class, args);
//		for (int i = 0; i <=20 ; i++) {
//			String firstName = "firstName" + i;
//			String lastName = "lastName" + i;
//			String randomString1 =  generateRandomUUIDString(6);
//			String randomString2 =  generateRandomUUIDString(7);
//
//			String query = String.format("INSERT INTO users(first_name,last_name,phone_number,email,car_registration,role_id)" +
//					"Values('%s','%s',null,'%s@mail.com','%s','guest')",
//							firstName,lastName,randomString1,randomString2);
//			System.out.println(query);
//		}
//
//	}
//	public static String generateRandomUUIDString(int length) {
//		String uuid = UUID.randomUUID().toString().replace("-", "");
//		if (length > uuid.length()) {
//			throw new IllegalArgumentException("Maksymalna długość wynosi " + uuid.length());
//		}
//
//		return uuid.substring(0, length);
	}
}