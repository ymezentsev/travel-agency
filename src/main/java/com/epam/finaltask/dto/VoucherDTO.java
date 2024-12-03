package com.epam.finaltask.dto;

/*import com.epam.finaltask.dto.group.OnChange;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;*/
import com.epam.finaltask.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Dto for voucher")
public class VoucherDTO {
/*    @NotBlank
    @Schema(description = "User email", example = "johndoe@gmail.com")*/
    private String id;
    private String title;
    private String description;
    private double price;
    private String tourType;
    private String transferType;
    private String hotelType;
    private String status;
    private LocalDate arrivalDate;
    private LocalDate evictionDate;
    private UUID userId;
    private String isHot;
}
