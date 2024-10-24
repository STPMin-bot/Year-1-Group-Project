import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DoctorTest {

	@Test
	void test() {
		Doctor doctor = new Doctor(0, "Smith", "Cardio", 3.5, 350);
		
		String expected_name = "Smitth";
		
		assertEquals(expected_name, doctor.getName());
	}

}
