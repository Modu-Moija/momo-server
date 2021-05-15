package com.momo.server.dto;

import com.momo.server.domain.Meet;
import com.momo.server.domain.User;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import lombok.Data;

@Component
@Data
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private User user;
    private Meet meet;

}
