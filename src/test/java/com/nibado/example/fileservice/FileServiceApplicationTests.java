package com.nibado.example.fileservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = FSInitializer.class)
class FileServiceApplicationTests {
	@Value("${save-dir}")
	File saveDir;

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private FileRepository repository;

	@BeforeEach
	public void setup() {
		storedFiles().forEach(File::delete);
		assertThat(storedFiles()).isEmpty();
	}

	@Test
	void Should_be_able_to_upload_and_download_files() {
		LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
		parameters.add("file", new ClassPathResource("/application-test.yml"));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);

		ResponseEntity<String> response = template.exchange("/file/upload", HttpMethod.POST, entity, String.class, "");

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

		assertThat(storedFiles()).hasSize(1);
		assertThat(storedFiles().get(0).getName()).contains("application-test.yml");

		var entities = repository.findAll();

		assertThat(entities).hasSize(1);

		var file = entities.get(0);

		assertThat(file.name).isEqualTo("application-test.yml");

		var download = template.getForEntity("/file/" + file.id, byte[].class);

		assertThat(download.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(download.getHeaders().getContentType()).isEqualTo(MediaType.parseMediaType(file.contentType));
		assertThat(download.getHeaders().getContentDisposition().getFilename()).isEqualTo(file.name);
	}

	private List<File> storedFiles() {
		return Arrays.asList(Objects.requireNonNull(saveDir.listFiles()));
	}
}
