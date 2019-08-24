package core.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseHeaderSetterTest {

    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
    }

    @Test
    void setStatusOK() throws Exception {
        ResponseHeaderSetter.setStatusOK(response);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void setStatusCREATED() throws Exception {
        String location = "location!!";

        ResponseHeaderSetter.setStatusCREATED(response, location);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader(HttpHeaders.LOCATION)).isEqualTo(location);
    }

}