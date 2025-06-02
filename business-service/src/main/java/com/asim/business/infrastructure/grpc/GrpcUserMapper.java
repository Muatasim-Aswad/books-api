package com.asim.business.infrastructure.grpc;

import com.asim.business.domain.user.model.dto.UserCreateDto;
import com.asim.grpc.generated.NewUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrpcUserMapper {
    protected final ModelMapper modelMapper;

    public UserCreateDto toUserCeateDto(NewUser newUser) {
        return modelMapper.map(newUser, UserCreateDto.class);
    }
}
