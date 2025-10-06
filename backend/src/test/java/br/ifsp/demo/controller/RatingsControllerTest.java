package br.ifsp.demo.controller;

import br.ifsp.demo.security.auth.AuthenticationInfoService;
import br.ifsp.demo.security.config.JwtService;
import br.ifsp.demo.service.get.GetRatedMoviesService;
import br.ifsp.demo.service.post.PostRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RatingsController.class)
public class RatingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationInfoService authenticationInfoService;

    @MockitoBean
    private GetRatedMoviesService getRatedMoviesService;

    @MockitoBean
    private PostRateService postRateService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-2]")
    @DisplayName("[SC-2.4] - Should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Tag("UnitTest")
    @Tag("TDD")
    @Tag("[US-1]")
    @DisplayName("[SC-1.5] - Should return 401 when posting a rating without auth")
    void shouldReturnUnauthorizedWhenPostingRatingWithoutAuth() throws Exception {
        String ratingBody = "{}";
        mockMvc.perform(get("/api/v1/ratings")
                        .contentType(MediaType.APPLICATION_JSON).content(ratingBody))
                .andExpect(status().isUnauthorized());
    }
}
