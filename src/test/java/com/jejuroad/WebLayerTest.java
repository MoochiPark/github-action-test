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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@WebMvcTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        webTestClient = MockMvcWebTestClient
            .bindTo(mockMvc)
            .filter(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(prettyPrint())
                    .withResponseDefaults(prettyPrint()))
            .build();
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    void testRegisteringRestaurant() {
        final Message expectedMessage = COMMON_RESPONSE_OK;
        final RestaurantResponse.Register expectedInformation = new RestaurantResponse.Register(1L);
        final RestaurantRequest.Register requestDto = RestaurantRequest.Register.builder()
            .name("???????????????")
            .categories(List.of("restaurant", "caffe"))
            .introduction("'???????????????'??? ?????????, ????????? ?????? ????????? ???????????? ?????? ?????????")
            .wayToGo("?????? ?????? ???????????? ?????? 9???")
            .zipcode("28921")
            .state("??????")
            .city("?????????")
            .simpleAddress("?????? ??????")
            .detailAddress("????????????????????? ????????? ????????? 11")
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
            })
            .consumeWith(
                document(
                    "restaurants/register",
                    requestFields(
                        fieldWithPath("name").description("????????? ??????"),
                        fieldWithPath("categories").description("????????? ??????"),
                        fieldWithPath("introduction").description("?????? ??????"),
                        fieldWithPath("wayToGo").description("????????????"),
                        fieldWithPath("zipcode").description("????????????"),
                        fieldWithPath("state").description("????????????????????????(???/???)"),
                        fieldWithPath("city").description("????????????????????????(???/???/???)"),
                        fieldWithPath("simpleAddress").description("????????? ??????"),
                        fieldWithPath("detailAddress").description("?????? ??????"),
                        fieldWithPath("latitude").description("??????"),
                        fieldWithPath("longitude").description("??????")
                    ),
                    responseFields(
                        beneathPath("information").withSubsectionId("information"),
                        fieldWithPath("id").description("????????? ????????? ?????????")
                    )
                )
            );

        verify(restaurantService).register(requestDto);
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    void testFindRestaurants() {
        final Message expectedMessage = COMMON_RESPONSE_OK;
        final List<RestaurantResponse.Find> expectedInformation = List.of(
            new RestaurantResponse.Find(
                1L,
                "???????????????",
                List.of("RESTAURANT", "CAFFE"),
                "?????? ??????",
                "/images/image_001",
                "'???????????????'??? ?????????, ????????? ?????? ????????? ???????????? ?????? ?????????"
            ),
            new RestaurantResponse.Find(
                2L,
                "??? ??? ??????",
                List.of("CAFFE"),
                "??????",
                "/images/image_017",
                "'????????????'?????? ?????????, '?????????'??? ???????????? ?????? ????????? ??? ?????? ??????"
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
            })
            .consumeWith(
                document(
                    "restaurants/findAll",
                    responseFields(
                        beneathPath("information").withSubsectionId("information"),
                        fieldWithPath("id").description("?????? ?????????"),
                        fieldWithPath("name").description("????????? ??????"),
                        fieldWithPath("categories").description("????????? ??????"),
                        fieldWithPath("address").description("????????? ????????? ??????"),
                        fieldWithPath("image").description("????????? ?????? ??????"),
                        fieldWithPath("introduction").description("?????? ?????????")
                    )
                )
            );

        verify(restaurantService).find();
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void testFindById() {
        final Message expectedMessage = COMMON_RESPONSE_OK;
        final RestaurantResponse.FindWithDetail expectedInformation =
            RestaurantResponse.FindWithDetail.builder()
                .id(1L)
                .name("???????????????")
                .images(List.of("/images/image_001", "/images/image_002"))
                .menus(List.of(
                    new RestaurantResponse.FindWithDetail.Menu(1L, "????????? ?????????", 9000, "/images/image_003"),
                    new RestaurantResponse.FindWithDetail.Menu(2L, "??????", 9000, "/images/image_004")
                ))
                .wayToGo("?????? ?????? ???????????? ?????? 9???")
                .simpleAddress("?????? ??????")
                .detailAddress("????????????????????? ????????? ????????? 111")
                .openTimes(List.of(
                    new RestaurantResponse.FindWithDetail.OpenTime("MON", "08:00-22:00", "15:00-17:00"),
                    new RestaurantResponse.FindWithDetail.OpenTime("TUE", "08:00-22:00", "15:00-17:00"),
                    new RestaurantResponse.FindWithDetail.OpenTime("WED", "08:00-22:00", "15:00-17:00"),
                    new RestaurantResponse.FindWithDetail.OpenTime("THU", "08:00-22:00", "15:00-17:00"),
                    new RestaurantResponse.FindWithDetail.OpenTime("SAT", "08:00-22:00", "15:00-17:00"),
                    new RestaurantResponse.FindWithDetail.OpenTime("SUN", "08:00-22:00", "15:00-17:00")
                ))
                .introduction("'???????????????'??? ?????????, ????????? ?????? ????????? ???????????? ?????? ?????????")
                .tips(List.of(
                    "?????? ?????? ????????? ?????? ??????",
                    "?????? ????????? ??????"
                ))
                .build();


        when(restaurantService.findById(1L)).thenReturn(expectedInformation);

        webTestClient
            .get()
            .uri("/api/restaurants/{id}", 1L)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().valueEquals("Content-Type", "application/json")
            .expectBody(new ParameterizedTypeReference<HttpResponseBody<RestaurantResponse.FindWithDetail>>() {
            })
            .consumeWith(response -> {
                HttpResponseBody<RestaurantResponse.FindWithDetail> responseBody = response.getResponseBody();
                assertThat(responseBody.getCode()).isEqualTo(expectedMessage.getCode());
                assertThat(responseBody.getMessage()).isEqualTo(expectedMessage.getMessage());
                assertThat(responseBody.getInformation()).isEqualTo(expectedInformation);
            })
            .consumeWith(
                document(
                    "restaurants/findById",
                    pathParameters(
                        parameterWithName("id").description("?????? ?????????")
                    ),
                    responseFields(
                        beneathPath("information").withSubsectionId("information"),
                        fieldWithPath("id").description("?????? ?????????"),
                        fieldWithPath("name").description("????????? ??????"),
                        fieldWithPath("images").description("????????? ?????? ??????"),
                        subsectionWithPath("menus").description("?????? ??????"),
                        fieldWithPath("menus.[].id").description("?????? ?????????"),
                        fieldWithPath("menus.[].name").description("?????? ??????"),
                        fieldWithPath("menus.[].price").description("?????? ??????"),
                        fieldWithPath("menus.[].image").description("?????? ????????? ??????"),
                        fieldWithPath("way_to_go").description("?????? ??????"),
                        fieldWithPath("simple_address").description("????????? ??????"),
                        fieldWithPath("detail_address").description("?????? ??????"),
                        subsectionWithPath("open_times").description("??????????????????"),
                        fieldWithPath("open_times.[].day").description("??????"),
                        fieldWithPath("open_times.[].serving_time").description("?????? ??????"),
                        fieldWithPath("open_times.[].break_time").description("???????????? ??????"),
                        fieldWithPath("introduction").description("?????? ?????????"),
                        fieldWithPath("tips").description("?????????")
                    )
                )
            );

        verify(restaurantService).findById(1L);
    }


}
