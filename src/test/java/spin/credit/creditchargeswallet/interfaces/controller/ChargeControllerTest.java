package spin.credit.creditchargeswallet.interfaces.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import spin.cedit.creditchargeswallet.ServiceApplication;
import spin.cedit.creditchargeswallet.application.service.ChargeCompensationService;
import spin.cedit.creditchargeswallet.domain.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ChargeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChargeCompensationService compensationService;

    private static String createdChargeId;
    private static UUID createdTransactionId;

    private String loadResource(String path) throws Exception {
        return java.nio.file.Files.readString(java.nio.file.Paths.get("src/test/resources/" + path));
    }

    @Test
    @Order(1)
    void testCreateCharge() throws Exception {
        String jsonRequest = loadResource("mockdata/create-charge-request.json");

        var mvcResult = mockMvc.perform(post("/v1/api/charges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chargeId").exists())
                .andExpect(jsonPath("$.amortizationTable", org.hamcrest.Matchers.hasSize(12)))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andReturn();

        String responseJson = mvcResult.getResponse().getContentAsString();
        printPretty("CreateCharge Response", responseJson);

        createdChargeId = objectMapper.readTree(responseJson).get("chargeId").asText();
        createdTransactionId = UUID.fromString(objectMapper.readTree(responseJson).get("transactionId").asText());
        Assertions.assertNotNull(createdChargeId);
        Assertions.assertNotNull(createdTransactionId);
    }

    @Test
    @Order(2)
    void testCompensateCharge() throws Exception {
        Charge mockCharge = new Charge();
        mockCharge.setId(UUID.fromString(createdChargeId));
        mockCharge.setTransactionId(createdTransactionId);
        mockCharge.setStatus(ChargeStatus.CANCELADO);
        mockCharge.setCreatedAt(LocalDateTime.now().minusDays(1));
        mockCharge.setUpdatedAt(LocalDateTime.now());
        mockCharge.setWalletId(UUID.randomUUID());
        mockCharge.setAmount(BigDecimal.valueOf(10000));
        mockCharge.setMonths(12);
        mockCharge.setAnnualRate(BigDecimal.valueOf(0.18));
        mockCharge.setAmortizationType(AmortizationType.FRANCESA);
        mockCharge.setDetails(List.of());

        Mockito.when(compensationService.compensateCharge(createdTransactionId))
                .thenReturn(mockCharge);

        mockMvc.perform(post("/v1/api/charges/compensate/" + createdTransactionId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId", is(createdTransactionId.toString())))
                .andExpect(jsonPath("$.status", is("CANCELADO")))
                .andExpect(jsonPath("$.chargeId", is(createdChargeId)));
    }

    private void printPretty(String title, String json) throws Exception {
        var pretty = objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                .readTree(json)
                .toPrettyString();
        System.out.println("\n=== " + title + " ===");
        System.out.println(pretty);
        System.out.println("========================\n");
    }
}
