package com.example.identityservice.service;

import com.example.identityservice.dto.request.AuthenticationRequest;
import com.example.identityservice.dto.request.IntrospectRequest;
import com.example.identityservice.dto.request.LogoutRequest;
import com.example.identityservice.dto.request.RefreshTokenRequest;
import com.example.identityservice.dto.response.AuthenticationResponse;
import com.example.identityservice.dto.response.IntrospectResponse;
import com.example.identityservice.enity.InvalidatedToken;
import com.example.identityservice.enity.User;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.exception.ErrorCode;
import com.example.identityservice.repository.InvalidatedTokenRepository;
import com.example.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long validDuration;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long refreshDuration;

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws ParseException, JOSEException {
        var token = introspectRequest.getToken();
        boolean valid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            valid = false;
        }

        return IntrospectResponse.builder()
                .valid(valid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getStatus().getName().equals("INACTIVE")) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        boolean authenticated = passwordEncoder
                .matches(authenticationRequest.getPassword(), user.getPasswordHash());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public String logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = verifyToken(logoutRequest.getToken(), true);

            String jti = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException _) {

        }
        
        return null;
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(refreshTokenRequest.getToken(), true);

        String jwtid = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        String subject = signedJWT.getJWTClaimsSet().getSubject();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtid)
                .expiryTime(expirationTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        User user = userRepository.findByUsername(subject).orElseThrow(() ->
                new AppException(ErrorCode.UNAUTHENTICATED));

        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = isRefresh
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(refreshDuration, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(jwsVerifier);
        boolean valid = verified && expiryTime.after(new Date());

        if (!valid) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("localhost")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(validDuration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.CANT_CREATE_TOKEN);
        }

        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }

        return stringJoiner.toString();
    }
}
