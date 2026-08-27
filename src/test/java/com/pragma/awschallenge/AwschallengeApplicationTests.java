package com.pragma.awschallenge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.pragma.awschallenge.repository.PersonRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AwschallengeApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepository;

	@Test
	void listPersonsReturnsEmptyListWhenNoPersonsExist() throws Exception {
		personRepository.deleteAll();

		mockMvc.perform(get("/api/persons"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.length()").value(0));
	}

	@Test
	void createPersonPersistsAndReturnsPerson() throws Exception {
		personRepository.deleteAll();

		mockMvc.perform(post("/api/persons")
					.param("name", "Ada Lovelace")
					.param("email", "ada@example.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("Ada Lovelace"))
				.andExpect(jsonPath("$.email").value("ada@example.com"));

		mockMvc.perform(get("/api/persons"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].name").value("Ada Lovelace"))
				.andExpect(jsonPath("$[0].email").value("ada@example.com"));
	}

}
