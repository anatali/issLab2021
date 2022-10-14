package unibo.SpringDataRest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import unibo.SpringDataRest.repo.PersonRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc

class SpringDataRestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepository;

	@BeforeEach
	public void deleteAllBeforeTests() throws Exception {
		//personRepository.deleteAll();
	}

	//@Test
	public void shouldReturnRepositoryIndex() throws Exception {

		ResultActions res = mockMvc
				.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.people").exists());
		System.out.println("shouldReturnRepositoryIndex res="  + res.andReturn().getResponse().getHeader("location") );
	}

	@Test
	public void shouldCreateEntity() throws Exception {
		ResultActions res = mockMvc
				.perform(post("/people")
				.content("{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("people/")));
		System.out.println("shouldCreateEntity res="  + res.andReturn().getResponse().getHeader("location") );
	}

	@Test
	public void shouldRetrieveEntity() throws Exception {

		MvcResult mvcResult = mockMvc
				.perform(post("/people").content("{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}"))
				.andExpect(status().isCreated())
				.andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Frodo"))
				.andExpect(jsonPath("$.lastName").value("Baggins"));
	}

	//@Test
	public void shouldQueryEntity() throws Exception {

		mockMvc.perform(post("/people").content(
				"{ \"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated());

		mockMvc.perform(
				get("/people/search/findByLastName?name={name}", "Baggins")).andExpect(
				status().isOk()).andExpect(
				jsonPath("$._embedded.people[0].firstName").value(
						"Frodo"));
	}

	//@Test
	public void shouldUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location).content(
				"{\"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo")).andExpect(
				jsonPath("$.lastName").value("Baggins"));
	}

	//@Test
	public void shouldPartiallyUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"firstName\": \"Bilbo Jr.\"}")).andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo Jr.")).andExpect(
				jsonPath("$.lastName").value("Baggins"));
	}

	//@Test
	public void shouldDeleteEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{ \"firstName\": \"Bilbo\", \"lastName\":\"Baggins\"}")).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}


}
