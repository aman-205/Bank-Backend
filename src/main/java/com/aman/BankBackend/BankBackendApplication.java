package com.aman.BankBackend;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info =@Info(
				title="Custom Bank App",
				description = "Backend Rest APIs for Custom bank",
				version= "v1.0",
				contact = @Contact(
						name = "Aman Bisht",
						email = "69soyachep@gmail.com",
						url = "https://github.com/aman-205/Bank-Backend"
				),
				license = @License(
						name = "The Custom Bank",
						url = "https://github.com/aman-205/Bank-Backend"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "The custom bank documentation",
				url = "https://github.com/aman-205/Bank-Backend"
		)

)
public class BankBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankBackendApplication.class, args);
	}

}
