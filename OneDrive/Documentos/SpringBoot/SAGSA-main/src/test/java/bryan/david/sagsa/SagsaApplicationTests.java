package bryan.david.sagsa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
		classes = SagsaApplication.class,
		properties = {
				"spring.datasource.url=jdbc:h2:mem:sagsa-test;DB_CLOSE_DELAY=-1",
				"spring.datasource.driver-class-name=org.h2.Driver",
				"spring.datasource.username=sa",
				"spring.datasource.password=",
				"spring.jpa.hibernate.ddl-auto=create-drop"
		}
)
class SagsaApplicationTests {

	@Test
	void contextLoads() {
	}

}
