package com.epam.finaltask.controller;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.enums.*;
import com.epam.finaltask.service.VoucherService;
import com.epam.finaltask.service.impl.AuthenticationServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class VoucherControllerTest {
    @MockBean
    private VoucherService voucherService;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Disabled
        //todo update test with page
    void findAll_Successfully() throws Exception {
        List<VoucherDTO> voucherDTOList = new ArrayList<>();
        when(voucherService.findAll(Pageable.unpaged())).thenReturn(new PageImpl<>(voucherDTOList));
        MvcResult result = mockMvc.perform(get("/vouchers"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");

        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {
        });

        assertEquals(voucherDTOList, responseVoucherDTOList);
    }

    @Test
    @WithMockUser
    @Disabled
        //todo update test with page
    void findAllByUserId_Successfully() throws Exception {
        String userId = String.valueOf(UUID.randomUUID());
        List<VoucherDTO> voucherDTOList = new ArrayList<>();

        when(voucherService.findAllByUserId(userId, Pageable.unpaged())).thenReturn(new PageImpl<>(voucherDTOList));
        when(authenticationService.isCurrentUser(any(UUID.class))).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/vouchers/userId/" + userId))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode resultsNode = rootNode.path("results");

        List<VoucherDTO> responseVoucherDTOList = objectMapper.convertValue(resultsNode, new TypeReference<List<VoucherDTO>>() {
        });

        assertEquals(voucherDTOList, responseVoucherDTOList);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void createVoucher_ValidData_SuccessfullyCreated() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTitle("SummerSale2024");
        voucherDTO.setDescription("Summer Sale Voucher Description");
        voucherDTO.setPrice(BigDecimal.valueOf(299.99));
        voucherDTO.setTourType(TourType.ADVENTURE.name());
        voucherDTO.setTransferType(TransferType.PLANE.name());
        voucherDTO.setHotelType(HotelType.FIVE_STARS.name());
        voucherDTO.setArrivalDate(LocalDate.now());
        voucherDTO.setEvictionDate(LocalDate.now().plusDays(2));
        voucherDTO.setUserId(UUID.randomUUID());
        voucherDTO.setIsHot("false");

        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher is successfully created";

        when(voucherService.create(any(VoucherDTO.class))).thenReturn(voucherDTO);

        mockMvc.perform(post("/vouchers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void updateVoucher_ValidData_SuccessfullyUpdated() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setTitle("UpdatedTitle");
        voucherDTO.setDescription("Updated description");
        voucherDTO.setPrice(BigDecimal.valueOf(499.99));
        voucherDTO.setTourType(TourType.SAFARI.name());
        voucherDTO.setTransferType(TransferType.JEEPS.name());
        voucherDTO.setHotelType(HotelType.THREE_STARS.name());
        voucherDTO.setStatus(VoucherStatus.PAID.name());
        voucherDTO.setArrivalDate(LocalDate.now());
        voucherDTO.setEvictionDate(LocalDate.now().plusDays(2));
        voucherDTO.setUserId(UUID.randomUUID());
        voucherDTO.setIsHot("true");

        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher is successfully updated";

        when(voucherService.update(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);

        mockMvc.perform(put("/vouchers/" + voucherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteVoucherById_VoucherExists_SuccessfullyDeleted() throws Exception {
        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = String.format("Voucher with id %s has been deleted", voucherId);

        doNothing().when(voucherService).delete(voucherId);

        mockMvc.perform(delete("/vouchers/" + voucherId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));

        verify(voucherService, times(1)).delete(voucherId);
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void deleteVoucherById_VoucherDoesNotExist_NotFound() throws Exception {
        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = StatusCodes.ENTITY_NOT_FOUND.name();
        String expectedMessage = String.format("Voucher with id %s not found", voucherId);

        doThrow(new EntityNotFoundException(StatusCodes.ENTITY_NOT_FOUND.name(), String.format("Voucher with id %s not found", voucherId)))
                .when(voucherService).delete(voucherId);

        mockMvc.perform(delete("/vouchers/" + voucherId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));

        verify(voucherService, times(1)).delete(voucherId);
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "ROLE_MANAGER"})
    void changeVoucherHotStatus_ValidData_SuccessfullyChanged() throws Exception {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setIsHot("true");

        String voucherId = String.valueOf(UUID.randomUUID());
        String expectedStatusCode = "OK";
        String expectedMessage = "Voucher hot status is successfully changed";

        when(voucherService.changeHotStatus(eq(voucherId), any(VoucherDTO.class))).thenReturn(voucherDTO);

        mockMvc.perform(patch("/vouchers/" + voucherId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(voucherDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(expectedStatusCode))
                .andExpect(jsonPath("$.statusMessage").value(expectedMessage));

        verify(voucherService, times(1)).changeHotStatus(eq(voucherId), any(VoucherDTO.class));
    }
}
