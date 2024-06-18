package com.ra.project_md04_api.model.dto.request;

import com.ra.project_md04_api.model.entity.ShoppingCart;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FormCheckout {
    @Size(max = 100, message = "Max character is 100 !")
    private String note;

    @Size(max = 100, message = "Max character is 100 !")
    private String receiveName;

    @Size(max = 255, message = "Max character is 255 !")
    private String receiveAddress;

    @Size(max = 15, message = "Max character is 15 !")
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$", message = "Invalid phone format!")
    private String receivePhone;

    @NotNull(message = "Shopping cart item must be not null!")
    List<ShoppingCart> shoppingCarts;
}
