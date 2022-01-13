package com.jejuroad;

import com.jejuroad.common.HttpResponseBody;
import com.jejuroad.common.Message;
import com.jejuroad.dto.RestaurantRequest;
import com.jejuroad.dto.RestaurantResponse;
import com.jejuroad.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.List;

import static com.jejuroad.common.Message.COMMON_RESPONSE_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = MockMvcWebTestClient
            .bindTo(mockMvc)
            .build();
    }

    @Test
    @DisplayName("맛집 등록 성공 테스트")
    void testRegisteringRestaurant() {
        final Message expectedMessage = COMMON_RESPONSE_OK;
        final RestaurantResponse.Register expectedInformation = new RestaurantResponse.Register(1L);
        final RestaurantRequest.Register requestDto = RestaurantRequest.Register.builder()
            .name("우진해장국")
            .categories(List.of("restaurant", "caffe"))
            .introduction("'수요미식회'에 방영된, 따끈한 국물 요리로 해장하기 좋은 음식점")
            .wayToGo("동문 재래 시장에서 도보 9분")
            .zipcode("28921")
            .state("제주")
            .city("제주시")
            .simpleAddress("제주 시내")
            .detailAddress("제주특별자치도 제주시 서사로 11")
            .latitude(11.11)
            .longitude(22.22)
            .build();

        when(restaurantService.register(requestDto)).thenReturn(expectedInformation);

        webTestClient
            .post()
            .uri("/api/restaurants")
            .accept(APPLICATION_JSON)
            .bodyValue(requestDto)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-Type", "application/json")
            .expectBody(new ParameterizedTypeReference<HttpResponseBody<RestaurantResponse.Register>>() {
            })
            .consumeWith(response -> {
                HttpResponseBody<RestaurantResponse.Register> responseBody = response.getResponseBody();
                assertThat(responseBody.getCode()).isEqualTo(expectedMessage.getCode());
                assertThat(responseBody.getMessage()).isEqualTo(expectedMessage.getMessage());
                assertThat(responseBody.getInformation()).isEqualTo(expectedInformation);
            });

        verify(restaurantService).register(requestDto);
    }

    @Test
    @DisplayName("맛집 목록 조회 성공 테스트")
    void testFindRestaurants() {
        final Message expectedMessage = COMMON_RESPONSE_OK;
        final List<RestaurantResponse.Find> expectedInformation = List.of(
            new RestaurantResponse.Find(
                1L,
                "우진해장국",
                List.of("RESTAURANT", "CAFFE"),
                "제주 시내",
                "/images/image_001",
                "'수요미식회'에 방영된, 따끈한 국물 요리로 해장하기 좋은 음식점"
            ),
            new RestaurantResponse.Find(
                2L,
                "원 앤 온리",
                List.of("CAFFE"),
                "중문",
                "/images/image_017",
                "'배틀트립'에서 다녀간, '산방산'의 이국적인 뷰를 감상할 수 있는 카페"
            )
        );

        when(restaurantService.find()).thenReturn(expectedInformation);

        webTestClient
            .get()
            .uri("/api/restaurants")
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-Type", "application/json")
            .expectBody(new ParameterizedTypeReference<HttpResponseBody<List<RestaurantResponse.Find>>>() {
            })
            .consumeWith(response -> {
                HttpResponseBody<List<RestaurantResponse.Find>> responseBody = response.getResponseBody();
                assertThat(responseBody.getCode()).isEqualTo(expectedMessage.getCode());
                assertThat(responseBody.getMessage()).isEqualTo(expectedMessage.getMessage());
                assertThat(responseBody.getInformation()).isEqualTo(expectedInformation);
            });

        verify(restaurantService).find();
    }


}