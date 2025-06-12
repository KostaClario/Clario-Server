package com.oopsw.clario.exception.history;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HistoryAdviceTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    public void historyNullPointException() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/null", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("NULL발생");
    }
    @Test
    public void historySaveFailTest() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/save", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).contains("저장 실패");
    }
    @Test
    public void historyResourceTest(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/resource", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("리소스 접근 불가");
    }
}
