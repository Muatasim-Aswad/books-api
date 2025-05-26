package com.asim.authentication.core.model.dto;

import com.asim.authentication.core.model.dto.annotation.validation.props.Name;
import com.asim.authentication.core.model.dto.annotation.validation.props.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for receiving user credentials.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInput {
    @Name
    private String name;

    @Password
    private String password;
}
